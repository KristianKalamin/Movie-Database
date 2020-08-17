package com.kalamin.moviedatabase.model.entity;

import org.jetbrains.annotations.NotNull;

public abstract class Frame {
    private String id;
    private String name;
    private String posterPath;

    public Frame() {
    }

    Frame(String id, String name, String posterPath) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @NotNull
    @Override
    public String toString() {
        return "Frame{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
