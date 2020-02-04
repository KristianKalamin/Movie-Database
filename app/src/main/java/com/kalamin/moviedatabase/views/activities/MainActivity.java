package com.kalamin.moviedatabase.views.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;
import com.kalamin.moviedatabase.viewmodels.MainViewModel;
import com.kalamin.moviedatabase.views.fragments.EnterAppFragment;
import com.kalamin.moviedatabase.views.fragments.NavigationHost;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            InternetConnectionReceiver internetConnectionReceiver = new InternetConnectionReceiver();
            this.registerReceiver(internetConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            internetConnectionReceiver.setInternetConnectionListener(isConnected -> {
                if (!isConnected) {
                    startActivity(new Intent(this, NoInternetActivity.class));
                } else {
                    MainViewModel mainViewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);
                    if (!mainViewModel.isUserSignedIn())
                        navigateTo(EnterAppFragment.newInstance(), false);
                    else {
                        MainActivity.this.finish();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                }
            });
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainActivity, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();
    }
}
