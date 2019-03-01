package com.mdzyuba.popularmovies.model;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MovieTest {

    @Test
    public void writeToParcel_title_movieCreated() {
        Movie.Builder movieBuilder = new Movie.Builder();
        movieBuilder.withTitle("Title");
        Movie movie = movieBuilder.build();
        assertMovieCreated(movie);
    }

    @Test
    public void writeToParcel_allFields_movieCreated() {
        Movie.Builder movieBuilder = new Movie.Builder()
                .withTitle(SampleMovie.TITLE)
                .withPosterPath(SampleMovie.POSTER_PATH)
                .withOverview(SampleMovie.OVERVIEW)
                .withReleaseDate(SampleMovie.RELEASE_DATE)
                .withVoteAverage(SampleMovie.VOTE_AVERAGE);
        Movie movie = movieBuilder.build();
        assertMovieCreated(movie);
    }

    private void assertMovieCreated(Movie movie) {
        Parcel parcel = Parcel.obtain();

        movie.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        Movie cloneMovie = Movie.CREATOR.createFromParcel(parcel);

        assertEquals(movie, cloneMovie);
    }
}