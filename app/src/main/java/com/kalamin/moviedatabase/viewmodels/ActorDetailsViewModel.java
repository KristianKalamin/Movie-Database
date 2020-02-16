package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.model.entity.ActorDetails;
import com.kalamin.moviedatabase.repository.ActorsRepository;

public class ActorDetailsViewModel extends AndroidViewModel {
    private ActorsRepository actorsRepository;
    private MutableLiveData<ActorDetails> actorDetails = new MutableLiveData<>();
    private Handler handler;

    public ActorDetailsViewModel(@NonNull Application application) {
        super(application);
        actorsRepository = ActorsRepository.getInstance(getApplication().getApplicationContext());
        handler = new Handler(Looper.getMainLooper());
    }

    public void askActorDetails(String actorId) {
        handler.postDelayed(() -> actorDetails.postValue(actorsRepository.getActorDetails(actorId)), 250);
    }

    public MutableLiveData<ActorDetails> getActorDetails() {
        return actorDetails;
    }
}
