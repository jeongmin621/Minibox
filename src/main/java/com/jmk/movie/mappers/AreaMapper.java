package com.jmk.movie.mappers;

import com.jmk.movie.entities.AreaEntity;
import com.jmk.movie.entities.TheaterEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AreaMapper { // 극장의 지역을 나타내는곳
    int insertArea(AreaEntity area);

    int insertTheater(TheaterEntity theater);


    AreaEntity[] selectAreas();

    TheaterEntity[] selectTheaters();

}
