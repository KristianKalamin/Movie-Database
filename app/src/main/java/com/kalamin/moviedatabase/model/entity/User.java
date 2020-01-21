package com.kalamin.moviedatabase.model.entity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class User {
    @Exclude
    private FirebaseUser firebaseUser;
    @Exclude
    private Map<String, Movie> movieMap = new HashMap<>();

    private String username;
    private String email;
    private String password;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(FirebaseUser firebaseUser, String username, String email, String password) {
        this.firebaseUser = firebaseUser;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Map<String, Movie> getMovieMap() {
        return movieMap;
    }

    public void setMovieMap(Map<String, Movie> movieMap) {
        this.movieMap = movieMap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
