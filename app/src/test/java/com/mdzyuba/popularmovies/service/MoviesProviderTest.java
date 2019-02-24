package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MoviesProviderTest {

    private TestDataUtils resourceUtils;
    private MoviesProvider moviesProvider;

    @Mock
    private NetworkDataProvider networkDataProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        resourceUtils = new TestDataUtils();
        moviesProvider = new MoviesProvider(networkDataProvider);
        String json = new TestDataUtils().readPopularMoviesJsonResponse();
        when(networkDataProvider.getResponseFromHttpUrl(Mockito.any(URL.class))).thenReturn(json);
    }

    @Test
    public void getPopularMovies() {
        List<Movie> movies = moviesProvider.getPopularMovies();
        resourceUtils.assertMoviesParsed(movies);
    }
}