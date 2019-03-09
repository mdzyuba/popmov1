package com.mdzyuba.popularmovies.service;

import java.net.URL;

import androidx.annotation.NonNull;

public class TopRatedMoviesProvider extends BaseMoviesProvider {

    public TopRatedMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        super(networkDataProvider);
    }

    @Override
    public URL getRequestUri(int page) {
        URL topRatedMoviesURL = new MovieApiClient().buildGetTopRatedMoviesUrl();
        return topRatedMoviesURL;
    }
}
