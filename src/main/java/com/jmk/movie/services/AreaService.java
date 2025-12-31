package com.jmk.movie.services;

import com.jmk.movie.entities.AreaEntity;
import com.jmk.movie.entities.TheaterEntity;
import com.jmk.movie.mappers.AreaMapper;
import com.jmk.movie.results.theater.NameCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaService { // 극장의 지역을 나타내는곳
    private final AreaMapper areaMapper;


    @Autowired
    public AreaService(AreaMapper areaMapper) {
        this.areaMapper = areaMapper;
    }

    public AreaEntity[] selectAreas() {
        return this.areaMapper.selectAreas();
    }

    public TheaterEntity[] selectTheaters() {
        return this.areaMapper.selectTheaters();
    }

    public NameCheck name(AreaEntity theater) {
        if (theater.getName() == null || theater.getName().length() < 2 || theater.getName().length() > 10) {
            return NameCheck.FAILURE;
        }
        int affectedRows = this.areaMapper.insertArea(theater);
        return affectedRows > 0 ? NameCheck.SUCCESS : NameCheck.FAILURE;
    }
}
