package com.kalamin.moviedatabase.utils;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

public final class Toast {
    public static void showInfo(Context context, @NotNull String msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void showErrorMsg(Context context, @NotNull Codes errorCode) {
        switch (errorCode){
            case INCORRECT_EMAIL:
                android.widget.Toast.makeText(context, "Incorrect email", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case EMPTY_FIELD:
                android.widget.Toast.makeText(context, "Input field is empty", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case PASSWORDS_ARE_EQUAL:
                android.widget.Toast.makeText(context, "New password can't be same as old password", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case COULD_NOT_LOGIN:
                android.widget.Toast.makeText(context, "Couldn't login", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case PASSWORD_TO_SHORT:
                android.widget.Toast.makeText(context, "Password must be longer than 6 characters", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case WRONG_OLD_PASSWORD:
                android.widget.Toast.makeText(context, "Wrong old password", android.widget.Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
