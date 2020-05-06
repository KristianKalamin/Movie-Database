package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;

import java.util.Map;

public class FavoriteMoviesViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private Handler handler;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance();
        handler = new Handler(Looper.getMainLooper());
    }

    public MutableLiveData<Map<String, Movie>> getFavoriteMovies() {
        return FirebaseListener.favoriteMoviesLiveData;
    }

    public void deleteFavoriteMovie(Movie movie) {
        handler.post(() -> firebaseRepository.removeFavoriteMovie(movie));
    }
}
