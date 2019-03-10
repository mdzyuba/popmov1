package com.mdzyuba.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Movie implements Parcelable {

    private static final String TAG = Movie.class.getSimpleName();
    private static final int UNDEFINED_VALUE = -1;

    private final String title;
    private final String posterPath;
    private final Boolean adult;
    private final String overview;
    private final Date releaseDate;
    private final Integer[] genreIDs;
    private final Integer id;
    private final String originalTitle;
    private final String originalLanguage;
    private final String backdropPath;
    private final Integer popularity;
    private final Integer voteCount;
    private final Boolean video;
    private final Float voteAverage;

    private Movie(String title, String posterPath, Boolean adult, String overview, Date releaseDate,
                  Integer[] genreIDs, Integer id, String originalTitle, String originalLanguage,
                  String backdropPath, Integer popularity, Integer voteCount, Boolean video,
                  Float voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIDs = genreIDs;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public Boolean getAdult() {
        return adult;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    @Nullable
    public Date getReleaseDate() {
        return releaseDate;
    }

    @Nullable
    public Integer[] getGenreIDs() {
        return genreIDs;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @Nullable
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Nullable
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    @Nullable
    public Integer getPopularity() {
        return popularity;
    }

    @Nullable
    public Integer getVoteCount() {
        return voteCount;
    }

    @Nullable
    public Boolean getVideo() {
        return video;
    }

    @Nullable
    public Float getVoteAverage() {
        return voteAverage;
    }

    public static class Builder {
        private String title;
        private String posterPath;
        private Boolean adult;
        private String overview;
        private Date releaseDate;
        private Integer[] genreIDs;
        private Integer id;
        private String originalTitle;
        private String originalLanguage;
        private String backdropPath;
        private Integer popularity;
        private Integer voteCount;
        private Boolean video;
        private Float voteAverage;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public Builder withOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder withReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder withVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Movie build() {
            return new Movie(title, posterPath, adult, overview, releaseDate, genreIDs, id,
                             originalTitle, originalLanguage, backdropPath, popularity,
                             voteCount, video, voteAverage);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        if (releaseDate != null) {
            dest.writeLong(releaseDate.getTime());
        } else {
            dest.writeLong(UNDEFINED_VALUE);
        }
        if (voteAverage != null) {
            dest.writeFloat(voteAverage);
        } else {
            dest.writeFloat(UNDEFINED_VALUE);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            String title = source.readString();
            String posterPath = source.readString();
            String overview = source.readString();
            long releaseDateMs = source.readLong();
            float voteAverage = source.readFloat();

            Movie.Builder movieBuilder = new Movie.Builder();
            movieBuilder.withTitle(title);
            movieBuilder.withPosterPath(posterPath);
            movieBuilder.withOverview(overview);
            if (releaseDateMs != UNDEFINED_VALUE) {
                Date date = new Date();
                date.setTime(releaseDateMs);
                movieBuilder.withReleaseDate(date);
            }
            if (Float.compare(voteAverage, UNDEFINED_VALUE) != 0) {
                movieBuilder.withVoteAverage(voteAverage);
            }
            return movieBuilder.build();
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" + "title='" + title + '\'' + ", posterPath='" + posterPath + '\'' +
               ", adult=" + adult + ", overview='" + overview + '\'' + ", releaseDate=" +
               releaseDate + ", genreIDs=" + Arrays.toString(genreIDs) + ", id=" + id +
               ", originalTitle='" + originalTitle + '\'' + ", originalLanguage='" +
               originalLanguage + '\'' + ", backdropPath='" + backdropPath + '\'' +
               ", popularity=" + popularity + ", voteCount=" + voteCount + ", video=" + video +
               ", voteAverage=" + voteAverage + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title) && Objects.equals(posterPath, movie.posterPath) &&
               Objects.equals(adult, movie.adult) && Objects.equals(overview, movie.overview) &&
               Objects.equals(releaseDate, movie.releaseDate) &&
               Arrays.equals(genreIDs, movie.genreIDs) && Objects.equals(id, movie.id) &&
               Objects.equals(originalTitle, movie.originalTitle) &&
               Objects.equals(originalLanguage, movie.originalLanguage) &&
               Objects.equals(backdropPath, movie.backdropPath) &&
               Objects.equals(popularity, movie.popularity) &&
               Objects.equals(voteCount, movie.voteCount) && Objects.equals(video, movie.video) &&
               Objects.equals(voteAverage, movie.voteAverage);
    }

    @Override
    public int hashCode() {
        int result =
                Objects.hash(title, posterPath, adult, overview, releaseDate, id, originalTitle,
                             originalLanguage, backdropPath, popularity, voteCount, video,
                             voteAverage);
        result = 31 * result + Arrays.hashCode(genreIDs);
        return result;
    }
}
