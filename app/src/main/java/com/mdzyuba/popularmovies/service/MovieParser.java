package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.mdzyuba.popularmovies.BuildConfig;
import com.mdzyuba.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieParser {

    private static final String TAG = MovieParser.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";

    @VisibleForTesting
    @NonNull
    List<Movie> parsePopularMovies(@Nullable String json) {
        List<Movie> movies = new ArrayList<>();
        if (json == null) {
            return movies;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (BuildConfig.DEBUG) {
                Iterator<String> keyIterator = jsonObject.keys();
                while (keyIterator.hasNext()) {
                    Log.d(TAG, "key: " + keyIterator.next());
                }
            }
            JSONArray results = jsonObject.optJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                Movie movie = parseMovie(results.optJSONObject(i));
                if (movie != null) {
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSON parsing error: " + e.getMessage(), e);
        }
        return movies;
    }

    @VisibleForTesting
    @Nullable
    Movie parseMovie(@Nullable JSONObject jsonMovie) {
        if (jsonMovie == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setPosterPath(jsonMovie.optString(POSTER_PATH));
        return movie;
    }
}
