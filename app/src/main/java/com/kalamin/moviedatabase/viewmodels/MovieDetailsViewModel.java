package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MovieDetailsViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private FirebaseListener firebaseListener;
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Boolean> savedBtnString = new MutableLiveData<>(false);
    private MutableLiveData<MovieDetails> movie = new MutableLiveData<>();
    private Handler handler;
    private String currentMovieKey;
    private int currentMovieId;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(getApplication().getApplicationContext());
        firebaseRepository = FirebaseRepository.getInstance(getApplication().getApplicationContext());
        firebaseListener = FirebaseListener.getInstance(getApplication().getApplicationContext());
        handler = new Handler(application.getMainLooper());
    }

    public void askForMovie(int id) {
        currentMovieId = id;
        handler.post(() -> movie.postValue(movieRepository.getMovieDetails(id)));
    }

    public MutableLiveData<MovieDetails> getMovieObservable() {
        return movie;
    }

    public MutableLiveData<Boolean> getSavedBtnStringObservable() {
        return savedBtnString;
    }

    public void addToFavorite() {
        handler.post(() -> firebaseRepository.addFavoriteMovie(String.valueOf(currentMovieId)));
    }

    public void isMovieFavorite() {
        this.firebaseListener.getFavoriteMoviesLiveData().observeForever(mapObserver);
    }

    private Observer<Map<String, Movie>> mapObserver = new Observer<Map<String, Movie>>() {
        @Override
        public void onChanged(@NotNull Map<String, Movie> stringMovieMap) {
            if (stringMovieMap.size() == 0) {
                savedBtnString.postValue(false);
                return;
            }

            for (Map.Entry<String, Movie> entry : stringMovieMap.entrySet()) {
                String key = entry.getKey();
                Movie movie = entry.getValue();
                if (movie.getId() == currentMovieId) {
                    savedBtnString.postValue(true);
                    currentMovieKey = key;
                    break;
                } else {
                    savedBtnString.postValue(false);
                }
            }
        }
    };

    public boolean isUserSignedIn() {
        return firebaseRepository.isUserSignedIn();
    }

    public void removeFavorite() {
        handler.post(() -> firebaseRepository.removeFavoriteMovie(this.currentMovieKey));
    }

    public void stopObserver() {
        this.firebaseListener.getFavoriteMoviesLiveData().removeObserver(mapObserver);
    }
}
