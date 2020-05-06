package com.kalamin.moviedatabase.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class MovieDetailsActivity extends BaseActivity {
    private TextView txtRuntime;
    private TextView txtTitle;
    private TextView txtOverview;
    private TextView txtAverageVote;
    private TextView txtPopularity;
    private TextView txtReleaseDate;
    private TextView movieDetailsNotFound;
    private ImageView imageView;
    private Button btnAddToFavorite;
    private ProgressBar progressBar;

    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setToolbarWithBackButton();

        internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(MovieDetailsActivity.this, NoInternetActivity.class));
            }
        });

        txtTitle = findViewById(R.id.txtTitle);
        txtRuntime = findViewById(R.id.txtRuntime);
        txtOverview = findViewById(R.id.txtOverview);
        txtAverageVote = findViewById(R.id.txtAverageVote);
        txtPopularity = findViewById(R.id.txtPopularity);
        txtReleaseDate = findViewById(R.id.txtReleaseDate);
        imageView = findViewById(R.id.poster);
        btnAddToFavorite = findViewById(R.id.btnAddToFavorite);
        progressBar = findViewById(R.id.progressBar);
        movieDetailsNotFound = findViewById(R.id.movie_details_not_found);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra(Extra.MOVIE_ID);
        if (movieId == null)
            movieId = intent.getStringExtra(Extra.SEARCH_ITEM_ID);

        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        movieDetailsViewModel.askForMovie(movieId);
        movieDetailsViewModel.getMovieObservable().observeForever(movieDetailsObserver);
        movieDetailsViewModel.isMovieFavorite();

        if (movieDetailsViewModel.isUserSignedIn()) {
            btnAddToFavorite.setVisibility(View.VISIBLE);
            movieDetailsViewModel.getSavedBtnStringObservable().observeForever(isFavoriteObserver);
        }
    }

    @SuppressLint("SetTextI18n")
    private Observer<Boolean> isFavoriteObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@NotNull Boolean aBoolean) {
            if (aBoolean) {
                btnAddToFavorite.setText("Remove From Favorite");
                btnAddToFavorite.setOnClickListener(v -> {
                    btnAddToFavorite.setText("Removing");
                    movieDetailsViewModel.removeFavorite();
                });
            } else {
                btnAddToFavorite.setText("Add To Favorite");
                btnAddToFavorite.setOnClickListener(v -> {
                    btnAddToFavorite.setText("Saving");
                    movieDetailsViewModel.addToFavorite();
                });
            }
        }
    };

    private Observer<MovieDetails> movieDetailsObserver = new Observer<MovieDetails>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(MovieDetails md) {
            if (md != null) {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.movie_details_layout).setVisibility(View.VISIBLE);

                txtTitle.setText(md.getTitle());
                txtRuntime.setText(md.getRuntime() + " min");
                txtOverview.setText(md.getOverview());
                txtAverageVote.setText(String.valueOf(md.getAverageVote()));
                txtPopularity.setText(String.valueOf(md.getPopularity()));
                txtReleaseDate.setText(md.getReleaseDate());
                Picasso.get().load(md.getPosterPath()).fit().centerCrop().into(imageView);
                movieDetailsViewModel.getMovieObservable().removeObserver(movieDetailsObserver);
            } else {
                progressBar.setVisibility(View.GONE);
                movieDetailsNotFound.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        movieDetailsViewModel.getSavedBtnStringObservable().removeObserver(isFavoriteObserver);
        movieDetailsViewModel.stopObserver();
        super.onDestroy();
    }
}
