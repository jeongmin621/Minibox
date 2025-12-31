package com.jmk.movie.services;

import com.jmk.movie.entities.ImageEntity;
import com.jmk.movie.entities.TheaterEntity;
import com.jmk.movie.mappers.ImageMapper;
import com.jmk.movie.mappers.TheaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheaterService { // 극장의 지점을 나타내는곳
    private final TheaterMapper theaterMapper;
    private final ImageMapper imageMapper;

    @Autowired
    public TheaterService(TheaterMapper theaterMapper, ImageMapper imageMapper) {
        this.theaterMapper = theaterMapper;
        this.imageMapper = imageMapper;
    }

    public ImageEntity getImage(int index) {
        if (index < 1) {
            return null;
        }
        return this.imageMapper.selectImageByIndex(index);
    }

    public boolean uploadImage(ImageEntity image) {
        if (image == null ||
            image.getData() == null || image.getData().length == 0 ||
            image.getContentType() == null || image.getContentType().isEmpty() ||
            image.getName() == null || image.getName().isEmpty()) {
            return false;
        }
        return this.imageMapper.insertImage(image) > 0;
    }

    public TheaterEntity getTheater(String brchCode) {
        if (brchCode == null) {
            return null;
        }
        return this.theaterMapper.selectTheaters(brchCode);
    }
}

