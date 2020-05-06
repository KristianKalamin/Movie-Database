package com.kalamin.moviedatabase;

import android.app.Application;
import android.content.Context;

public class MovieDatabase extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }
}
