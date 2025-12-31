package com.jmk.movie.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"index"})
public class TheaterDetailEntity { // 극장의 지점의 상세페이지를 나타내는곳
    private String detailCode;
    private String content;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int index;
}
