package com.mdzyuba.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class MovieCollection {

    private List<Movie> movieList;

    private int totalPages;

    private int furthestPage;

    public MovieCollection() {
        furthestPage = 0;
        totalPages = 0;
        movieList = new ArrayList<>();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getFurthestPage() {
        return furthestPage;
    }

    public void setFurthestPage(int furthestPage) {
        this.furthestPage = furthestPage;
    }
}
