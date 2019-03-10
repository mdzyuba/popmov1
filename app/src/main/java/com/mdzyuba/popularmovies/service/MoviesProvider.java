package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;

public interface MoviesProvider {

    URL getRequestUri(int page);

    @NonNull
    List<Movie> getMovies();

    boolean canLoadMoreMovies();

    void loadMovies() throws IOException;

}
