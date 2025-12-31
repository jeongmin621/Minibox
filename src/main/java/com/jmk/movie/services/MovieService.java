package com.jmk.movie.services;

import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.entities.MovieEntity;
import com.jmk.movie.mappers.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper movieMapper;
    public MovieDetailEntity[] getFilmMovies() {
        return movieMapper.selectFilmMovies();
    }

    public MovieDetailEntity[] getComingSoonMovies() {
        return movieMapper.selectComingSoonMovies();
    }

    public MovieBoxOffice[] getMovieBoxOffices() {
        return movieMapper.selectMoviesByRank();
    }

    public MovieEntity findByMovieCd(String movieCd) {
        return movieMapper.findByMovieCd(movieCd);
    }

    // 박스오피스 영화별 포스터 URL 리스트
    public List<List<?>> getPostersByMovie() {
        List<String> postersList = movieMapper.selectAllPosters();

        return postersList.stream()
                .map(posters -> {
                    if (posters == null || posters.isEmpty()) return new ArrayList<>();
                    return Arrays.stream(posters.split(", "))
                            .map(String::trim)
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());
    }

    // 박스오피스 영화별 메인 포스터 가져오기
    public Map<String,String> getMainPoster() {
        Map<String,String> posterMap = new HashMap<>();     // 영화코드에 맞는 메인포스터를 가져오기 위해 Map 사용
        List<List<?>> postersList = getPostersByMovie();    // 영화별 포스터 url 리스트 -> [[영화1 포스터 리스트], [영화2 포스터 리스트], ... , [영화 10 포스터 리스트]]
        int i = 0;
        for (String movieCd : getAllMovieCds()) {                    // 영화 1위 ~ 10위의 영화코드
            List<?> posters = postersList.get(i);                    // 순위 (i+1)위의 영화 포스터 리스트
            String[] mainPosters = new String[postersList.size()];   // 메인포스터 리스트 생성
            mainPosters[i] = (String)posters.get(0);                 // 리스트에 1~10위 영화 메인 포스터 추가

            posterMap.put(movieCd, mainPosters[i]);         // Map<영화코드, 영화 메인 포스터>
            i++;
        }
        return posterMap;
    }

    // 영화별 포스터 리스트
    public List<String> getPosterListAtMovies(String movieCd) {
        String urls = movieMapper.selectMoviePoster(movieCd);
        return Arrays.asList(urls.split(","));
    }

    // 영화별 메인 포스터 가져오기
    public Map<String, String> getMainPosterAtMovies() {
        Map<String, String> posterMap = new HashMap<>();
        for (String movieCd : getAllMovieCdsAtMovies()) {
            posterMap.put(movieCd, getPosterListAtMovies(movieCd).get(0));
        }
        return posterMap;
    }

    public void saveMovie(MovieEntity movieEntity) {
        movieMapper.insertMovieCd(movieEntity);
    }

    public List<String> getAllMovieCds() {
        return movieMapper.selectAllMovieCds();
    }
    public List<String> getAllMovieCdsAtMovies() {
        return movieMapper.selectAllMovieCdsAtMovies();
    }

    public List<String> getAllMovieNms() {
        return movieMapper.selectAllMovieNms();
    }

    public List<String> getAllOpenDts() {
        return movieMapper.selectAllOpenDts();
    }
}
