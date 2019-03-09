package com.mdzyuba.popularmovies.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.mdzyuba.popularmovies.BuildConfig;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class MovieParser {

    private static final String TAG = MovieParser.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String PAGE = "page";
    private static final String TOTAL_PAGES = "total_pages";

    @NonNull
    public MovieCollection parseMovieCollection(@Nullable String json) {
        MovieCollection movieCollection = new MovieCollection();
        if (json == null) {
            return movieCollection;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (BuildConfig.DEBUG) {
                Iterator<String> keyIterator = jsonObject.keys();
                while (keyIterator.hasNext()) {
                    Log.d(TAG, "key: " + keyIterator.next());
                }
            }

            int page = jsonObject.optInt(PAGE);
            int totalPages = jsonObject.optInt(TOTAL_PAGES);
            JSONArray results = jsonObject.optJSONArray(RESULTS);
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                Movie movie = parseMovie(results.optJSONObject(i));
                if (movie != null) {
                    movies.add(movie);
                }
            }
            movieCollection.setFurthestPage(page);
            movieCollection.setTotalPages(totalPages);
            movieCollection.setMovieList(movies);
        } catch (JSONException e) {
            Log.d(TAG, "JSON parsing error: " + e.getMessage(), e);
        }
        return movieCollection;
    }

//    @NonNull
//    public List<Movie> parseMovies(@Nullable String json) {
//        List<Movie> movies = new ArrayList<>();
//        if (json == null) {
//            return movies;
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            if (BuildConfig.DEBUG) {
//                Iterator<String> keyIterator = jsonObject.keys();
//                while (keyIterator.hasNext()) {
//                    Log.d(TAG, "key: " + keyIterator.next());
//                }
//            }
//            JSONArray results = jsonObject.optJSONArray(RESULTS);
//            for (int i = 0; i < results.length(); i++) {
//                Movie movie = parseMovie(results.optJSONObject(i));
//                if (movie != null) {
//                    movies.add(movie);
//                }
//            }
//        } catch (JSONException e) {
//            Log.d(TAG, "JSON parsing error: " + e.getMessage(), e);
//        }
//        return movies;
//    }

    @Nullable
    private Movie parseMovie(@Nullable JSONObject jsonMovie) {
        if (jsonMovie == null) {
            return null;
        }

        Movie.Builder movieBuilder = new Movie.Builder();
        String title = jsonMovie.optString(TITLE);
        String posterPath = jsonMovie.optString(POSTER_PATH);
        String overview = jsonMovie.optString(OVERVIEW);
        String releaseDate = jsonMovie.optString(RELEASE_DATE);
        String voteAverage = jsonMovie.optString(VOTE_AVERAGE);

        movieBuilder
                .withTitle(title)
                .withPosterPath(posterPath)
                .withOverview(overview);

        if (releaseDate != null) {
            movieBuilder.withReleaseDate(toDate(releaseDate));
        }

        if (voteAverage != null) {
            movieBuilder.withVoteAverage(Float.parseFloat(voteAverage));
        }

        return movieBuilder.build();
    }

    Date toDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse thee release date: " + date);
        }
        return null;
    }
}
