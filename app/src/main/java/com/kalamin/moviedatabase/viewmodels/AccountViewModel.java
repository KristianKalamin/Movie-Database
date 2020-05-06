package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.utils.Codes;

import org.jetbrains.annotations.NotNull;

public class AccountViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public Codes checkCredentials(@NotNull String oldPassword, @NotNull String newPassword) {
        if (newPassword.length() < 6)
            return Codes.PASSWORD_TO_SHORT;
        if (oldPassword.equals(newPassword))
            return Codes.PASSWORDS_ARE_EQUAL;
        if (!oldPassword.equals(firebaseRepository.getUser().getPassword()))
            return Codes.WRONG_OLD_PASSWORD;

        return Codes.OK;
    }
}
