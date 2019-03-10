package com.mdzyuba.popularmovies.service;

import java.net.URL;

public class PopularMoviesProvider extends BaseMoviesProvider {

    public PopularMoviesProvider(NetworkDataProvider networkDataProvider) {
        super(networkDataProvider);
    }

    @Override
    public URL getRequestUri(int page) {
        URL popularMoviesUrl = new MovieApiClient().buildGetPopularMoviesUrl(page);
        return popularMoviesUrl;
    }
}
