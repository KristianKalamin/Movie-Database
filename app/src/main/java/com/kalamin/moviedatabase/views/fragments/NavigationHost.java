package com.kalamin.moviedatabase.views.fragments;

import androidx.fragment.app.Fragment;

public interface NavigationHost {
    void navigateTo(int containerViewId, Fragment fragment, boolean addToBackstack);
}
