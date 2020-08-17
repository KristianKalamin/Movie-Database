package com.kalamin.moviedatabase.model.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorDetails {
    private String name;
    private String birthday;
    private String deathday;
    private String bio;
    private String placeOfBirth;
    private String imagePath;
    private double popularity;
    private String age;
    private List<String> images;

    public ActorDetails() {
    }

    public ActorDetails(String name, String birthday, String deathday, String bio, String placeOfBirth, String imagePath, double popularity, String age, List<String> images) {
        this.name = name;
        this.birthday = birthday;
        this.deathday = deathday;
        this.bio = bio;
        this.placeOfBirth = placeOfBirth;
        this.imagePath = imagePath;
        this.popularity = popularity;
        this.age = age;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @NotNull
    @Override
    public String toString() {
        return "ActorDetails{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", deathday='" + deathday + '\'' +
                ", bio='" + bio + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", popularity=" + popularity +
                ", age=" + age +
                '}';
    }
}
