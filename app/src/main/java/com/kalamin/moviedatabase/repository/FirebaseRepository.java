package com.kalamin.moviedatabase.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kalamin.moviedatabase.listener.FirebaseListener;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.model.entity.User;
import com.kalamin.moviedatabase.model.remote.FirebaseDB;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class FirebaseRepository {
    private static FirebaseRepository instance = null;
    private FirebaseListener firebaseListener;
    private User currentUser;
    private FirebaseDB firebaseDB;

    public synchronized static FirebaseRepository getInstance() {
        if (instance == null)
            instance = new FirebaseRepository();
        return instance;
    }

    private FirebaseRepository() {
        firebaseListener = FirebaseListener.getInstance();
        firebaseDB = new FirebaseDB();
    }

    public void startFavoriteMoviesListener(boolean on) {
        if (on)
            firebaseListener.startFavoriteMoviesListener();
        else firebaseListener.stopFavoriteMoviesListener();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseDB.getFirebaseAuth();
    }

    public DatabaseReference getUserReference() {
        return firebaseDB.getDatabaseUserReference();
    }

    public void logout() {
        firebaseDB.logout();
        firebaseListener.stopFavoriteMoviesListener();
    }

    public void addFavoriteMovie(@NotNull String movieDetailsId) {
        firebaseDB.addFavoriteMovie(movieDetailsId, currentUser.getFirebaseUser().getUid());
    }

    public void removeFavoriteMovie(@NotNull Movie movie) {
        firebaseDB.removeFavoriteMovie(movie, FirebaseListener.favoriteMoviesLiveData.getValue(), currentUser.getFirebaseUser().getUid());
    }

    public void removeFavoriteMovie(String key) {
        firebaseDB.removeFavoriteMovie(key, currentUser.getFirebaseUser().getUid());
    }

    public User getUser() {
        return currentUser;
    }

    public LiveData<User> getUserLiveData() {
        return new MutableLiveData<>(currentUser);
    }

    public boolean isUserSignedIn() {
        FirebaseUser fu = firebaseDB.getFirebaseAuth().getCurrentUser();
        if (fu != null) {
            firebaseDB.getDatabaseUserReference().child(fu.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    currentUser.setFirebaseUser(fu);
                    startFavoriteMoviesListener(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return true;
        } else return false;
    }

    public void setNewUserPassword(String password) {
        firebaseDB.setNewUserPassword(password, currentUser.getFirebaseUser().getUid());
    }

    public void setCurrentUser(@NotNull HashMap<String, Object> user, FirebaseUser firebaseUser) {
        currentUser = new User(firebaseUser, user.get("username").toString(), user.get("email").toString(), user.get("password").toString());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public DatabaseReference getFavoriteMoviesReference() {
        return firebaseDB.getDatabaseFavoriteMoviesReference();
    }

}
