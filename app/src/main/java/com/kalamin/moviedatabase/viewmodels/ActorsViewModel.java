package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.repository.ActorsRepository;

import java.util.List;

public class ActorsViewModel extends AndroidViewModel {
    private ActorsRepository actorsRepository;
    private MutableLiveData<List<Actor>> actors = new MutableLiveData<>();
    private Handler handler;

    public ActorsViewModel(@NonNull Application application) {
        super(application);
        actorsRepository = ActorsRepository.getInstance();
        handler = new Handler(Looper.getMainLooper());
    }

    public void askForActors(String currentMovieId) {
        handler.postDelayed(() -> actors.postValue(actorsRepository.getActors(currentMovieId)), 250);
    }

    public MutableLiveData<List<Actor>> getActors() {
        return actors;
    }
}
