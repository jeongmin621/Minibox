package com.jmk.movie.mappers;

import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieDetailMapper {
    void insertMovieInfo(MovieDetailEntity movieDetailEntity);

    MovieDetailEntity findByMovieCd(String movieCd);

    MovieDetailEntity selectMovieByMovieCd(String movieCd);

    String selectMoviePosterByMovieCd(String movieCd);

    String selectMovieBoxOffice(String movieCd);

    String selectMovieAudiAcc(String movieCd);

    String selectWatchGrade(String movieCd);
}
