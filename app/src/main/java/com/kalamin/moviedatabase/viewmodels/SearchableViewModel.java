package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.kalamin.moviedatabase.AbsentLiveData;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.ActorsRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchableViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private ActorsRepository actorsRepository;
    private LiveData<List<Movie>> movies;
    private LiveData<List<Actor>> actors;

    public SearchableViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance();
        actorsRepository = ActorsRepository.getInstance();

        movies = AbsentLiveData.create();
        actors = AbsentLiveData.create();
    }

    public void search(String query) {
        try {
            movies = Transformations.map(movieRepository.searchMovies(query), fun -> fun);
            actors = Transformations.map(actorsRepository.searchActors(query), fun -> fun);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<Actor>> getActors() {
        return actors;
    }
}
