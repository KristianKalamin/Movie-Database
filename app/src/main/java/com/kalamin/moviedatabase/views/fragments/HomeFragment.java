package com.kalamin.moviedatabase.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.HomeViewModel;
import com.kalamin.moviedatabase.views.activities.MovieDetailActivity;
import com.kalamin.moviedatabase.views.activities.adapters.MoviesCardRecyclerViewAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private static final int SHOW_MOVIE_DETAILS = 1;
    private HomeViewModel homeViewModel;
    private MoviesCardRecyclerViewAdapter adapter;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        adapter = new MoviesCardRecyclerViewAdapter("movie");
        recyclerView.setAdapter(adapter);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        //homeViewModel.getDiscoveredMovies().observe(this, adapter::setMovies);

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(Extra.MOVIE_ID, movie.getId());

            startActivityForResult(intent, SHOW_MOVIE_DETAILS);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        homeViewModel.getDiscoveredMovies().observeForever(adapter::setMovies);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeViewModel.getDiscoveredMovies().removeObservers(this);
    }
}
