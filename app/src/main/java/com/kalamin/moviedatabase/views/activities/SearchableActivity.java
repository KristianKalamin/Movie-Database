package com.kalamin.moviedatabase.views.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.viewmodels.SearchableViewModel;
import com.kalamin.moviedatabase.views.activities.adapters.SearchAdapter;

import org.jetbrains.annotations.NotNull;

public class SearchableActivity extends AppCompatActivity {
    private SearchableViewModel searchableViewModel;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

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

    private void handleIntent(@NotNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchableViewModel.searchMovies(query).observe(this, movies -> {
                SearchAdapter customAdapter = new SearchAdapter(SearchableActivity.this, movies);
                listView.setAdapter(customAdapter);
            });
        }
    }
}
