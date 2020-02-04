package com.kalamin.moviedatabase.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.viewmodels.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

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

    private MovieDetails movieDetails;

    private MovieDetailsViewModel movieDetailsViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            InternetConnectionReceiver internetConnectionReceiver = new InternetConnectionReceiver();
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
        Intent intent = getIntent();
        int id = intent.getIntExtra(MOVIE_ID, 0);
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        if (!movieDetailsViewModel.isUserSignedIn()) {
            btnAddToFavorite.setVisibility(View.GONE);
        } else {
            movieDetailsViewModel.isMovieFavorite(id).observe(this, res -> {
                if (res) {
                    btnAddToFavorite.setText("Remove From Favorite");
                    btnAddToFavorite.setOnClickListener(v -> {
                        movieDetailsViewModel.removeFavorite(id);
                        btnAddToFavorite.setText("Add To Favorite");
                    });
                } else {
                    btnAddToFavorite.setText("Add To Favorite");
                    btnAddToFavorite.setOnClickListener(v -> {
                        movieDetailsViewModel.addToFavorite(String.valueOf(this.movieDetails.getId()));
                        btnAddToFavorite.setText("Remove From Favorite");
                    });
                }
            });
        }

        LiveData<MovieDetails> movieDetails = movieDetailsViewModel.getMovie(id);
        movieDetails.observe(MovieDetailActivity.this, md -> {
            this.movieDetails = md;
            txtTitle.setText(md.getTitle());
            txtRuntime.setText(String.valueOf(md.getRuntime()));
            txtOverview.setText(md.getOverview());
            txtAverageVote.setText(String.valueOf(md.getAverageVote()));
            txtPopularity.setText(String.valueOf(md.getPopularity()));
            txtReleaseDate.setText(md.getReleaseDate());
            Picasso.get().load(md.getPosterPath()).fit().centerCrop().into(imageView);
        });
    }
}
