package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MovieApiClientTest {

    @Test
    public void buildGetPopularMoviesUrl() throws Exception {
        MovieApiClient movieApiClient = new MovieApiClient();
        URL popMoviesUrl = movieApiClient.buildGetPopularMoviesUrl();
        String expectedUriString =
                "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOVIEDB_KEY +
                "&language=en-US&page=1";
        assertEquals(expectedUriString, popMoviesUrl.toString());
    }
}