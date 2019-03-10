package com.mdzyuba.popularmovies.service;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.mdzyuba.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;


public class MovieApiClient {

    private static final String TAG = MovieApiClient.class.getSimpleName();

    private static final String THEMOVIEDB_ORG = "https://api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String MOVIE = "movie";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String API_KEY = "api_key";
    private static final String LANGUAGE = "language";
    private static final String EN_US = "en-US";
    private static final String PAGE = "page";

    private static final String IMAGE_BASE = "http://image.tmdb.org/t/p";
    // Possible options are: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    // For most phones we recommend using “w185”.
    private static final String IMAGE_SIZE = "w185";
    private static final String IMAGE_PATH_SEPARATOR = "/";

    @NonNull
    public URL buildGetPopularMoviesUrl(int page) {
        return buildGerMovieUrl(POPULAR, page);
    }

    @NonNull
    public URL buildGetTopRatedMoviesUrl(int page) {
        return buildGerMovieUrl(TOP_RATED, page);
    }

    @NonNull
    private URL buildGerMovieUrl(String category, int page) {
        Uri uri = Uri.parse(THEMOVIEDB_ORG).buildUpon().appendPath(API_VERSION).appendPath(MOVIE)
                     .appendPath(category).appendQueryParameter(API_KEY, BuildConfig.MOVIEDB_KEY)
                     .appendQueryParameter(LANGUAGE, EN_US)
                     .appendQueryParameter(PAGE, String.valueOf(page)).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            // Letting the app to crash for a coding error.
            throw new RuntimeException("Failed creating a url: " + e.getMessage(), e);
        }
    }

    @Nullable
    public URL getImageUri(@NonNull String imagePath) {
        Uri.Builder builder = Uri.parse(IMAGE_BASE).buildUpon().appendPath(IMAGE_SIZE);
        for (String token : imagePath.split(IMAGE_PATH_SEPARATOR)) {
            builder.appendPath(token);
        }
        try {
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

}
