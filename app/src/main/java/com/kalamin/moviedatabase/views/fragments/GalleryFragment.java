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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.views.activities.PictureActivity;
import com.kalamin.moviedatabase.views.activities.adapters.GalleryAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryFragment extends Fragment {
    private static List<String> urls;

    public GalleryFragment() {
    }

    @NotNull
    @Contract("_ -> new")
    public static GalleryFragment newInstance(List<String> galleryUrls) {
        urls = galleryUrls;
        return new GalleryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_actor_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.gallery);
        TextView noImages = view.findViewById(R.id.no_images);
        if (urls.size() == 0) {
            noImages.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.setHasFixedSize(true);

            GalleryAdapter galleryAdapter = new GalleryAdapter();
            recyclerView.setAdapter(galleryAdapter);

            galleryAdapter.setUrls(urls);

            galleryAdapter.setOnImageClickListener(url -> {
                Intent intent = new Intent(getContext(), PictureActivity.class);
                intent.putExtra(Extra.URL, url);

                startActivity(intent);
            });
        }
    }
}
