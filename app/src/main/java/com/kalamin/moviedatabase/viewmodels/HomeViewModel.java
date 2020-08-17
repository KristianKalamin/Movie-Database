package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.kalamin.moviedatabase.AbsentLiveData;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> discoveredMovies;
    private FirebaseRepository firebaseRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        MovieRepository movieRepository = MovieRepository.getInstance();
        discoveredMovies = AbsentLiveData.create();
        firebaseRepository = FirebaseRepository.getInstance();
        try {
            discoveredMovies = Transformations.map(movieRepository.discoverMovies(), fun -> fun);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Movie>> getDiscoveredMovies() {
        return discoveredMovies;
    }

    public boolean isUserSignedIn() {
        return firebaseRepository.isUserSignedIn();
    }
}
