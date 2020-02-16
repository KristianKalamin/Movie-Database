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
import com.kalamin.moviedatabase.views.activities.HomeActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnterAppFragment extends Fragment {
    private static int parentActivityId;

    public EnterAppFragment() {
    }

    @NotNull
    @Contract("_ -> new")
    public static EnterAppFragment newInstance(int activityId) {
        parentActivityId = activityId;
        return new EnterAppFragment();
    }


    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnLogin).setOnClickListener(v -> ((NavigationHost) Objects.requireNonNull(getActivity())).navigateTo(parentActivityId, LoginFragment.newInstance(), true));

        view.findViewById(R.id.btnRegister).setOnClickListener(v -> ((NavigationHost) Objects.requireNonNull(getActivity())).navigateTo(parentActivityId, RegisterFragment.newInstance(), true));

        view.findViewById(R.id.btnNotNow).setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), HomeActivity.class));
        });
    }
}
