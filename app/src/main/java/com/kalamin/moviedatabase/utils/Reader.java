package com.kalamin.moviedatabase.utils;

import android.content.Context;

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

    private static Reader reader = null;
    private Context appContext;

    public static Reader getInstance(Context applicationContext) {
        if (reader == null)
            reader = new Reader(applicationContext);
        return reader;
    }

    private Reader(Context applicationContext) {
        this.appContext = applicationContext;
        jsonKeys(parseJson());
    }

    @NotNull
    private String parseJson() {
        InputStream inputStream = this.appContext.getResources().openRawResource(R.raw.movie_api);
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
            this.searchMovieEndPoint = endPoints.getString("search");
            this.discoverMoviesEndPoint = endPoints.getString("discover");
            this.posterEndPoint = endPoints.getString("poster");
            this.movieDetailsEndPoint = endPoints.getString("details");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public String getMovieDetailsEndPoint(String movie_id) {
        return this.movieDetailsEndPoint.replace("{MOVIE-ID}", movie_id);
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
