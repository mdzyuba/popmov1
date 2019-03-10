package com.mdzyuba.popularmovies.view;

import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieLoadListener;
import com.mdzyuba.popularmovies.service.MoviesProvider;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.service.PopularMoviesProvider;
import com.mdzyuba.popularmovies.service.TopRatedMoviesProvider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MoviesGridViewModel extends ViewModel {
    private static final String TAG = MoviesGridViewModel.class.getSimpleName();

    private final NetworkDataProvider networkDataProvider;
    private MoviesProvider moviesProvider;
    private final TopRatedMoviesProvider topRatedMoviesProvider;
    private final PopularMoviesProvider popularMoviesProvider;

    private final MutableLiveData<MoviesSelection> moviesSelection;
    private final MutableLiveData<List<Movie>> movieList;
    private final MutableLiveData<Boolean> moviesAreLoading;
    private final MutableLiveData<Exception> dataLoadException;

    public MoviesGridViewModel() {
        movieList = new MutableLiveData<>();
        moviesAreLoading = new MutableLiveData<>();
        moviesSelection = new MutableLiveData<>();
        dataLoadException = new MutableLiveData<>();
        networkDataProvider = new NetworkDataProvider();
        popularMoviesProvider = new PopularMoviesProvider(networkDataProvider);
        topRatedMoviesProvider = new TopRatedMoviesProvider(networkDataProvider);
        moviesSelection.setValue(MoviesSelection.MOST_POPULAR);
        moviesProvider = popularMoviesProvider;
    }

    public MutableLiveData<List<Movie>> getMovieList() {
        return movieList;
    }

    public MutableLiveData<Boolean> areMoviesLoading() {
        return moviesAreLoading;
    }

    public MutableLiveData<MoviesSelection> getMoviesSelection() {
        return moviesSelection;
    }

    public MutableLiveData<Exception> getDataLoadException() {
        return dataLoadException;
    }

    public boolean canLoadMoreMovies() {
        return moviesProvider.canLoadMoreMovies();
    }

    public void setMoviesSelection(MoviesSelection selection) {
        if (this.moviesSelection.getValue() == selection) {
            return;
        }
        this.moviesSelection.setValue(selection);
        updateMoviesProvider(selection);
    }

    private void updateMoviesProvider(@NonNull MoviesSelection selection) {
        switch (selection) {
            case TOP_RATED:
                moviesProvider = topRatedMoviesProvider;
                break;
            case MOST_POPULAR:
                moviesProvider = popularMoviesProvider;
                break;
            default:
                Log.e(TAG, "The selection is unknown: " + selection);
                moviesProvider = topRatedMoviesProvider;
        }
        loadMovies();
    }

    public void loadMovies() {
        InitPopularMoviesTask initPopularMoviesTask =
                new InitPopularMoviesTask(moviesProvider, new MovieLoadListener() {
                    @Override
                    public void onLoadStarted() {
                        moviesAreLoading.setValue(true);
                    }

                    @Override
                    public void onLoaded(List<Movie> movies) {
                        moviesAreLoading.setValue(false);
                        movieList.setValue(movies);
                    }

                    @Override
                    public void onError(Exception e) {
                        moviesAreLoading.setValue(false);
                        dataLoadException.setValue(e);
                    }
                });
        initPopularMoviesTask.execute();
    }

    public void resetException() {
        if (dataLoadException.getValue() != null) {
            dataLoadException.setValue(null);
        }
    }
}
