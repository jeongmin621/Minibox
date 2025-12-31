package com.jmk.movie.mappers;

import com.jmk.movie.entities.TheaterDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TheaterDetailMapper { // 극장의 지점의 상세페이지를 나타내는곳
    int insertTheaterDetail(TheaterDetailEntity theaterDetail);

    TheaterDetailEntity selectTheaterDetail(@Param("detailCode") String detailCode);

    int update(TheaterDetailEntity detail);

    // 12-17
    int insertDetail(TheaterDetailEntity detail);

    int updatedTheaterBrchDetail(TheaterDetailEntity detail);

    TheaterDetailEntity selectByDetailCode(String detailCode);

}
