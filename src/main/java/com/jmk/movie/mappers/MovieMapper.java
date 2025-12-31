package com.jmk.movie.mappers;

import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.entities.MovieEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieMapper {
    void insertMovieCd(MovieEntity movieEntity);

    MovieBoxOffice[] selectMoviesByRank();

    MovieEntity findByMovieCd(@Param("movieCd") String movieCd);

    // 박스오피스 영화 포스터 가져오기
    List<String> selectAllPosters();

    List<String> selectAllMovieCds();

    List<String> selectAllMovieNms();

    List<String> selectAllOpenDts();

    // 상영예정작 영화 가져오기
    MovieDetailEntity[] selectComingSoonMovies();

    // 상영예정작 영화 포스터 가져오기
    String selectMoviePoster(@Param("movieCd") String movieCd);

    List<String> selectAllMovieCdsAtMovies();

    // 필름 영화 가져오기
    MovieDetailEntity[] selectFilmMovies();
}
