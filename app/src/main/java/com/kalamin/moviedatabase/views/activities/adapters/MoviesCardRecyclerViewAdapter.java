package com.kalamin.moviedatabase.views.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesCardRecyclerViewAdapter extends RecyclerView.Adapter<MoviesCardRecyclerViewAdapter.MovieCardViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private String card;

    public MoviesCardRecyclerViewAdapter(String card) {
        this.card = card;
    }

    @NonNull
    @Override
    public MovieCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (card.equals("movie")) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
            return new MovieCardViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_movie_card, parent, false);
            return new MovieCardViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCardViewHolder holder, int position) {
        Movie currentMovie = movies.get(position);
        Picasso.get().load(currentMovie.getPosterPath()).fit().centerCrop().into(holder.imageViewPoster);
        holder.textViewTitle.setText(currentMovie.getTitle());
        if (card.equals("favorite_movie"))
            holder.averageVote.setText(String.valueOf(currentMovie.getAverageVote()));
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie getMovieAt(int position) {
        return movies.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    class MovieCardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPoster;
        TextView textViewTitle;
        TextView averageVote;

        MovieCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.movie_poster);
            textViewTitle = itemView.findViewById(R.id.movie_title);
            averageVote = itemView.findViewById(R.id.average_vote);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(movies.get(position));
                }
            });
        }
    }
}
