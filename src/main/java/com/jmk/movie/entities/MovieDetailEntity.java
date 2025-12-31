package com.jmk.movie.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@EqualsAndHashCode()
public class MovieDetailEntity {
    private int id;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String showTypes;   // JSON 형태
    private String directors;   // JSON 형태
    private String actors;      // JSON 형태
    private String genres;      // JSON 형태
    private String showTm;
    private String watchGradeNm;
    private String openDt;
    private String prdtStatNm;
    private String typeNm;
    private String nationNm;
    private String poster;
    private String content;

    public void update(MovieDetailEntity newData) {
        // 기존 데이터와 새로운 데이터가 같을 경우 업데이트를 생략, 새로운 데이터의 값이 null 이 아닌 경우에만 업데이트

        if (newData.getMovieNm() != null && !newData.getMovieNm().equals(this.movieNm)) {
            this.movieNm = newData.getMovieNm();
        }
        if (newData.getMovieNmEn() != null && !newData.getMovieNmEn().equals(this.movieNmEn)) {
            this.movieNmEn = newData.getMovieNmEn();
        }
        if (newData.getShowTypes() != null && !newData.getShowTypes().equals(this.showTypes)) {
            this.showTypes = newData.getShowTypes();
        }
        if (newData.getDirectors() != null && !newData.getDirectors().equals(this.directors)) {
            this.directors = newData.getDirectors();
        }
        if (newData.getActors() != null && !newData.getActors().equals(this.actors)) {
            this.actors = newData.getActors();
        }
        if (newData.getGenres() != null && !newData.getGenres().equals(this.genres)) {
            this.genres = newData.getGenres();
        }
        if (newData.getShowTm() != null && !newData.getShowTm().equals(this.showTm)) {
            this.showTm = newData.getShowTm();
        }
        if (newData.getWatchGradeNm() != null && !newData.getWatchGradeNm().equals(this.watchGradeNm)) {
            this.watchGradeNm = newData.getWatchGradeNm();
        }
        if (newData.getOpenDt() != null && !newData.getOpenDt().equals(this.openDt)) {
            this.openDt = newData.getOpenDt();
        }
        if (newData.getPrdtStatNm() != null && !newData.getPrdtStatNm().equals(this.prdtStatNm)) {
            this.prdtStatNm = newData.getPrdtStatNm();
        }
        if (newData.getTypeNm() != null && !newData.getTypeNm().equals(this.typeNm)) {
            this.typeNm = newData.getTypeNm();
        }
        if (newData.getNationNm() != null && !newData.getNationNm().equals(this.nationNm)) {
            this.nationNm = newData.getNationNm();
        }
        if (newData.getPoster() != null && !newData.getPoster().equals(this.poster)) {
            this.poster = newData.getPoster();
        }
        if (newData.getContent() != null && !newData.getContent().equals(this.content)) {
            this.content = newData.getContent();
        }
    }

}
