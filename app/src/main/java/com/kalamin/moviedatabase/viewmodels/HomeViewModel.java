package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Movie>> discoveredMovies;
    private FirebaseRepository firebaseRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        Handler handler = new Handler(application.getMainLooper());
        MovieRepository movieRepository = MovieRepository.getInstance(application.getApplicationContext());
        discoveredMovies = new MutableLiveData<>();
        firebaseRepository = FirebaseRepository.getInstance(application.getApplicationContext());

        handler.postDelayed(() -> {
            try {
                discoveredMovies.postValue(movieRepository.discoverMovies());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 250);
    }

    public LiveData<List<Movie>> getDiscoveredMovies() {
        return discoveredMovies;
    }

    public boolean isUserSignedIn() {
        return firebaseRepository.isUserSignedIn();
    }
}
