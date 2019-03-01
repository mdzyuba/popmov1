package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

public class TopRatedMoviesProvider extends BaseMoviesProvider {

    public TopRatedMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        super(networkDataProvider);
    }

    @NonNull
    @Override
    public List<Movie> getMovies() {
        URL topRatedMoviesURL = new MovieApiClient().buildGetTopRatedMoviesUrl();
        return getMovieList(topRatedMoviesURL);
    }
}