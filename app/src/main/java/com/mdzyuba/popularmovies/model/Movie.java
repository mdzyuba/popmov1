package com.mdzyuba.popularmovies.model;

import android.support.annotation.Nullable;

import java.util.Date;

public class Movie {
    private String posterPath;

    Boolean adult;

    String overview;

    Date releaseDate;

    Integer[] genreIDs;

    Integer id;

    String originalTitle;

    String originalLanguage;

    String title;

    String backdropPath;

    Integer popularity;

    Integer voteCount;

    Boolean video;

    Integer voteAverage;

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@Nullable String posterPath) {
        this.posterPath = posterPath;
    }

}
