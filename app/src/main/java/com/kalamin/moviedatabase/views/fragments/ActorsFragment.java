package com.kalamin.moviedatabase.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.ActorsViewModel;
import com.kalamin.moviedatabase.views.activities.ActorDetailsActivity;
import com.kalamin.moviedatabase.views.activities.adapters.ActorAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorsFragment extends Fragment {
    private ActorsViewModel actorsViewModel;
    private ActorAdapter actorAdapter;
    private TextView topCast;
    private RecyclerView recyclerView;

    public ActorsFragment() {
    }

    @NotNull
    @Contract(" -> new")
    public static ActorsFragment newInstance() {
        return new ActorsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.actors_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        actorsViewModel = new ViewModelProvider(getActivity()).get(ActorsViewModel.class);
        recyclerView = view.findViewById(R.id.actors_list_recycler_view);
        topCast = view.findViewById(R.id.txt_top_cast);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        actorAdapter = new ActorAdapter();
        recyclerView.setAdapter(actorAdapter);

        actorAdapter.setOnItemClickListener(actor -> {
            Intent intent = new Intent(getContext(), ActorDetailsActivity.class);
            intent.putExtra(Extra.ACTOR_ID, actor.getId());

            startActivity(intent);
        });
    }

    private Observer<List<Actor>> actorObserver = new Observer<List<Actor>>() {
        @Override
        public void onChanged(List<Actor> actors) {
            if (actors != null) {
                topCast.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                actorAdapter.setActors(actors);
            } else {
                topCast.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    };

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onStart() {
        super.onStart();
        String movieId = getActivity().getIntent().getStringExtra(Extra.MOVIE_ID);
        if (movieId == null)
            movieId = getActivity().getIntent().getStringExtra(Extra.SEARCH_ITEM_ID);
        actorsViewModel.askForActors(movieId);
        actorsViewModel.getActors().observe(getViewLifecycleOwner(), actorObserver);
    }
}
