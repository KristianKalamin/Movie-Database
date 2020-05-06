package com.kalamin.moviedatabase.utils;

import com.kalamin.moviedatabase.MovieDatabase;
import com.kalamin.moviedatabase.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Reader {
    private String searchMovieEndPoint;
    private String discoverMoviesEndPoint;
    private String posterEndPoint;
    private String movieDetailsEndPoint;
    private String creditsEndPoint;
    private String actorEndPoint;
    private String actorImagesEndPoint;
    private String searchActorsEndPoint;

    private static Reader reader = null;

    public static Reader getInstance() {
        if (reader == null)
            reader = new Reader();
        return reader;
    }

    private Reader() {
        jsonKeys(parseJson());
    }

    @NotNull
    private String parseJson() {
        InputStream inputStream = MovieDatabase.applicationContext.getResources().openRawResource(R.raw.movie_api);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    private void jsonKeys(String json) {
        JSONObject endPoints;
        try {
            endPoints = new JSONObject(json).getJSONObject("end-points");
            this.searchMovieEndPoint = endPoints.getString("search-movies");
            this.discoverMoviesEndPoint = endPoints.getString("discover");
            this.posterEndPoint = endPoints.getString("poster");
            this.movieDetailsEndPoint = endPoints.getString("details");
            this.creditsEndPoint = endPoints.getString("credits");
            this.actorEndPoint = endPoints.getString("actor");
            this.actorImagesEndPoint = endPoints.getString("actor-images");
            this.searchActorsEndPoint = endPoints.getString("search-actors");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public String getSearchActorsEndPoint(String query) {
        return this.searchActorsEndPoint.replace("{QUERY}", query);
    }

    @NotNull
    public String getActorEndPoint(String actorId) {
        return this.actorEndPoint.replace("{ACTOR_ID}", actorId);
    }

    @NotNull
    public String getActorImagesEndPoint(String actorId) {
        return this.actorImagesEndPoint.replace("{ACTOR_ID}", actorId);
    }

    @NotNull
    public String getCreditsEndPoint(String movieId) {
        return this.creditsEndPoint.replace("{MOVIE-ID}", movieId);
    }

    @NotNull
    public String getMovieDetailsEndPoint(String movieId) {
        return this.movieDetailsEndPoint.replace("{MOVIE-ID}", movieId);
    }

    @NotNull
    public String getPosterEndPoint(String image) {
        return this.posterEndPoint.replace("{IMAGE}", image);
    }

    @NotNull
    public String getSearchMovieEndPoint(String movieName) {
        return this.searchMovieEndPoint.replace("{MOVIE-NAME}", movieName);
    }

    @NotNull
    public String getDiscoverMoviesEndPoint(int pageNum) {
        return this.discoverMoviesEndPoint.replace("{NUM}", String.valueOf(pageNum));
    }
}
