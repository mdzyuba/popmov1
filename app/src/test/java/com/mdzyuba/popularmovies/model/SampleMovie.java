package com.mdzyuba.popularmovies.model;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides data for the first Movie item in the src/test/resources/popular_movies_response.json
 */
public class SampleMovie {
    public static final String TITLE = "Alita: Battle Angel";
    public static final String POSTER_PATH = "/xRWht48C2V8XNfzvPehyClOvDni.jpg";
    public static final String OVERVIEW =
            "When Alita awakens with no memory of who she is in a future world she does not " +
            "recognize, she is taken in by Ido, a compassionate doctor who realizes that " +
            "somewhere in this abandoned cyborg shell is the heart and soul of a young woman " +
            "with an extraordinary past.";
    public static final Date RELEASE_DATE = new GregorianCalendar(2019, 0, 31).getTime();
    public static final String RELEASE_DATE_STRING = "2019-01-31";
    public static final Float VOTE_AVERAGE = 6.8f;
}
