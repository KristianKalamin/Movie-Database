package com.kalamin.moviedatabase.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.FavoriteMoviesViewModel;
import com.kalamin.moviedatabase.views.activities.MovieDetailsActivity;
import com.kalamin.moviedatabase.views.activities.adapters.MovieAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FavoriteMoviesFragment extends Fragment {
    private static final int SHOW_MOVIE_DETAILS = 1;
    private FavoriteMoviesViewModel favoriteMoviesViewModel;
    private TextView msg;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ProgressBar progressBar;

    public FavoriteMoviesFragment() {
    }

    @NotNull
    @Contract(" -> new")
    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_movies, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        msg = view.findViewById(R.id.favorites_msg);
        progressBar = view.findViewById(R.id.favorite_movies_progress_bar);

        adapter = new MovieAdapter("favorite_movie");
        recyclerView.setAdapter(adapter);

        getActivity().setTitle("Favorite Movies");

        adapter.setOnItemClickListener(movie -> {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra(Extra.MOVIE_ID, movie.getId());

            startActivityForResult(intent, SHOW_MOVIE_DETAILS);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        favoriteMoviesViewModel = new ViewModelProvider(this).get(FavoriteMoviesViewModel.class);
        favoriteMoviesViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movies -> {
            progressBar.setVisibility(View.GONE);
            if (movies.size() == 0)
                msg.setVisibility(View.VISIBLE);
            else {
                msg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.submitList(movies);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                favoriteMoviesViewModel.deleteFavoriteMovie(adapter.getMovieAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }
}
