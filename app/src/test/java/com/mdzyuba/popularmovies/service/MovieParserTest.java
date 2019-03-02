package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.SampleMovie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class MovieParserTest {

    private MovieParser movieParser;

    @Before
    public void setUp() {
        movieParser = new MovieParser();
    }

    @Test
    public void parsePopularMovies_returnsValidPosterPathUrls() throws IOException {
        TestDataUtils resourceUtils = new TestDataUtils();
        String json = resourceUtils.readPopularMoviesJsonResponse();
        assertNotNull("The json is null", json);

        List<Movie> movies = movieParser.parseMovies(json);

        resourceUtils.assertMoviesParsed(movies);

        Movie movie = movies.get(0);
        assertEquals(SampleMovie.TITLE, movie.getTitle());
        assertEquals(SampleMovie.POSTER_PATH, movie.getPosterPath());
        assertEquals(SampleMovie.OVERVIEW, movie.getOverview());
        assertEquals(SampleMovie.RELEASE_DATE, movie.getReleaseDate());
        assertEquals(SampleMovie.VOTE_AVERAGE, movie.getVoteAverage());
    }

    @Test
    public void toDate_parsesDate() {
        assertEquals(SampleMovie.RELEASE_DATE, movieParser.toDate(SampleMovie.RELEASE_DATE_STRING));
    }

}