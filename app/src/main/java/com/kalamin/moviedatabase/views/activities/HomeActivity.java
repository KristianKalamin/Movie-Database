package com.kalamin.moviedatabase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.viewmodels.HomeViewModel;
import com.kalamin.moviedatabase.views.fragments.AccountFragment;
import com.kalamin.moviedatabase.views.fragments.EnterAppFragment;
import com.kalamin.moviedatabase.views.fragments.FavoriteMoviesFragment;
import com.kalamin.moviedatabase.views.fragments.HomeFragment;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbarWithDrawer();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        this.internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(HomeActivity.this, NoInternetActivity.class));
            } else {
                navigateTo(R.id.activity_home_fragment, HomeFragment.newInstance(), false);
                navigationView.setCheckedItem(R.id.nav_home);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        new Handler().postDelayed(() -> {
            int currentViewId = R.id.activity_home_fragment;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    navigateTo(currentViewId, HomeFragment.newInstance(), true);
                    break;
                case R.id.nav_favorites:
                    if (homeViewModel.isUserSignedIn())
                        navigateTo(currentViewId, FavoriteMoviesFragment.newInstance(), true);
                    else
                        navigateTo(currentViewId, EnterAppFragment.newInstance(R.id.activity_home), true);
                    break;
                case R.id.nav_account:
                    if (homeViewModel.isUserSignedIn())
                        navigateTo(currentViewId, AccountFragment.newInstance(currentViewId), true);
                    else
                        navigateTo(currentViewId, EnterAppFragment.newInstance(R.id.activity_home), true);
                    break;
            }
        }, 200);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
