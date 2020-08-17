package com.kalamin.moviedatabase.listener;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseListener {
    private MovieRepository movieRepository;
    private static FirebaseListener firebaseListener = null;
    public static MutableLiveData<List<Movie>> favoriteMoviesLiveData;
    private DatabaseReference reference;

    public synchronized static FirebaseListener getInstance() {
        if (firebaseListener == null)
            firebaseListener = new FirebaseListener();
        return firebaseListener;
    }

    private FirebaseListener() {
        movieRepository = MovieRepository.getInstance();
        favoriteMoviesLiveData = new MutableLiveData<>();
    }

    public void startFavoriteMoviesListener() {
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
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
                List<Movie> favMovies = new ArrayList<>(map.size());

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Movie movie = movieRepository.getMovie(entry.getValue()).getValue();
                    movie.setFirebaseId(entry.getKey());
                    favMovies.add(movie);
                }
                favoriteMoviesLiveData.postValue(favMovies);
            } else {
                favoriteMoviesLiveData.postValue(new ArrayList<>());
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
