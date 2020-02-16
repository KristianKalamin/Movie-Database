package com.kalamin.moviedatabase.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kalamin.moviedatabase.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BiographyFragment extends Fragment {
    private static String biography;

    public BiographyFragment() {
    }

    @NotNull
    @Contract("_ -> new")
    public static BiographyFragment newInstance(String bio) {
        biography = bio;
        return new BiographyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_biography, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textView = view.findViewById(R.id.actor_bio);
        textView.setText(biography);
    }
}
