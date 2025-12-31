package com.jmk.movie.results;

public interface Result {
    String NAME = "results";

    String name();

    default String nameToLower() {
        return name().toLowerCase();
    }
}
