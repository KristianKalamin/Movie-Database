package com.kalamin.moviedatabase.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.InternetConnection;
import com.kalamin.moviedatabase.viewmodels.MainViewModel;
import com.kalamin.moviedatabase.views.fragments.EnterAppFragment;
import com.kalamin.moviedatabase.views.fragments.NavigationHost;
import com.kalamin.moviedatabase.views.fragments.NoInternetFragment;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (!InternetConnection.checkConnection(this)) {
                navigateTo(NoInternetFragment.newInstance(), false);
            } else {
                MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
                if (!mainViewModel.isUserSignedIn())
                    navigateTo(EnterAppFragment.newInstance(), false);
                else {
                    this.finish();
                    startActivity(new Intent(this, HomeActivity.class));
                }
            }
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

        transaction.commit();
    }
}
