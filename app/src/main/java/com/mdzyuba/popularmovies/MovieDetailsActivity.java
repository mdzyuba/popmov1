package com.mdzyuba.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.view.ImageUtil;
import com.mdzyuba.popularmovies.view.PicassoProvider;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

/**
 * Displays a Movie details.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    private static final String KEY_MOVIE = "MOVIE";
    private Movie movie;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (movie == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(KEY_MOVIE)) {
                movie = intent.getParcelableExtra(KEY_MOVIE);
            }
        }

        if (picasso == null) {
            picasso = PicassoProvider.getPicasso(this.getApplicationContext());
        }

        if (movie != null) {
            initView();
        }
    }

    @SuppressLint("DefaultLocale")
    private void initView() {
        initTextView(R.id.tv_title, movie.getTitle());
        initReleaseYear();
        if (movie.getVoteAverage() != null) {
            initTextView(R.id.tv_rating, String.format("%1.1f/10", movie.getVoteAverage()));
        }
        initPosterImage();
        initTextView(R.id.tv_overview, movie.getOverview());
    }

    private void initTextView(int resourceId, String text) {
        TextView overviewTextView = findViewById(resourceId);
        if (text != null) {
            overviewTextView.setText(text);
        } else {
            overviewTextView.setText(R.string.no_data);
        }
    }

    private void initPosterImage() {
        ImageView imageView = findViewById(R.id.iv_poster);
        if (movie.getPosterPath() != null) {
            ImageUtil.loadImage(picasso, movie, imageView);
        }
    }

    private void initReleaseYear() {
        TextView releaseYearTextView = findViewById(R.id.tv_release_year);
        Date releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            String text = Integer.toString(calendar.get(Calendar.YEAR));
            releaseYearTextView.setText(text);
        } else {
            releaseYearTextView.setText(R.string.no_data);
        }
    }

    public static Intent createIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(KEY_MOVIE, movie);
        return intent;
    }
}
