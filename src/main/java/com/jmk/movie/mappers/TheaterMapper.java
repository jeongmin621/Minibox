package com.jmk.movie.mappers;

import com.jmk.movie.entities.TheaterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TheaterMapper { // 극장의 지점을 나타내는곳
    int insertTheater(TheaterEntity theater);

    TheaterEntity selectTheaters(@Param("brchCode") String brchCode);

    TheaterEntity selectByBrchCode(String brchCode);
}


