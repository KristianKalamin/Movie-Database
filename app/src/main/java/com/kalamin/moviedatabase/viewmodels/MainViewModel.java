package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.kalamin.moviedatabase.repository.FirebaseRepository;

public class MainViewModel extends AndroidViewModel {
    private boolean isUserSignedIn;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
        isUserSignedIn = firebaseRepository.isUserSignedIn();
    }

    public boolean isUserSignedIn() {
        return isUserSignedIn;
    }
}
