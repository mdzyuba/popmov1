package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    @NonNull
    List<Movie> getMovieList(URL getMoviesURL) {
        List<Movie> movies = new ArrayList<>();
        if (getMoviesURL == null) {
            throw new InvalidParameterException("The url should not be null");
        }
        try {
            String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
            MovieParser movieParser = new MovieParser();
            movies = movieParser.parseMovies(json);
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
        return movies;
    }
}
