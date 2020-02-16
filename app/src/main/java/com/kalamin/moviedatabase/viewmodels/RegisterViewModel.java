package com.kalamin.moviedatabase.viewmodels;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.kalamin.moviedatabase.utils.Codes;

import org.jetbrains.annotations.NotNull;

public class RegisterViewModel extends AndroidViewModel {

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public Codes checkCredentials(@NotNull String username, @NotNull String email, @NotNull String password) {
        if (username.length() == 0 || email.length() == 0 || password.length() == 0)
            return Codes.EMPTY_FIELD;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Codes.INCORRECT_EMAIL;
        }
        if (password.length() < 6)
            return Codes.PASSWORD_TO_SHORT;

        return Codes.OK;
    }
}
