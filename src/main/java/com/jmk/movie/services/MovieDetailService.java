package com.jmk.movie.services;

import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.mappers.MovieDetailMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class MovieDetailService {
    private final MovieDetailMapper movieDetailMapper;

    @Autowired
    public MovieDetailService(MovieDetailMapper movieDetailMapper) {
        this.movieDetailMapper = movieDetailMapper;
    }

    public MovieDetailEntity getMovieDetail(String movieCd) {
        // DB 조회 영화 객체 반환
        return movieDetailMapper.selectMovieByMovieCd(movieCd);
    }

    // 영화별 포스터 url 리스트
    public String getMoviePosters(String movieCd) {
        return movieDetailMapper.selectMoviePosterByMovieCd(movieCd);
    }

    // 영화 순위 가져오기
    public String getMovieBoxOffice(String movieCd) {
        if (movieDetailMapper.selectMovieBoxOffice(movieCd) != null) {
            return movieDetailMapper.selectMovieBoxOffice(movieCd);
        } else {
            return "-";
        }
    }

    // 누적관객수 가져오기
    public String getMovieAudiAcc(String movieCd) {
        String audiAcc = this.movieDetailMapper.selectMovieAudiAcc(movieCd);
        if (audiAcc != null) {
            // 100000000 -> 100,000,000 변환
            return audiAcc.replaceAll("(?<=\\d)(?=(\\d{3})+$)", ",");
        } else {
            return "0";
        }
    }

    // 영화진흥 위원회 통합전산망제공 기준 날짜 (어제)
    public String getStandardDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        calendar.add(Calendar.DATE, -1);
        return formatter.format(calendar.getTime());
    }

    // 영화 관람가
    public String getWatchGrade(String movieCd) {
        String watchGradeNm = this.movieDetailMapper.selectWatchGrade(movieCd);
        if (watchGradeNm != null) {
            String grade;
            if (watchGradeNm.contains("전체관람가")) {
                grade = "age-all";
            } else if (watchGradeNm.contains("12세이상관람가")) {
                grade = "age-12";
            } else if (watchGradeNm.contains("15세이상관람가")) {
                grade = "age-15";
            } else if (watchGradeNm.contains("청소년관람불가")) {
                grade = "age-19";
            } else {
                grade = "";
            }
            return grade;
        } else {
            return "미정";
        }
    }


    public List<String> posterList(String movieCd) {
        return Arrays.asList(getMoviePosters(movieCd).split(",\\s*"));
    }

    public MovieDetailEntity findByMovieCd(String movieCd) {
        return movieDetailMapper.findByMovieCd(movieCd);
    }

    public void saveMovieInfo(MovieDetailEntity movieDetailEntity) {
        movieDetailMapper.insertMovieInfo(movieDetailEntity);
    }

}



