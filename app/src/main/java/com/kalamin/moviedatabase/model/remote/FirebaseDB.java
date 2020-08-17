package com.kalamin.moviedatabase.model.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.utils.Hash;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class FirebaseDB {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseUserReference = database.getReference().child("Users");
    private DatabaseReference databaseFavoriteMoviesReference = database.getReference().child("FavoriteMovies");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void logout() {
        firebaseAuth.signOut();
    }

    public DatabaseReference getDatabaseFavoriteMoviesReference() {
        return databaseFavoriteMoviesReference;
    }

    public DatabaseReference getDatabaseUserReference() {
        return databaseUserReference;
    }

    public void addFavoriteMovie(@NotNull String movieDetailsId, String userId) {
        databaseFavoriteMoviesReference
                .child(userId)
                .push()
                .setValue(movieDetailsId);
    }

    public void removeFavoriteMovie(@NotNull Movie movie, String userId) {
        databaseFavoriteMoviesReference
                .child(userId)
                .child(movie.getFirebaseId())
                .removeValue();
    }

    public void removeFavoriteMovie(String key, String userId) {
        databaseFavoriteMoviesReference
                .child(userId)
                .child(String.valueOf(key))
                .removeValue();
    }

    public void setNewUserPassword(String password, String userId) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.updatePassword(password);
        databaseUserReference.child(userId).child("password").setValue(Hash.password(password));
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
