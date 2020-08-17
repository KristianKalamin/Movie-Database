package com.kalamin.moviedatabase.views.activities.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kalamin.moviedatabase.views.fragments.SearchActorsFragment;
import com.kalamin.moviedatabase.views.fragments.SearchMoviesFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchResultsPageAdapter extends FragmentStatePagerAdapter {
    private static final CharSequence[] tabNames = {"Movies", "Actors"};

    public SearchResultsPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchMoviesFragment.newInstance();
            case 1:
                return SearchActorsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
