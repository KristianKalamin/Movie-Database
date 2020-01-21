package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;

import java.util.Map;

public class FavoriteMoviesViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private FirebaseListener firebaseListener;
    private MutableLiveData<Map<String, Movie>> favoriteMovies;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance(getApplication().getApplicationContext());
        firebaseListener = FirebaseListener.getInstance(getApplication().getApplicationContext());
        favoriteMovies = firebaseListener.getFavoriteMoviesLiveData();
    }

    public MutableLiveData<Map<String, Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void deleteFavoriteMovie(Movie movie) {
        firebaseRepository.removeFavoriteMovie(movie);
    }
}
