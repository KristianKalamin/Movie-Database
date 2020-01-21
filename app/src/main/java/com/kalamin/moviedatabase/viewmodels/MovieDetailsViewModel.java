package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class MovieDetailsViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private FirebaseListener firebaseListener;
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Boolean> savedBtnString = new MutableLiveData<>();

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(getApplication().getApplicationContext());
        firebaseRepository = FirebaseRepository.getInstance(getApplication().getApplicationContext());
        firebaseListener = FirebaseListener.getInstance(getApplication().getApplicationContext());
    }

    public LiveData<MovieDetails> getMovie(int id) {
        return movieRepository.getMovieDetails(id);
    }

    public void addToFavorite(String movieDetailsId) {
        firebaseRepository.addFavoriteMovie(movieDetailsId);
    }

    public MutableLiveData<Boolean> isMovieFavorite(int id) {
        try {
            boolean b = new AsyncCheck().execute(id).get();
            savedBtnString.postValue(b);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return savedBtnString;
    }

    public boolean isUserSignedIn() {
        return firebaseRepository.isUserSignedIn();
    }

    public void removeFavorite(int id) {
        firebaseRepository.removeFavoriteMovie(id);
    }

    private class AsyncCheck extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {

            Collection<Movie> movies = firebaseListener.getFavoriteMovies().values();
            for (Movie movie : movies) {
                if (movie.getId() == integers[0]) {
                    return true;
                }
            }
            return false;
        }

    }
}
