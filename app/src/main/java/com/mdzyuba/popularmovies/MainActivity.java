package com.mdzyuba.popularmovies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.view.MovieAdapter;
import com.mdzyuba.popularmovies.view.MoviesGridViewModel;
import com.mdzyuba.popularmovies.view.MoviesSelection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Displays a list of movie posters.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView movieListView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    private MoviesGridViewModel viewModel;

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
                if (viewModel.canLoadMoreMovies()) {
                    Log.d(TAG, "Load more movies");
                    viewModel.loadMovies();
                }
            }
        }
    };

    private final Observer<List<Movie>> movieListObserver = new Observer<List<Movie>>() {
        @Override
        public void onChanged(List<Movie> movies) {
            Log.d(TAG, "Movie List changed. Updating the grid.");
            movieAdapter.updateMovies(movies);
        }
    };

    private final Observer<Boolean> moviesLoadingObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean moviesAreLoading) {
            if (moviesAreLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    private final Observer<MoviesSelection> moviesSelectionObserver = new Observer<MoviesSelection>() {
        @Override
        public void onChanged(MoviesSelection moviesSelection) {
            switch (moviesSelection) {
                case TOP_RATED:
                    setTitle(R.string.top_movies);
                    break;
                case MOST_POPULAR:
                    setTitle(R.string.most_popular_movies);
                    break;
                default:
                    Log.e(TAG, "The selection is unknown: " + moviesSelection);
            }
        }
    };

    private final Observer<Exception> dataLoadingExceptionObserver = new Observer<Exception>() {
        @Override
        public void onChanged(Exception e) {
            showErrorDialog(e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MoviesGridViewModel.class);
        progressBar = findViewById(R.id.progress_circular);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        movieListView = findViewById(R.id.list_view);
        movieListView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(this, movieClickListener);
        movieListView.setAdapter(movieAdapter);
        movieListView.addOnScrollListener(scrollListener);

        viewModel.getMovieList().observe(this, movieListObserver);
        viewModel.areMoviesLoading().observe(this, moviesLoadingObserver);
        viewModel.getMoviesSelection().observe(this, moviesSelectionObserver);
        viewModel.getDataLoadException().observe(this, dataLoadingExceptionObserver);
        viewModel.loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.most_popular_movies) {
            viewModel.setMoviesSelection(MoviesSelection.MOST_POPULAR);
        } else if (item.getItemId() == R.id.top_movies) {
            viewModel.setMoviesSelection(MoviesSelection.TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
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
