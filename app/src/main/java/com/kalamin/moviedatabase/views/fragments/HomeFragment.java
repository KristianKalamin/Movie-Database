package com.kalamin.moviedatabase.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.HomeViewModel;
import com.kalamin.moviedatabase.views.activities.MovieDetailsActivity;
import com.kalamin.moviedatabase.views.activities.adapters.MovieAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private MovieAdapter adapter;
    private ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Contract(" -> new")
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        getActivity().setTitle("Movie Database");

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        adapter = new MovieAdapter("card");
        recyclerView.setAdapter(adapter);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getDiscoveredMovies().observeForever(movies -> {
            adapter.submitList(movies);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.putExtra(Extra.MOVIE_ID, movie.getId());

            startActivity(intent);
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeViewModel.getDiscoveredMovies().removeObservers(this);
    }
}
