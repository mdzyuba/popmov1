package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import java.util.List;

public interface MovieLoadListener {

    void onLoadStarted();

    void onLoaded(List<Movie> movies);

    void onError(Exception e);
}
