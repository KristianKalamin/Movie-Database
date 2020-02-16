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
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.viewmodels.SearchableViewModel;
import com.kalamin.moviedatabase.views.activities.ActorDetailsActivity;
import com.kalamin.moviedatabase.views.activities.adapters.SearchAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchActorsFragment extends Fragment {
    private SearchableViewModel searchableViewModel;
    private ProgressBar progressBar;
    private ListView listView;
    private TextView textView;

    public SearchActorsFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Contract(" -> new")
    public static SearchActorsFragment newInstance() {
        return new SearchActorsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_actors, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progress_bar);
        listView = view.findViewById(R.id.list_view);
        textView = view.findViewById(R.id.actor_not_found);

        searchableViewModel = new ViewModelProvider(getActivity()).get(SearchableViewModel.class);

        searchableViewModel.getActors().observeForever(actorObserver);
    }

    @SuppressLint("SetTextI18n")
    private Observer<List<Actor>> actorObserver = actors -> {
        if (actors.size() > 0) {
            SearchAdapter searchAdapter = new SearchAdapter(getContext(), actors, ActorDetailsActivity.class);
            listView.setAdapter(searchAdapter);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setAdapter(null);
            textView.setText("Actor not found");
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onDestroyView() {
        searchableViewModel.getActors().removeObserver(actorObserver);
        super.onDestroyView();
    }
}
