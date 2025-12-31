package com.jmk.movie;

import com.jmk.movie.component.Movie;
import com.jmk.movie.mappers.MovieMapper;
import com.jmk.movie.services.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieApplicationTests {
    @Autowired
    private Movie movie;
    @Autowired
    private MovieMapper movieMapper;


}
