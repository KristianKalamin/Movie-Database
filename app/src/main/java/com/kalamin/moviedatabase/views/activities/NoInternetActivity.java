package com.kalamin.moviedatabase.views.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;

public class NoInternetActivity extends AppCompatActivity {
    private InternetConnectionReceiver internetConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        if (savedInstanceState == null) {
            internetConnectionReceiver = new InternetConnectionReceiver();
            this.registerReceiver(internetConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            internetConnectionReceiver.setInternetConnectionListener(isConnected -> {
                if (isConnected) {
                    NoInternetActivity.this.finish();
                }
            });
        }

        findViewById(R.id.btnExit).setOnClickListener(v ->
                this.finish());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(internetConnectionReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}