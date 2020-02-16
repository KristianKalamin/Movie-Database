package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

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
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Boolean> savedBtnString = new MutableLiveData<>(false);
    private MutableLiveData<MovieDetails> movie = new MutableLiveData<>();
    private Handler handler;
    private String currentMovieKey;
    private String currentMovieId;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(getApplication().getApplicationContext());
        firebaseRepository = FirebaseRepository.getInstance(getApplication().getApplicationContext());

        handler = new Handler(Looper.getMainLooper());
    }

    public void askForMovie(String id) {
        currentMovieId = id;
        handler.postDelayed(() -> movie.postValue(movieRepository.getMovieDetails(id)), 500);
    }

    public MutableLiveData<MovieDetails> getMovieObservable() {
        return movie;
    }

    public MutableLiveData<Boolean> getSavedBtnStringObservable() {
        return savedBtnString;
    }

    public void addToFavorite() {
        handler.postDelayed(() -> firebaseRepository.addFavoriteMovie(String.valueOf(currentMovieId)), 200);
    }

    public void isMovieFavorite() {
        FirebaseListener.favoriteMoviesLiveData.observeForever(mapObserver);
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
                if (movie.getId().equals(currentMovieId)) {
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
        handler.postDelayed(() -> firebaseRepository.removeFavoriteMovie(this.currentMovieKey), 200);
    }

    public void stopObserver() {
        FirebaseListener.favoriteMoviesLiveData.removeObserver(mapObserver);
    }
}
