package com.kalamin.moviedatabase.model.entity;

import org.jetbrains.annotations.NotNull;

public class Movie extends Frame {
    private double averageVote;
    private String firebaseId;

    public Movie() {
        super();
    }

    public Movie(String id, String name, String posterPath, double averageVote) {
        super(id, name, posterPath);
        this.averageVote = averageVote;
    }

    public Movie(String id, String name, String posterPath, double averageVote, String firebaseId) {
        super(id, name, posterPath);
        this.averageVote = averageVote;
        this.firebaseId = firebaseId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "Avg vote " + averageVote +
                "FirebaseId " + firebaseId;
    }
}
