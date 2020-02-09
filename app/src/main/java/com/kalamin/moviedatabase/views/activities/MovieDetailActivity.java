package com.kalamin.moviedatabase.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.viewmodels.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import static com.kalamin.moviedatabase.utils.Extra.MOVIE_ID;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView txtRuntime;
    private TextView txtTitle;
    private TextView txtOverview;
    private TextView txtAverageVote;
    private TextView txtPopularity;
    private TextView txtReleaseDate;
    private ImageView imageView;
    private Button btnAddToFavorite;
    private ProgressBar progressBar;

    private InternetConnectionReceiver internetConnectionReceiver;

    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            internetConnectionReceiver = new InternetConnectionReceiver();
            this.registerReceiver(internetConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            internetConnectionReceiver.setInternetConnectionListener(isConnected -> {
                if (!isConnected) {
                    startActivity(new Intent(MovieDetailActivity.this, NoInternetActivity.class));
                }
            });
        }

        txtTitle = findViewById(R.id.txtTitle);
        txtRuntime = findViewById(R.id.txtRuntime);
        txtOverview = findViewById(R.id.txtOverview);
        txtAverageVote = findViewById(R.id.txtAverageVote);
        txtPopularity = findViewById(R.id.txtPopularity);
        txtReleaseDate = findViewById(R.id.txtReleaseDate);
        imageView = findViewById(R.id.poster);
        btnAddToFavorite = findViewById(R.id.btnAddToFavorite);
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID, 0);
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        movieDetailsViewModel.askForMovie(movieId);
        movieDetailsViewModel.getMovieObservable().observeForever(movieDetailsObserver);
        movieDetailsViewModel.isMovieFavorite();

        if (movieDetailsViewModel.isUserSignedIn()) {
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
        @Override
        public void onChanged(MovieDetails md) {
            if (md != null) {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.movie_details_layout).setVisibility(View.VISIBLE);

                txtTitle.setText(md.getTitle());
                txtRuntime.setText(String.valueOf(md.getRuntime()));
                txtOverview.setText(md.getOverview());
                txtAverageVote.setText(String.valueOf(md.getAverageVote()));
                txtPopularity.setText(String.valueOf(md.getPopularity()));
                txtReleaseDate.setText(md.getReleaseDate());
                Picasso.get().load(md.getPosterPath()).fit().into(imageView);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieDetailsViewModel.getMovieObservable().removeObserver(movieDetailsObserver);
        movieDetailsViewModel.getSavedBtnStringObservable().removeObserver(isFavoriteObserver);
        movieDetailsViewModel.stopObserver();
        this.unregisterReceiver(internetConnectionReceiver);
    }
}
