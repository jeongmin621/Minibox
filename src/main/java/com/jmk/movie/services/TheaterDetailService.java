package com.jmk.movie.services;

import com.jmk.movie.entities.TheaterDetailEntity;
import com.jmk.movie.entities.TheaterEntity;
import com.jmk.movie.mappers.TheaterDetailMapper;
import com.jmk.movie.mappers.TheaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TheaterDetailService { // 극장의 지점의 상세페이지를 나타내는곳
    private final TheaterMapper theaterBrchMapper;
    private final TheaterDetailMapper theaterDetailMapper;

    @Autowired
    public TheaterDetailService(TheaterMapper theaterMapper, TheaterDetailMapper theaterDetailMapper) {
        this.theaterBrchMapper = theaterMapper;
        this.theaterDetailMapper = theaterDetailMapper;
    }

    public TheaterEntity getDetailByBrchCode(String brchCode) { // 지점코드
        if (brchCode == null || brchCode.isEmpty()) {
            return null;
        }
        return this.theaterBrchMapper.selectTheaters(brchCode);
    }

    public TheaterDetailEntity getDetailByDetailCode(String detailCode) { // 상세코드(지점의 지점코드와 같다)
        if (detailCode == null) {
            return null;
        }
        return this.theaterDetailMapper.selectTheaterDetail(detailCode);
    }

    // 12-17
    public boolean write(TheaterDetailEntity detail) {
        if (detail == null || detail.getDetailCode() == null ||
                detail.getContent() == null || detail.getContent().isEmpty() ||  detail.getContent().length() > 500000) {
            System.out.println("Invalid detail : " + detail);
            return false;
        }

        TheaterEntity theater = this.theaterBrchMapper.selectByBrchCode(detail.getDetailCode());
        if (theater == null) {
            return false;
        }

        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(null);
        detail.setDeletedAt(null);
        int affect = this.theaterDetailMapper.insertDetail(detail);
        System.out.println("Affected : " + affect);
        return affect > 0;
    }

    public boolean updateDetail(TheaterDetailEntity detail) {
        if (detail == null || detail.getDetailCode() == null || detail.getContent() == null || detail.getContent().isEmpty() ||  detail.getContent().length() > 500000) {
            System.out.println("Invalid detail : " + detail);
            return false;
        }
        TheaterDetailEntity theater = this.theaterDetailMapper.selectByDetailCode(detail.getDetailCode());
        if (theater == null) {
            // insert
            if (detail.getTitle() == null || detail.getTitle().isEmpty() || detail.getContent() == null || detail.getContent().isEmpty() || detail.getContent().length() > 500000) {
                return true;
            }
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(null);
            detail.setDeletedAt(null);
            int affectedRow = this.theaterDetailMapper.insertDetail(detail);
            System.out.println("Affected : " + affectedRow);
            return affectedRow > 0;
        } else {
            // update
            if (detail.getTitle() == null || detail.getTitle().isEmpty() || detail.getContent() == null || detail.getContent().isEmpty() || detail.getContent().length() > 500000) {
                return true;
            }
            detail.setUpdatedAt(LocalDateTime.now());
            int affectedRow = this.theaterDetailMapper.updatedTheaterBrchDetail(detail);
            System.out.println("Affected : " + affectedRow);
            return affectedRow > 0;
        }
//        detail.setUpdatedAt(LocalDateTime.now());
//        int affectedRow = this.theaterDetailMapper.updatedTheaterBrchDetail(detail);
//        System.out.println("Affected : " + affectedRow);
//        return affectedRow > 0;
    }


    public boolean update(TheaterDetailEntity detail) {
        if (detail == null ||
            detail.getTitle() == null || detail.getTitle().isEmpty() ||
            detail.getContent() == null ||detail.getContent().isEmpty()) {
            return false;
        }
        int affects = theaterDetailMapper.update(detail);
        return affects > 0;
    }
}
