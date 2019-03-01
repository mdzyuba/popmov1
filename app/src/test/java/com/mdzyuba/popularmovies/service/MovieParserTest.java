package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class MovieParserTest {

    @Test
    public void parsePopularMovies_returnsValidPosterPathUrls() throws IOException {
        TestDataUtils resourceUtils = new TestDataUtils();
        String json = resourceUtils.readPopularMoviesJsonResponse();
        assertNotNull("The json is null", json);

        MovieParser movieParser = new MovieParser();
        List<Movie> movies = movieParser.parseMovies(json);

        resourceUtils.assertMoviesParsed(movies);
    }

}