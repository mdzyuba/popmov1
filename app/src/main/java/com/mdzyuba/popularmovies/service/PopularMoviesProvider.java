package com.mdzyuba.popularmovies.service;

import androidx.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PopularMoviesProvider extends BaseMoviesProvider {

    private static final String TAG = PopularMoviesProvider.class.getSimpleName();

    public PopularMoviesProvider(NetworkDataProvider networkDataProvider) {
        super(networkDataProvider);
    }

    @NonNull
    @Override
    public List<Movie> getMovies() throws IOException {
        URL popularMoviesUrl = new MovieApiClient().buildGetPopularMoviesUrl();
        return getMovieList(popularMoviesUrl);
    }
}
