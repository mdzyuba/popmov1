package com.mdzyuba.popularmovies.view;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater
                .inflate(R.layout.movie_view_item, parent, shouldAttachToParentImmediately);
        MovieViewHolder moviesProvider = new MovieViewHolder(view);

        return moviesProvider;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        if (movie != null) {
            holder.bind(movie);
        } else {
            Log.e(TAG, String.format("The movie in position %d is null", position));
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        void bind(@NonNull Movie movie) {
            MovieApiClient movieApiClient = new MovieApiClient();
            Picasso.Builder picassoBuilder = new Picasso.Builder(imageView.getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e(TAG, "Error loading an image: " + uri, exception);
                }
            });
            Picasso picasso = picassoBuilder.build();

            String posterPath = movie.getPosterPath();
            if (posterPath != null) {
                URL imageUrl = movieApiClient.getImageUri(posterPath);
                picasso.load(imageUrl.toString()).placeholder(R.drawable.image_placeholder).into(imageView);
            } else {
                Log.w(TAG, "The poster path is null");
                picasso.load(R.drawable.image_placeholder).into(imageView);
            }
        }
    }
}