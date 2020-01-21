package com.kalamin.moviedatabase.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.views.activities.MainActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NoInternetFragment extends Fragment {
    public NoInternetFragment() {
    }

    @NotNull
    @Contract(" -> new")
    public static NoInternetFragment newInstance() {
        return new NoInternetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_internet, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnExit).setOnClickListener(v -> getActivity().finish());
        view.findViewById(R.id.btnRefresh).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            getActivity().finish();
            startActivity(intent);

        });
    }
}
