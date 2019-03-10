package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MovieApiClientTest {

    private MovieApiClient movieApiClient;

    @Before
    public void setUp() {
        movieApiClient = new MovieApiClient();
    }

    @Test
    public void buildGetPopularMoviesUrl() {
        URL popMoviesUrl = movieApiClient.buildGetPopularMoviesUrl(1);
        String expectedUriString =
                "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOVIEDB_KEY +
                "&language=en-US&page=1";
        assertNotNull(popMoviesUrl);
        assertEquals(expectedUriString, popMoviesUrl.toString());
    }

    @Test
    public void getImageUri() {
        URL imageUrl = movieApiClient.getImageUri("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        String expectedUriString = "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        assertNotNull(imageUrl);
        assertEquals(expectedUriString, imageUrl.toString());
    }
}