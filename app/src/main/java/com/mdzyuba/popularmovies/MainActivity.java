package com.mdzyuba.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MoviesProvider;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.view.MovieAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView movieListView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();

        movieListView = findViewById(R.id.list_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        movieListView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(movieList);
        movieListView.setAdapter(movieAdapter);
        InitPopularMoviesTask initPopularMoviesTask = new InitPopularMoviesTask(this);
        initPopularMoviesTask.execute();
    }

    private void updateMovies(List<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        movieAdapter.notifyDataSetChanged();
    }

    private static class InitPopularMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private final WeakReference<MainActivity> activityWeakReference;

        InitPopularMoviesTask(final MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            NetworkDataProvider networkDataProvider = new NetworkDataProvider();
            MoviesProvider moviesProvider = new MoviesProvider(networkDataProvider);
            List<Movie> movies = moviesProvider.getPopularMovies();
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            activityWeakReference.get().updateMovies(movies);
        }
    }
}
