package com.kalamin.moviedatabase.views.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {
    private List<Actor> actors = new ArrayList<>(5);
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new ActorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Actor currentActor = actors.get(position);
        Picasso.get().load(currentActor.getPosterPath()).fit().centerCrop().into(holder.poster);
        holder.name.setText(currentActor.getName());
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Actor actor);
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;

        ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION)
                    onItemClickListener.onItemClick(actors.get(position));
            });
        }
    }
}
