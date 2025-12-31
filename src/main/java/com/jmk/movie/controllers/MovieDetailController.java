package com.jmk.movie.controllers;

import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.services.MovieDetailService;
import com.jmk.movie.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/movie-detail")
public class MovieDetailController {

    private final MovieDetailService movieDetailService;

    @Autowired
    public MovieDetailController(MovieDetailService movieDetailService) {
        this.movieDetailService = movieDetailService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getMovieDetail(@RequestParam(value = "movieCd") String movieCd) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            MovieDetailEntity movieDetail = this.movieDetailService.getMovieDetail(movieCd);
            modelAndView.addObject("movieDetail", movieDetail);

            // 영화 순위
            String rank = this.movieDetailService.getMovieBoxOffice(movieCd);
            modelAndView.addObject("rank", rank);

            // 누적관객수
            String formattedAudiAcc = this.movieDetailService.getMovieAudiAcc(movieCd);
            modelAndView.addObject("formattedAudiAcc", formattedAudiAcc);

            // 기준 날짜
            String standardDate = this.movieDetailService.getStandardDate();
            modelAndView.addObject("standardDate", standardDate);

            // 영화 관람가
            String watchGrade = this.movieDetailService.getWatchGrade(movieCd);
            modelAndView.addObject("watchGrade", watchGrade);

            // 영화 포스터 리스트
            List<String> posterList = this.movieDetailService.posterList(movieCd);
            modelAndView.addObject("posterList", posterList);

            modelAndView.setViewName("movie/movie-detail");
            return modelAndView;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<ReviewEntity[]> getReview(@RequestParam(value = "movieCd") String movieCd) {
//        if (reviews == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(reviews);
//    }

}
