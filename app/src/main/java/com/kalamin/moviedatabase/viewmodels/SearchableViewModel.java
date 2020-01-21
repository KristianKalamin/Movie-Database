package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchableViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;

    public SearchableViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Movie>> searchMovies(String movieName) {
        try {
            return movieRepository.searchMovies(movieName);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
