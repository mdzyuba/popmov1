package com.mdzyuba.popularmovies.service;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;

/**
 * Provides a singleton instance of the OkHttpClient.
 *
 * According to https://square.github.io/okhttp/3.x/okhttp/,
 * "OkHttp performs best when you create a single OkHttpClient instance and reuse it for all of
 * your HTTP calls."
 */
public class HttpClientProvider {
    private static volatile OkHttpClient client;

    private HttpClientProvider() {
    }

    @NonNull
    public static synchronized OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }
}
