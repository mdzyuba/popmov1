package com.mdzyuba.popularmovies.service;

import androidx.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.util.List;

public interface MoviesProvider {
    @NonNull
    List<Movie> getMovies() throws IOException;

    boolean isInitialized();
}
