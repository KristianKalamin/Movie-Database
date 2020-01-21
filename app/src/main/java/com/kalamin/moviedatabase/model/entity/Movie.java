package com.kalamin.moviedatabase.model.entity;

public class Movie {
    private int id;
    private String title;
    private String posterPath;
    private double averageVote;

    public Movie(int id,
                 String title,
                 String posterPath,
                 double averageVote) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.averageVote = averageVote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", averageVote=" + averageVote +
                '}';
    }
}
