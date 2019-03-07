package com.mdzyuba.popularmovies.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mdzyuba.popularmovies.service.HttpClientProvider;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Provides a singleton instance of the picasso library.
 *
 * We have to use a context to create it, which could cause the leaks. The code will use the
 * application context which could hopefully minimize the issue.
 *
 * Creating a Picasso instance for each image leads to a crash with a following error:
 * OkHttp  : DiskLruCache /data/user/0/com.mdzyuba.popularmovies/cache/picasso-cache is corrupt:
 * /data/user/0/com.mdzyuba.popularmovies/cache/picasso-cache/journal (Too many open files), removing
 *
 * Applying Jake's suggestion to keep it as a singleton posted here:
 * https://github.com/square/picasso/issues/1100
 *
 * This class is inspired by this example:
 * https://github.com/square/picasso/blob/master/picasso-sample/src/main/java/com/example/picasso/provider/PicassoProvider.java
 */
public class PicassoProvider {
    private static final String TAG = PicassoProvider.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static volatile Picasso instance;

    private PicassoProvider() {
    }

    @NonNull
    public static Picasso getPicasso(@NonNull Context context) {
        if (instance == null) {
            synchronized (PicassoProvider.class) {
                Picasso.Builder picassoBuilder =
                        new Picasso.Builder(context.getApplicationContext())
                                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e(TAG, "Error loading an image: " + uri, exception);
                    }
                });
                OkHttpClient client = HttpClientProvider.getClient();
                picassoBuilder.downloader(new OkHttp3Downloader(client));
                instance = picassoBuilder.build();
            }
        }
        return instance;
    }
}
