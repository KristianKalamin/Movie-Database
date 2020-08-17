package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.kalamin.moviedatabase.AbsentLiveData;
import com.kalamin.moviedatabase.model.entity.ActorDetails;
import com.kalamin.moviedatabase.repository.ActorsRepository;

public class ActorDetailsViewModel extends AndroidViewModel {
    private ActorsRepository actorsRepository;
    private LiveData<ActorDetails> actorDetails;

    public ActorDetailsViewModel(@NonNull Application application) {
        super(application);
        actorsRepository = ActorsRepository.getInstance();
        actorDetails = AbsentLiveData.create();
    }

    public void askActorDetails(String actorId) {
        actorDetails = Transformations.map(actorsRepository.getActorDetails(actorId), fun -> fun);
    }

    public LiveData<ActorDetails> getActorDetails() {
        return actorDetails;
    }
}
