package com.kalamin.moviedatabase.listener;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.HashMap;
import java.util.Map;

public class FirebaseListener {
    private MovieRepository movieRepository;
    private static FirebaseListener firebaseListener = null;
    public static MutableLiveData<Map<String, Movie>> favoriteMoviesLiveData;
    private DatabaseReference reference;
    private Context context;

    public synchronized static FirebaseListener getInstance(Context context) {
        if (firebaseListener == null)
            firebaseListener = new FirebaseListener(context);
        return firebaseListener;
    }

    private FirebaseListener(Context context) {
        movieRepository = MovieRepository.getInstance(context);
        this.context = context;
        favoriteMoviesLiveData = new MutableLiveData<>();
    }

    public void startFavoriteMoviesListener() {
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance(context);
        reference = firebaseRepository.getFavoriteMoviesReference()
                .child(firebaseRepository.getUser().getFirebaseUser().getUid());

        reference.addValueEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
            if (map != null) {
                Map<String, Movie> idMovieMap = new HashMap<>(map.size());

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    idMovieMap.put(entry.getKey(), movieRepository.getMovie(entry.getValue()));
                }
                favoriteMoviesLiveData.postValue(idMovieMap);
            } else {
                favoriteMoviesLiveData.postValue(new HashMap<String, Movie>());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void stopFavoriteMoviesListener() {
        reference.removeEventListener(valueEventListener);
    }

}
