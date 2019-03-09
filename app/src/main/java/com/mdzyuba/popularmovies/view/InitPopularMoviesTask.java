package com.mdzyuba.popularmovies.view;

import android.os.AsyncTask;
import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieLoadListener;
import com.mdzyuba.popularmovies.service.MoviesProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitPopularMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
    private static final String TAG = InitPopularMoviesTask.class.getSimpleName();
    private final MovieLoadListener movieLoadListener;
    private final MoviesProvider moviesProvider;
    private Exception exception;

    public InitPopularMoviesTask(MoviesProvider moviesProvider, MovieLoadListener movieLoadListener) {
        this.movieLoadListener = movieLoadListener;
        this.moviesProvider = moviesProvider;
    }

    @Override
    protected void onPreExecute() {
        movieLoadListener.onLoadStarted();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = moviesProvider.getMovies();
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            exception = e;
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (exception != null) {
            movieLoadListener.onError(exception);
        } else {
            movieLoadListener.onLoaded(movies);
        }
    }
}
