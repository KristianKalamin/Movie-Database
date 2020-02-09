package com.kalamin.moviedatabase.views.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.listener.InternetConnectionReceiver;
import com.kalamin.moviedatabase.viewmodels.SearchableViewModel;
import com.kalamin.moviedatabase.views.activities.adapters.SearchAdapter;

import org.jetbrains.annotations.NotNull;

public class SearchableActivity extends AppCompatActivity {
    private SearchableViewModel searchableViewModel;
    private ListView listView;

    private InternetConnectionReceiver internetConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        if (savedInstanceState == null) {
            internetConnectionReceiver = new InternetConnectionReceiver();
            this.registerReceiver(internetConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            internetConnectionReceiver.setInternetConnectionListener(isConnected -> {
                if (!isConnected) {
                    startActivity(new Intent(SearchableActivity.this, NoInternetActivity.class));
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchableViewModel = ViewModelProviders.of(this).get(SearchableViewModel.class);
        listView = findViewById(R.id.list_view);

        handleIntent(getIntent());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_m).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    @SuppressLint("SetTextI18n")
    private void handleIntent(@NotNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchableViewModel.searchMovies(query).observe(this, movies -> {
                if (movies.size() > 0) {
                    SearchAdapter customAdapter = new SearchAdapter(SearchableActivity.this, movies);
                    listView.setAdapter(customAdapter);
                } else {
                    listView.setAdapter(null);
                    TextView searchResults = findViewById(R.id.search_results);
                    searchResults.setText("No movies found for: '"+query+"'");
                    searchResults.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(internetConnectionReceiver);
    }
}
