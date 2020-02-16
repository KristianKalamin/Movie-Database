package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.ActorsRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchableViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private ActorsRepository actorsRepository;
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<List<Actor>> actors = new MutableLiveData<>();
    private Handler handler;

    public SearchableViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application.getApplicationContext());
        actorsRepository = ActorsRepository.getInstance(application.getApplicationContext());
        handler = new Handler(Looper.getMainLooper());
    }

    public void search(String query) {
        handler.postDelayed(() -> {
            try {
                movies.postValue(movieRepository.searchMovies(query));
                actors.postValue(actorsRepository.searchActors(query));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 50);
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<List<Actor>> getActors() {
        return actors;
    }
}
