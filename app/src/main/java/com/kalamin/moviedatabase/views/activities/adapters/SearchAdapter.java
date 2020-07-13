package com.kalamin.moviedatabase.views.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.Frame;
import com.kalamin.moviedatabase.utils.Extra;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter implements ListAdapter {
    private Context context;
    private List<? extends Frame> frames;
    private Class<?> destinationActivity;

    public SearchAdapter(Context context, List<? extends Frame> frames, Class<?> destinationActivity) {
        this.context = context;
        this.frames = frames;
        this.destinationActivity = destinationActivity;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return frames.size();
    }

    @Override
    public Object getItem(int position) {
        return frames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(frames.get(position).getId());
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Frame frame = frames.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, parent, false);
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(context, destinationActivity);
                intent.putExtra(Extra.SEARCH_ITEM_ID, frame.getId());
                context.startActivity(intent);
            });
        }
        TextView movieTitle = convertView.findViewById(R.id.list_movie_title);
        ImageView imageView = convertView.findViewById(R.id.list_movie_poster);
        movieTitle.setText(frame.getName());
        Picasso.get().load(frame.getPosterPath()).fit().centerCrop().into(imageView);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return frames.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
