package com.kalamin.moviedatabase.views.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<String> urls;
    private OnImageClickListener onImageClickListener;

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String url = urls.get(position);
        Picasso.get().load(url).fit().centerCrop().into(holder.imageView);
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public interface OnImageClickListener {
        void onImageClick(String url);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onImageClickListener != null && position != RecyclerView.NO_POSITION) {
                    onImageClickListener.onImageClick(urls.get(position));
                }
            });
        }
    }
}
