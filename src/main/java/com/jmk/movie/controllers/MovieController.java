package com.jmk.movie.controllers;

import com.jmk.movie.component.Movie;
import com.jmk.movie.entities.MovieBoxOffice;
import com.jmk.movie.entities.MovieDetailEntity;
import com.jmk.movie.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "movie")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getAllMovie() {
        try {

            ModelAndView modelAndView = new ModelAndView();
            MovieBoxOffice[] movieBoxOffices = this.movieService.getMovieBoxOffices();
            modelAndView.addObject("movieBoxOffices", movieBoxOffices);

            modelAndView.setViewName("movie/movie");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/comingsoon", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getComingSoonMovie() {
        try {

            ModelAndView modelAndView = new ModelAndView();
            MovieDetailEntity[] comingSoonMovies = this.movieService.getComingSoonMovies();
            modelAndView.addObject("comingSoonMovies", comingSoonMovies);

            // 영화 메인 포스터 가져오기
            Map<String, String> mainPosterAtMoviesMap = this.movieService.getMainPosterAtMovies();
            modelAndView.addObject("mainPosterAtMoviesMap", mainPosterAtMoviesMap);

            modelAndView.setViewName("movie/comingsoon");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/film", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getFilmMovie() {
        try {

            ModelAndView modelAndView = new ModelAndView();
            MovieDetailEntity[] filmMovies = this.movieService.getFilmMovies();
            modelAndView.addObject("filmMovies", filmMovies);

            // 영화 메인 포스터 가져오기
            Map<String, String> mainPosterAtMoviesMap = this.movieService.getMainPosterAtMovies();
            modelAndView.addObject("mainPosterAtMoviesMap", mainPosterAtMoviesMap);

            modelAndView.setViewName("movie/film");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
