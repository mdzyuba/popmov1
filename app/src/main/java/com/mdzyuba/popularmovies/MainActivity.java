package com.mdzyuba.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieLoadListener;
import com.mdzyuba.popularmovies.service.MoviesProvider;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.service.PopularMoviesProvider;
import com.mdzyuba.popularmovies.service.TopRatedMoviesProvider;
import com.mdzyuba.popularmovies.view.InitPopularMoviesTask;
import com.mdzyuba.popularmovies.view.MovieAdapter;
import com.mdzyuba.popularmovies.view.MoviesSelection;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

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
    private ProgressBar progressBar;

    private TopRatedMoviesProvider topRatedMoviesProvider;
    private PopularMoviesProvider popularMoviesProvider;

    private final MovieAdapter.MovieClickListener movieClickListener = new MovieAdapter.MovieClickListener() {
        @Override
        public void onMovieClick(Movie movie) {
            Intent intent = MovieDetailsActivity.createIntent(MainActivity.this, movie);
            startActivity(intent);
        }
    };

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "scroll: " + newState + ", " + recyclerView.canScrollVertically(1));
            if (newState == SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                if (moviesProvider.canLoadMoreMovies()) {
                    Log.d(TAG, "Load more movies");
                }
            }
        }
    };

    private final MovieLoadListener movieLoadListener = new MovieLoadListener() {
        @Override
        public void onLoadStarted() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoaded(List<Movie> movies) {
            progressBar.setVisibility(View.INVISIBLE);
            movieAdapter.updateMovies(movies);
            movieListView.smoothScrollToPosition(0);
        }

        @Override
        public void onError(Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            showErrorDialog(e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIES_SELECTION)) {
            MoviesSelection moviesSelection =
                    (MoviesSelection) savedInstanceState.getSerializable(KEY_MOVIES_SELECTION);
            setMoviesSelection(moviesSelection);
        }

        progressBar = findViewById(R.id.progress_circular);

        networkDataProvider = new NetworkDataProvider();
        moviesProvider = getPopularMoviesProvider();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        movieListView = findViewById(R.id.list_view);
        movieListView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(this, movieClickListener);
        movieListView.setAdapter(movieAdapter);
        movieListView.addOnScrollListener(scrollListener);

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
                moviesProvider = getTopRatedMoviesProvider();
                break;
            case MOST_POPULAR:
                setTitle(R.string.most_popular_movies);
                moviesProvider = getPopularMoviesProvider();
                break;
            default:
                Log.e(TAG, "The selection is unknown: " + selection);
                moviesProvider = getTopRatedMoviesProvider();
        }
        if (!moviesProvider.isInitialized()) {
            InitPopularMoviesTask initPopularMoviesTask =
                    new InitPopularMoviesTask(moviesProvider, movieLoadListener);
            initPopularMoviesTask.execute();
        } else {
            try {
                movieAdapter.updateMovies(moviesProvider.getMovies());
                movieListView.smoothScrollToPosition(0);
            } catch (IOException e) {
                Log.e(TAG, "Updating movies failed: " + e.getMessage(), e);
                showErrorDialog(e);
            }
        }
    }

    private PopularMoviesProvider getPopularMoviesProvider() {
        if (popularMoviesProvider == null) {
            popularMoviesProvider = new PopularMoviesProvider(networkDataProvider);
        }
        return popularMoviesProvider;
    }

    private TopRatedMoviesProvider getTopRatedMoviesProvider() {
        if (topRatedMoviesProvider == null) {
            topRatedMoviesProvider = new TopRatedMoviesProvider(networkDataProvider);
        }
        return topRatedMoviesProvider;
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

    private void showErrorDialog(Exception exception) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_loading_movies);
        builder.setMessage("Error: " + exception.getMessage() + "\n" +
                           getString(R.string.try_later));
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
