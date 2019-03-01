package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

public class PopularMoviesProvider extends BaseMoviesProvider {

    private static final String TAG = PopularMoviesProvider.class.getSimpleName();

    public PopularMoviesProvider(NetworkDataProvider networkDataProvider) {
        super(networkDataProvider);
    }

    @NonNull
    @Override
    public List<Movie> getMovies() {
        URL popularMoviesUrl = new MovieApiClient().buildGetPopularMoviesUrl();
        return getMovieList(popularMoviesUrl);
    }
}
