package com.kalamin.moviedatabase.views.activities.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kalamin.moviedatabase.views.fragments.BiographyFragment;
import com.kalamin.moviedatabase.views.fragments.GalleryFragment;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ActorDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private static final CharSequence[] tabNames = {"Biography", "Gallery"};
    private String bio;
    private List<String> galleryUrls;

    public ActorDetailsPagerAdapter(@NonNull FragmentManager fm, int behavior, String bio, List<String> galleryUrls) {
        super(fm, behavior);
        this.bio = bio;
        this.galleryUrls = galleryUrls;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BiographyFragment.newInstance(bio);
            case 1:
                return GalleryFragment.newInstance(galleryUrls);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
