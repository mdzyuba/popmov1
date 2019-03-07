package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

public abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;
    private boolean initialized;
    private List<Movie> movieList;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @NonNull
    List<Movie> getMovieList(URL getMoviesURL) throws IOException {
        if (getMoviesURL == null) {
            throw new InvalidParameterException("The url should not be null");
        }
        if (movieList == null || movieList.size() == 0) {
            String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
            MovieParser movieParser = new MovieParser();
            movieList = movieParser.parseMovies(json);
            if (movieList.size() > 0) {
                initialized = true;
            }
        }
        return movieList;
    }
}
