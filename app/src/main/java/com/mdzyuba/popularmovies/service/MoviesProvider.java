package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesProvider {

    private static final String TAG = MoviesProvider.class.getSimpleName();

    private final NetworkDataProvider networkDataProvider;

    public MoviesProvider(NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    @NonNull
    public List<Movie> getPopularMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            MovieApiClient movieApiClient = new MovieApiClient();
            URL getMoviesURL = movieApiClient.buildGetPopularMoviesUrl();
            String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
            MovieParser movieParser = new MovieParser();
            movies = movieParser.parsePopularMovies(json);
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
        return movies;
    }

}
