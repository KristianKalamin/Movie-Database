package com.kalamin.moviedatabase.views.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.viewmodels.SearchableViewModel;
import com.kalamin.moviedatabase.views.activities.adapters.SearchResultsPageAdapter;

import org.jetbrains.annotations.NotNull;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class SearchableActivity extends BaseActivity {
    private SearchableViewModel searchableViewModel;
    private SearchResultsPageAdapter searchResultsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(SearchableActivity.this, NoInternetActivity.class));
            }
        });

        setToolbarWithBackButton();

        searchableViewModel = new ViewModelProvider(this).get(SearchableViewModel.class);
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        searchResultsPageAdapter = new SearchResultsPageAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(searchResultsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        handleIntent(getIntent());
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
            setTitle(query);
            searchableViewModel.search(query);
            searchResultsPageAdapter.notifyDataSetChanged();
        }
    }
}
