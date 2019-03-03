package com.mdzyuba.popularmovies.view;

import android.util.Log;
import android.widget.ImageView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    public static void loadImage(Picasso picasso, Movie movie, ImageView imageView) {
        MovieApiClient movieApiClient = new MovieApiClient();
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
