package com.mdzyuba.popularmovies.view;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    public static void loadImage(Movie movie, ImageView imageView) {
        MovieApiClient movieApiClient = new MovieApiClient();
        Picasso.Builder picassoBuilder = new Picasso.Builder(imageView.getContext()).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.e(TAG, "Error loading an image: " + uri, exception);
            }
        });
        Picasso picasso = picassoBuilder.build();

        String posterPath = movie.getPosterPath();
        URL imageUrl = null;
        if (posterPath != null) {
            imageUrl = movieApiClient.getImageUri(posterPath);
        }
        if (imageUrl != null) {
            picasso.load(imageUrl.toString()).placeholder(R.drawable.image_placeholder)
                   .into(imageView);
        } else {
            Log.w(TAG, "The poster path is null");
            picasso.load(R.drawable.image_placeholder).into(imageView);
        }
    }
}
