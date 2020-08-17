package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.kalamin.moviedatabase.AbsentLiveData;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.repository.ActorsRepository;

import java.util.List;

public class ActorsViewModel extends AndroidViewModel {
    private ActorsRepository actorsRepository;
    private LiveData<List<Actor>> actors;

    public ActorsViewModel(@NonNull Application application) {
        super(application);
        actorsRepository = ActorsRepository.getInstance();
        actors = AbsentLiveData.create();
    }

    public void askForActors(String currentMovieId) {
        actors = Transformations.map(actorsRepository.getActors(currentMovieId), fun -> fun);
    }

    public LiveData<List<Actor>> getActors() {
        return actors;
    }
}
