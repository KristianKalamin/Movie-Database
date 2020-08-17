package com.kalamin.moviedatabase.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MovieDetails implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private double averageVote;
    private int runtime;
    private double popularity;

    public MovieDetails() {
    }

    public MovieDetails(int id,
                        String title,
                        String overview,
                        String posterPath,
                        String releaseDate,
                        double averageVote,
                        int runtime,
                        double popularity) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.averageVote = averageVote;
        this.runtime = runtime;
        this.popularity = popularity;
    }

    protected MovieDetails(@NotNull Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        averageVote = in.readDouble();
        runtime = in.readInt();
        popularity = in.readDouble();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @NotNull
        @Contract("_ -> new")
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @NotNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @NotNull
    @Override
    public String toString() {
        return "MovieDetails{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate=" + releaseDate +
                ", averageVote=" + averageVote +
                ", runtime=" + runtime +
                ", popularity=" + popularity +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeDouble(averageVote);
        dest.writeInt(runtime);
        dest.writeDouble(popularity);
    }
}
