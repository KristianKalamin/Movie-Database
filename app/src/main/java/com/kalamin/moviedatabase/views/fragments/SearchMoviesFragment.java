package com.kalamin.moviedatabase.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.viewmodels.SearchableViewModel;
import com.kalamin.moviedatabase.views.activities.MovieDetailsActivity;
import com.kalamin.moviedatabase.views.activities.adapters.SearchAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchMoviesFragment extends Fragment {
    private ProgressBar progressBar;
    private ListView listView;
    private TextView textView;
    private SearchableViewModel searchableViewModel;

    public SearchMoviesFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Contract(" -> new")
    public static SearchMoviesFragment newInstance() {
        return new SearchMoviesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movies, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progress_bar);
        listView = view.findViewById(R.id.list_view);
        textView = view.findViewById(R.id.movie_not_found);

        searchableViewModel = new ViewModelProvider(getActivity()).get(SearchableViewModel.class);
        searchableViewModel.getMovies().observeForever(movieObserver);
    }

    @SuppressLint("SetTextI18n")
    private Observer<List<Movie>> movieObserver = movies -> {
        if (movies.size() > 0) {
            SearchAdapter searchAdapter = new SearchAdapter(getContext(), movies, MovieDetailsActivity.class);
            listView.setAdapter(searchAdapter);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setAdapter(null);
            textView.setText("Movie not found");
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onDestroyView() {
        searchableViewModel.getMovies().removeObserver(movieObserver);
        super.onDestroyView();
    }
}
