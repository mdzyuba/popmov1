package com.mdzyuba.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MoviesProvider;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.service.PopularMoviesProvider;
import com.mdzyuba.popularmovies.service.TopRatedMoviesProvider;
import com.mdzyuba.popularmovies.view.MovieAdapter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of movie posters.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String KEY_MOVIES_SELECTION = "MOVIES_SELECTION";
    private NetworkDataProvider networkDataProvider;
    private MoviesProvider moviesProvider;
    private RecyclerView movieListView;
    private MovieAdapter movieAdapter;
    private MoviesSelection moviesSelection;

    private final MovieAdapter.MovieClickListener movieClickListener = new MovieAdapter.MovieClickListener() {
        @Override
        public void onMovieClick(Movie movie) {
            Intent intent = MovieDetailsActivity.createIntent(MainActivity.this, movie);
            startActivity(intent);
        }
    };

    enum MoviesSelection {
        MOST_POPULAR(0),
        TOP_RATED(1);

        private final int value;

        MoviesSelection(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        static MoviesSelection valueOf(int i) {
            switch (i) {
                case 0:
                    return MOST_POPULAR;
                case 1:
                    return TOP_RATED;
                    default:
                        return MOST_POPULAR;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIES_SELECTION)) {
            setMoviesSelection(
                    (MoviesSelection) savedInstanceState.getSerializable(KEY_MOVIES_SELECTION));
        }

        networkDataProvider = new NetworkDataProvider();
        moviesProvider = new PopularMoviesProvider(networkDataProvider);

        movieListView = findViewById(R.id.list_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        movieListView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(movieClickListener);
        movieListView.setAdapter(movieAdapter);

        reloadMovies(getMoviesSelection());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_MOVIES_SELECTION, moviesSelection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.most_popular_movies) {
            reloadMovies(MoviesSelection.MOST_POPULAR);
        } else if (item.getItemId() == R.id.top_movies) {
            // select top movies
            reloadMovies(MoviesSelection.TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadMovies(@NonNull MoviesSelection selection) {
        if (selection == moviesSelection && movieAdapter.getItemCount() > 0) {
            return;
        }
        setMoviesSelection(selection);
        switch (selection) {
            case TOP_RATED:
                setTitle(R.string.top_movies);
                moviesProvider = new TopRatedMoviesProvider(networkDataProvider);
                break;
            case MOST_POPULAR:
                setTitle(R.string.most_popular_movies);
                moviesProvider = new PopularMoviesProvider(networkDataProvider);
                break;
            default:
                Log.e(TAG, "The selection is unknown: " + selection);
                moviesProvider = new TopRatedMoviesProvider(networkDataProvider);
        }
        InitPopularMoviesTask initPopularMoviesTask = new InitPopularMoviesTask(this);
        initPopularMoviesTask.execute();
    }

    @NonNull
    private MoviesProvider getMoviesProvider() {
        return moviesProvider;
    }

    private MoviesSelection getMoviesSelection() {
        if (moviesSelection == null) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            int savedSelection = sharedPref
                    .getInt(KEY_MOVIES_SELECTION, MoviesSelection.MOST_POPULAR.getValue());
            moviesSelection = MoviesSelection.valueOf(savedSelection);
        }
        return moviesSelection;
    }

    private void setMoviesSelection(MoviesSelection moviesSelection) {
        if (moviesSelection != this.moviesSelection) {
            this.moviesSelection = moviesSelection;
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(KEY_MOVIES_SELECTION, moviesSelection.getValue());
            editor.apply();
        }
    }

    private static class InitPopularMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private final WeakReference<MainActivity> activityWeakReference;

        private Exception exception;

        InitPopularMoviesTask(final MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            List<Movie> movies = new ArrayList<>();
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity != null) {
                MoviesProvider moviesProvider = mainActivity.getMoviesProvider();
                try {
                    movies = moviesProvider.getMovies();
                } catch (IOException e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    exception = e;
                }
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            if (this.exception == null) {
                mainActivity.movieAdapter.updateMovies(movies);
                mainActivity.movieListView.smoothScrollToPosition(0);
            } else {
                showErrorDialog(mainActivity);
            }
        }

        private void showErrorDialog(final MainActivity mainActivity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle(R.string.error_loading_movies);
            builder.setMessage("Error: " + exception.getMessage() + "\n" +
                               mainActivity.getString(R.string.try_later));
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
