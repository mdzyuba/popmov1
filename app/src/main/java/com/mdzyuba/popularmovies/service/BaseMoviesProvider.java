package com.mdzyuba.popularmovies.service;

import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

import androidx.annotation.NonNull;

public abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;
    private final MovieCollection movieCollection;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
        movieCollection = new MovieCollection();
    }

    @NonNull
    @Override
    public List<Movie> getMovies() {
        return movieCollection.getMovieList();
    }

    /**
     * Updates a movie collection with a next page of movies provided
     * by the service.
     * @throws IOException if an error occurs.
     */
    @Override
    public void loadMovies() throws IOException {
        int page = movieCollection.getFurthestPage() + 1;
        if (movieCollection.getTotalPages() > 0 && page >= movieCollection.getTotalPages()) {
            Log.d(TAG, "Reached max number of pages.");
            return;
        }
        URL getMoviesURL = getRequestUri(page);
        if (getMoviesURL == null) {
            throw new InvalidParameterException("The url should not be null");
        }
        String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
        MovieParser movieParser = new MovieParser();
        MovieCollection partialCollection = movieParser.parseMovieCollection(json);
        List<Movie> movies = partialCollection.getMovieList();
        movieCollection.getMovieList().addAll(movies);
        movieCollection.setFurthestPage(page);
        movieCollection.setTotalPages(partialCollection.getTotalPages());
    }

    @Override
    public boolean canLoadMoreMovies() {
        return movieCollection.getFurthestPage() < movieCollection.getTotalPages();
    }
}
