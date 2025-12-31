package com.jmk.movie.component;

import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.entities.MovieEntity;
import com.jmk.movie.services.MovieDetailService;
import com.jmk.movie.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class Movie {

    private final MovieService movieService;
    private final MovieDetailService movieDetailService;

    @Autowired
    public Movie(MovieService movieService, MovieDetailService movieDetailService) {
        this.movieService = movieService;
        this.movieDetailService = movieDetailService;
    }

    @Transactional
    public void BoxOfficeData() {
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df"; // KOBIS 키
        String result = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        calendar.add(Calendar.DATE, -1);
        String targetDt = formatter.format(calendar.getTime());

        try {
            URL url = new URL("https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=" + key + "&targetDt=" + targetDt);
            log.info("Retrieved BoxOfficeUrl: {}", url);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            result = reader.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray boxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");

            for (Object obj : boxOfficeList) {
                JSONObject boxOffice = (JSONObject) obj;

                MovieEntity movieEntity = MovieEntity.builder()
                        .movieCd((String) boxOffice.get("movieCd"))
                        .movieNm((String) boxOffice.get("movieNm"))
                        .rank(Integer.parseInt((String) boxOffice.get("rank")))
                        .openDt((String) boxOffice.get("openDt"))
                        .audiAcc(Integer.parseInt((String) boxOffice.get("audiAcc")))
                        .build();

                String movieCd = (String) boxOffice.get("movieCd");
                MovieEntity existingEntity = movieService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    existingEntity.update(movieEntity);
                    movieService.saveMovie(existingEntity);
                } else {
                    movieService.saveMovie(movieEntity);
                }
            }
        } catch (Exception e) {
            log.error("Failed to process BoxOfficeData: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void MovieData() {
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df"; // KOBIS
        String dataKey = "JI2NJ01L6WLZQWR1P3D2"; // KMDB
        String result = "";

        // 1. 영화 코드 목록만 가져옵니다. (가장 확실한 기준)
        List<String> movieCdList = movieService.getAllMovieCds();

        log.info("==================================================");
        log.info("🎬 [MovieData] 수집 대상 영화 개수: {}개", movieCdList.size());
        log.info("==================================================");

        if (movieCdList.isEmpty()) {
            log.warn("⚠️ 경고: DB에 저장된 영화 코드가 없습니다. BoxOfficeData가 먼저 실행되었는지 확인하세요.");
            return;
        }

        // 2. 코드 하나하나 돌면서 영화 정보를 조회합니다. (안전한 방식)
        for (String movieCd : movieCdList) {
            try {
                // DB에서 영화 상세 정보 가져오기 (제목, 개봉일 확보)
                MovieEntity entity = movieService.findByMovieCd(movieCd);
                if (entity == null) continue;

                String movieNm = entity.getMovieNm();
                String openDt = entity.getOpenDt();

                log.info("🔍 검색 시작: {} ({})", movieNm, movieCd);

                // --- [여기부터 KOBIS API: 감독 정보 가져오기] ---
                URL url = new URL("https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key + "&movieCd=" + movieCd);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                result = reader.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");

                JSONArray showTypesArray = (JSONArray) movieInfo.get("showTypes");
                List<String> showTypeList = new ArrayList<>();
                if (showTypesArray != null) {
                    for (Object obj : showTypesArray) {
                        String type = (String) ((JSONObject) obj).get("showTypeNm");
                        showTypeList.add(type.replace("디지털", "2D"));
                    }
                }

                JSONArray directorsArray = (JSONArray) movieInfo.get("directors");
                List<String> directorList = new ArrayList<>();
                if (directorsArray != null) {
                    for (Object obj : directorsArray) {
                        directorList.add((String) ((JSONObject) obj).get("peopleNm"));
                    }
                }

                JSONArray actorsArray = (JSONArray) movieInfo.get("actors");
                List<String> actorList = new ArrayList<>();
                if (actorsArray != null) {
                    for (Object obj : actorsArray) {
                        actorList.add((String) ((JSONObject) obj).get("peopleNm"));
                    }
                }

                // --- [여기부터 KMDB API: 포스터 검색] ---
                // 공백 제거 및 인코딩
                String cleanTitle = movieNm.replaceAll("\\s+", "");
                String encodedTitle = URLEncoder.encode(cleanTitle, StandardCharsets.UTF_8.toString());

                String queryParam = "";
                if (!directorList.isEmpty()) {
                    String directorNm = directorList.get(0);
                    queryParam = "&director=" + URLEncoder.encode(directorNm, StandardCharsets.UTF_8.toString());
                } else if (openDt != null) {
                    String formattedOpenDt = openDt.replace("-", "");
                    queryParam = "&releaseDts=" + formattedOpenDt;
                }

                URL movieDataUrl = new URL("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&title=" + encodedTitle + queryParam + "&ServiceKey=" + dataKey);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(movieDataUrl.openStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = dataReader.readLine()) != null) sb.append(line);

                String dataResult = sb.toString();
                Document doc = Jsoup.parse(dataResult);
                Element jsonElement = doc.select("body").first();

                JSONObject resultObject = null;
                if (jsonElement != null) {
                    String json = jsonElement.html();
                    JSONParser dataJsonParser = new JSONParser();
                    JSONObject dataJsonObject = (JSONObject) dataJsonParser.parse(json);

                    JSONArray dataArray = (JSONArray) dataJsonObject.get("Data");
                    if (dataArray != null && !dataArray.isEmpty()) {
                        JSONObject dataObject = (JSONObject) dataArray.get(0);
                        JSONArray Result = (JSONArray) dataObject.get("Result");
                        if (Result != null && !Result.isEmpty()) {
                            resultObject = (JSONObject) Result.get(0);
                        }
                    }
                }

                // 포스터 URL 추출 (없으면 빈 문자열)
                String posterUrl = (resultObject != null) ? extractPoster(resultObject) : "";

                // [로그 확인] 포스터를 찾았는지 출력
                if (!posterUrl.isEmpty()) {
                    log.info("✅ 포스터 발견: {}", posterUrl);
                } else {
                    log.warn("❌ 포스터 못 찾음: {}", movieNm);
                }

                String plotText = (resultObject != null) ? extractContent(resultObject) : "줄거리 정보 없음";

                // DB 저장 객체 생성
                MovieDetailEntity movieDetailEntity = MovieDetailEntity.builder()
                        .movieCd((String) movieInfo.get("movieCd"))
                        .movieNm((String) movieInfo.get("movieNm"))
                        .movieNmEn((String) movieInfo.get("movieNmEn"))
                        .showTypes(showTypeList.toString().replace("[", "").replace("]", ""))
                        .directors(directorList.toString().replace("[", "").replace("]", ""))
                        .actors(actorList.toString().replace("[", "").replace("]", ""))
                        .genres(extractGenre(movieInfo))
                        .showTm((String) movieInfo.get("showTm"))
                        .watchGradeNm(extractWatchGrade(movieInfo))
                        .openDt(formattedOpenDt(movieInfo))
                        .prdtStatNm((String) movieInfo.get("prdtStatNm"))
                        .typeNm((String) movieInfo.get("typeNm"))
                        .nationNm(extractNation(movieInfo))
                        .poster(posterUrl)
                        .content(plotText)
                        .build();

                // DB 업데이트 또는 저장
                MovieDetailEntity existingEntity = movieDetailService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    existingEntity.update(movieDetailEntity);
                    movieDetailService.saveMovieInfo(existingEntity);
                } else {
                    movieDetailService.saveMovieInfo(movieDetailEntity);
                }

            } catch (Exception e) {
                log.error("영화 정보 처리 중 오류 발생 ({}): {}", movieCd, e.getMessage());
            }
        }
    }

    @Transactional
    public void ComingSoonMovieData() {
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df";
        String dataKey = "JI2NJ01L6WLZQWR1P3D2";

        Calendar calendarS = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        calendarS.add(Calendar.DATE, 0);
        String releaseDts = formatter.format(calendarS.getTime());

        Calendar calendarE = Calendar.getInstance();
        calendarE.add(Calendar.DATE, 60);
        String releaseDte = formatter.format(calendarE.getTime());

        try {
            URL dataUrl = new URL("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&ServiceKey=" + dataKey + "&releaseDts=" + releaseDts + "&releaseDte=" + releaseDte + "&listCount=50");

            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataUrl.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = dataReader.readLine()) != null) {
                sb.append(line);
            }
            String dataResult = sb.toString();

            Document doc = Jsoup.parse(dataResult);
            Element jsonElement = doc.select("body").first();
            String json = jsonElement.html();

            JSONParser dataJsonParser = new JSONParser();
            JSONObject dataJsonObject = (JSONObject) dataJsonParser.parse(json);

            JSONArray dataArray = (JSONArray) dataJsonObject.get("Data");
            if (dataArray == null || dataArray.isEmpty()) return;

            JSONObject dataObject = (JSONObject) dataArray.get(0);
            JSONArray Result = (JSONArray) dataObject.get("Result");

            if (Result == null) return;

            for (Object obj : Result) {
                JSONObject resultObject = (JSONObject) obj;

                String movieNm = (String) resultObject.get("title");
                movieNm = movieNm.replaceAll("!HS", "").replaceAll("!HE", "").trim();
                String formattedMovieNm = movieNm.replace(" ", "");

                JSONObject directorsObject = (JSONObject) resultObject.get("directors");
                JSONArray directorArray = (JSONArray) directorsObject.get("director");
                JSONObject director = (JSONObject) directorArray.get(0);
                String directorNm = (String) director.get("directorNm");

                String encodedMovieNm = URLEncoder.encode(formattedMovieNm, StandardCharsets.UTF_8.toString());
                String encodedDirectorNm = URLEncoder.encode(directorNm, StandardCharsets.UTF_8.toString());

                URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=" + key + "&movieNm=" + encodedMovieNm + "&directorNm=" + encodedDirectorNm);

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String result = reader.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject movieListResult = (JSONObject) jsonObject.get("movieListResult");

                if (movieListResult == null || movieListResult.isEmpty()) continue;

                JSONArray movieList = (JSONArray) movieListResult.get("movieList");
                if (movieList == null || movieList.isEmpty()) continue;

                JSONObject movie = (JSONObject) movieList.get(0);
                String movieCd = (String) movie.get("movieCd");

                URL infoUrl = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key + "&movieCd=" + movieCd);
                BufferedReader infoReader = new BufferedReader(new InputStreamReader(infoUrl.openStream()));
                result = infoReader.readLine();

                JSONParser infoJsonParser = new JSONParser();
                JSONObject infoJsonObject = (JSONObject) infoJsonParser.parse(result);
                JSONObject movieInfoResultObject = (JSONObject) infoJsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject) movieInfoResultObject.get("movieInfo");

                JSONArray genres = (JSONArray) movieInfo.get("genres");
                List<String> genreList = new ArrayList<>();
                if (genres != null) {
                    for (Object type : genres) genreList.add((String) ((JSONObject) type).get("genreNm"));
                }

                JSONArray directors = (JSONArray) movieInfo.get("directors");
                List<String> directorList = new ArrayList<>();
                if (directors != null) {
                    for (Object type : directors) directorList.add((String) ((JSONObject) type).get("peopleNm"));
                }

                JSONArray actors = (JSONArray) movieInfo.get("actors");
                List<String> actorList = new ArrayList<>();
                if (actors != null) {
                    for (Object type : actors) actorList.add((String) ((JSONObject) type).get("peopleNm"));
                }

                JSONArray showTypes = (JSONArray) movieInfo.get("showTypes");
                List<String> showTypeList = new ArrayList<>();
                if (showTypes != null) {
                    for (Object type : showTypes) {
                        String typeNm = (String) ((JSONObject) type).get("showTypeNm");
                        showTypeList.add(typeNm.replace("디지털", "2D"));
                    }
                }

                JSONArray audits = (JSONArray) movieInfo.get("audits");
                List<String> auditList = new ArrayList<>();
                if (audits != null) {
                    for (Object type : audits) auditList.add((String) ((JSONObject) type).get("watchGradeNm"));
                }

                JSONArray nations = (JSONArray) movieInfo.get("nations");
                List<String> nationList = new ArrayList<>();
                if (nations != null) {
                    for (Object type : nations) nationList.add((String) ((JSONObject) type).get("nationNm"));
                }

                MovieDetailEntity movieDetailEntity = MovieDetailEntity.builder()
                        .movieCd((String) movieInfo.get("movieCd"))
                        .movieNm((String) movieInfo.get("movieNm"))
                        .movieNmEn((String) movieInfo.get("movieNmEn"))
                        .showTypes(showTypeList.toString().replace("[", "").replace("]", ""))
                        .directors(directorList.toString().replace("[", "").replace("]", ""))
                        .actors(actorList.toString().replace("[", "").replace("]", ""))
                        .genres(genreList.toString().replace("[", "").replace("]", ""))
                        .showTm((String) movieInfo.get("showTm"))
                        .watchGradeNm(auditList.toString().replace("[", "").replace("]", ""))
                        .openDt(formattedOpenDt(movieInfo))
                        .prdtStatNm((String) movieInfo.get("prdtStatNm"))
                        .typeNm((String) movieInfo.get("typeNm"))
                        .nationNm(nationList.toString().replace("[", "").replace("]", ""))
                        .poster(extractPoster(resultObject))
                        .content(extractContent(resultObject))
                        .build();

                MovieDetailEntity existingEntity = movieDetailService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    existingEntity.update(movieDetailEntity);
                    movieDetailService.saveMovieInfo(existingEntity);
                } else {
                    movieDetailService.saveMovieInfo(movieDetailEntity);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error in ComingSoon: {}", e.getMessage());
        }
    }

    private String extractPoster(JSONObject resultObject) {
        if (resultObject == null) return "";
        String posters = (String) resultObject.get("posters");
        if (posters == null || posters.isEmpty()) {
            return "";
        }
        return posters.split("\\|")[0];
    }

    private String extractGenre(JSONObject movieInfo) {
        JSONArray genresArray = (JSONArray) movieInfo.get("genres");
        if (genresArray == null) return "";
        List<String> list = new ArrayList<>();
        for (Object obj : genresArray) list.add((String) ((JSONObject) obj).get("genreNm"));
        return String.join(", ", list);
    }

    private String extractWatchGrade(JSONObject movieInfo) {
        JSONArray auditsArray = (JSONArray) movieInfo.get("audits");
        if (auditsArray == null) return "";
        List<String> list = new ArrayList<>();
        for (Object obj : auditsArray) list.add((String) ((JSONObject) obj).get("watchGradeNm"));
        return String.join(", ", list);
    }

    private String formattedOpenDt(JSONObject movieInfo) {
        Object openDtObj = movieInfo.get("openDt");
        if (openDtObj == null) return "";
        String openDt = openDtObj.toString();
        if (openDt.trim().isEmpty()) return "";
        try {
            return LocalDate.parse(openDt, DateTimeFormatter.ofPattern("yyyyMMdd"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return openDt;
        }
    }

    private String extractNation(JSONObject movieInfo) {
        JSONArray nationsArray = (JSONArray) movieInfo.get("nations");
        if (nationsArray == null) return "";
        List<String> list = new ArrayList<>();
        for (Object obj : nationsArray) list.add((String) ((JSONObject) obj).get("nationNm"));
        return String.join(", ", list);
    }

    private String extractContent(JSONObject resultObject) {
        if (resultObject == null) return "";
        JSONObject plots = (JSONObject) resultObject.get("plots");
        if (plots == null) return "";
        JSONArray plotArray = (JSONArray) plots.get("plot");
        if (plotArray == null || plotArray.isEmpty()) return "";

        JSONObject firstPlot = (JSONObject) plotArray.get(0);
        return (String) firstPlot.get("plotText");
    }
}