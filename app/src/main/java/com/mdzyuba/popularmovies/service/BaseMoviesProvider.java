package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    @NonNull
    List<Movie> getMovieList(URL getMoviesURL) throws IOException {
        if (getMoviesURL == null) {
            throw new InvalidParameterException("The url should not be null");
        }
        String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
        MovieParser movieParser = new MovieParser();
        List<Movie> movies = movieParser.parseMovies(json);
        return movies;
    }
}
