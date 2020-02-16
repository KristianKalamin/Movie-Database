package com.kalamin.moviedatabase.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.viewmodels.MainViewModel;
import com.kalamin.moviedatabase.views.fragments.EnterAppFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(this, NoInternetActivity.class));
            } else {
                MainViewModel mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
                if (!mainViewModel.isUserSignedIn())
                    navigateTo(R.id.mainActivity, EnterAppFragment.newInstance(R.id.mainActivity), false);
                else {
                    MainActivity.this.finish();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        });
    }
}
