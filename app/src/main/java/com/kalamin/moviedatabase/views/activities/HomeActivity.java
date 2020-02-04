package com.kalamin.moviedatabase.views.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;
import com.kalamin.moviedatabase.viewmodels.HomeViewModel;
import com.kalamin.moviedatabase.views.fragments.AccountFragment;
import com.kalamin.moviedatabase.views.fragments.EnterAppFragment;
import com.kalamin.moviedatabase.views.fragments.FavoriteMoviesFragment;
import com.kalamin.moviedatabase.views.fragments.HomeFragment;
import com.kalamin.moviedatabase.views.fragments.NavigationHost;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationHost {

    private DrawerLayout drawer;
    private HomeViewModel homeViewModel;

    private InternetConnectionReceiver internetConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.activity_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        if (savedInstanceState == null) {
            internetConnectionReceiver = new InternetConnectionReceiver();
            this.registerReceiver(internetConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        this.internetConnectionReceiver.setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(HomeActivity.this, NoInternetActivity.class));
            } else {
                navigateTo(HomeFragment.newInstance(), false);
                navigationView.setCheckedItem(R.id.nav_home);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_m).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                navigateTo(HomeFragment.newInstance(), true);
                break;
            case R.id.nav_favorites:
                if (homeViewModel.isUserSignedIn())
                    navigateTo(FavoriteMoviesFragment.newInstance(), true);
                else navigateTo(EnterAppFragment.newInstance(), true);
                break;
            case R.id.nav_account:
                if (homeViewModel.isUserSignedIn())
                    navigateTo(AccountFragment.newInstance(), true);
                else navigateTo(EnterAppFragment.newInstance(), true);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_home_fragment, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();
    }
}
