package com.kalamin.moviedatabase.views.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    private OnItemClickListener onItemClickListener;
    private String card;

    public MovieAdapter(String card) {
        super(DIFF_CALLBACK);
        this.card = card;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NotNull Movie oldItem, @NotNull Movie newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NotNull Movie oldItem, @NotNull Movie newItem) {
            return oldItem.getAverageVote() == newItem.getAverageVote() &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPosterPath().equals(newItem.getPosterPath()) &&
                    oldItem.getId().equals(newItem.getId());
        }
    };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (card.equals("card")) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
            return new MovieViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_movie_card, parent, false);
            return new MovieViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = getMovieAt(position);
        Picasso.get().load(currentMovie.getPosterPath()).fit().centerCrop().into(holder.imageViewPoster);
        holder.textViewTitle.setText(currentMovie.getName());
        if (card.equals("favorite_movie"))
            holder.averageVote.setText(String.valueOf(currentMovie.getAverageVote()));
    }

    public Movie getMovieAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPoster;
        TextView textViewTitle;
        TextView averageVote;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.poster);
            textViewTitle = itemView.findViewById(R.id.name);
            averageVote = itemView.findViewById(R.id.average_vote);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(getItem(position));
                }
            });
        }
    }
}
