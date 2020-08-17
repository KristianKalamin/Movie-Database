package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.kalamin.moviedatabase.AbsentLiveData;
import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieDetailsViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Boolean> savedBtnString = new MutableLiveData<>(false);
    private LiveData<MovieDetails> movie;
    private Handler handler;
    private String currentMovieKey;
    private String currentMovieId;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance();
        firebaseRepository = FirebaseRepository.getInstance();
        movie = AbsentLiveData.create();
        handler = new Handler(Looper.getMainLooper());
    }

    public void askForMovie(String id) {
        currentMovieId = id;
        movie = Transformations.map(movieRepository.getMovieDetails(id), fun -> fun);
    }

    public LiveData<MovieDetails> getMovieObservable() {
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

    private Observer<List<Movie>> mapObserver = new Observer<List<Movie>>() {
        @Override
        public void onChanged(@NotNull List<Movie> movies) {
            if (movies.size() == 0) {
                savedBtnString.postValue(false);
                return;
            }

            for (Movie movie : movies) {
                if (movie.getId().equals(currentMovieId)) {
                    savedBtnString.postValue(true);
                    currentMovieKey = movie.getFirebaseId();
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
        FirebaseListener.favoriteMoviesLiveData.removeObserver(mapObserver);
    }
}
