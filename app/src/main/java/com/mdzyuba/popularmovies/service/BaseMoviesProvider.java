package com.mdzyuba.popularmovies.service;

import androidx.annotation.NonNull;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

public abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;
    private boolean initialized;
    private List<Movie> movieList;
    private MovieCollection movieCollection;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @NonNull
    @Override
    public List<Movie> getMovies() throws IOException {
        URL popularMoviesUrl = getRequestUri(1);
        return getMovieList(popularMoviesUrl);
    }

    @NonNull
    List<Movie> getMovieList(URL getMoviesURL) throws IOException {
        if (getMoviesURL == null) {
            throw new InvalidParameterException("The url should not be null");
        }
        if (movieList == null || movieList.size() == 0) {
            String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
            MovieParser movieParser = new MovieParser();
            movieCollection = movieParser.parseMovieCollection(json);
            movieList = movieCollection.getMovieList();
            if (movieList.size() > 0) {
                initialized = true;
            }
        }
        return movieList;
    }

    @Override
    public boolean canLoadMoreMovies() {
        return movieCollection == null ||
               movieCollection.getFurthestPage() < movieCollection.getTotalPages();
    }
}
