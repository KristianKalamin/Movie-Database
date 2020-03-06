package com.kalamin.moviedatabase.views.activities;

import android.os.Bundle;

import com.kalamin.moviedatabase.R;

public class NoInternetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (isConnected) {
                NoInternetActivity.this.finish();
            }
        });

        findViewById(R.id.btnExit).setOnClickListener(v -> this.finishAffinity());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
