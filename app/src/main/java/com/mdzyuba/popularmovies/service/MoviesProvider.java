package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.util.List;

public interface MoviesProvider {
    @NonNull
    List<Movie> getMovies() throws IOException;
}
