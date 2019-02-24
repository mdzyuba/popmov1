package com.mdzyuba.popularmovies.service;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
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
    private static final String API_KEY = "api_key";
    private static final String LANGUAGE = "language";
    private static final String EN_US = "en-US";
    private static final String PAGE = "page";

    @VisibleForTesting
    @Nullable
    URL buildGetPopularMoviesUrl() {
        Uri bultUri = Uri.parse(THEMOVIEDB_ORG)
                         .buildUpon()
                         .appendPath(API_VERSION)
                         .appendPath(MOVIE)
                         .appendPath(POPULAR)
                         .appendQueryParameter(API_KEY, BuildConfig.MOVIEDB_KEY)
                         .appendQueryParameter(LANGUAGE, EN_US)
                         .appendQueryParameter(PAGE, String.valueOf(1))
                         .build();
        URL url = null;
        try {
            url = new URL(bultUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return url;
    }

}
