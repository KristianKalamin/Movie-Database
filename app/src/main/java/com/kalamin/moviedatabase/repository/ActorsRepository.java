package com.kalamin.moviedatabase.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.model.entity.ActorDetails;
import com.kalamin.moviedatabase.model.remote.MoviesRestService;
import com.kalamin.moviedatabase.utils.Reader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActorsRepository {
    private static ActorsRepository instance;
    private Reader reader;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private MoviesRestService restService;

    public synchronized static ActorsRepository getInstance() {
        if (instance == null)
            instance = new ActorsRepository();
        return instance;
    }

    private ActorsRepository() {
        this.reader = Reader.getInstance();
        restService = new MoviesRestService();
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SimpleDateFormat")
    public LiveData<ActorDetails> getActorDetails(String id) {
        try {
            restService.setEndPointURL(this.reader.getActorEndPoint(id));
            String jsonResult = executor.submit(restService).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

            DateFormat localFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            SimpleDateFormat restDateFormat = new SimpleDateFormat("YYYY-mm-dd");
            Date birthdayDate = restDateFormat.parse((String) (JsonPath.read(document, "$['birthday']")));
            String birthday = localFormat.format(birthdayDate);

            String deathday = JsonPath.read(document, "$['deathday']");
            String age = "";
            if (deathday != null) {
                Date deathDate = restDateFormat.parse(deathday);
                assert deathDate != null;

                deathday = localFormat.format(deathDate);
                long diff = Math.abs(deathDate.getTime() - birthdayDate.getTime());
                age = Long.toString(diff / 31556952000L);
            } else {
                Date today = Calendar.getInstance().getTime();
                long diff = Math.abs(today.getTime() - birthdayDate.getTime());
                age = Long.toString(diff / 31556952000L);
            }

            String name = JsonPath.read(document, "$['name']");
            String bio = JsonPath.read(document, "$['biography']");
            String placeOfBirth = JsonPath.read(document, "$['place_of_birth']");
            String imagePath = (String) JsonPath.read(document, "$['profile_path']");
            imagePath = this.reader.getPosterEndPoint(imagePath);

            double popularity = ((Number) ((JsonPath.read(document, "$['popularity']") == null) ? 0 : JsonPath.read(document, "$['popularity']"))).doubleValue();

            return new MutableLiveData<>(new ActorDetails(name, birthday, deathday, bio, placeOfBirth, imagePath, popularity, age, getImages(id)));
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>(new ActorDetails());
    }

    public LiveData<List<Actor>> searchActors(String actorName) {
        List<Actor> searchResult = new ArrayList<>();
        try {
            restService.setEndPointURL(this.reader.getSearchActorsEndPoint(actorName));
            String jsonResult = executor.submit(restService).get();
            ArrayList<HashMap<String, Object>> actors = JsonPath.read(jsonResult, "$.results");

            for (int i = 0; i < actors.size(); i++) {
                int actorId = (int) actors.get(i).get("id");
                Actor ac = getActor(String.valueOf(actorId));
                if (ac != null)
                    searchResult.add(ac);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>(searchResult);
    }

    @Nullable
    private Actor getActor(String actorId) throws ExecutionException, InterruptedException {
        restService.setEndPointURL(this.reader.getActorEndPoint(actorId));
        String jsonResult = executor.submit(restService).get();

        String imagePath = (String) JsonPath.read(jsonResult, "$['profile_path']");
        if (imagePath == null) return null;

        imagePath = this.reader.getPosterEndPoint(imagePath);
        String name = JsonPath.read(jsonResult, "$['name']");
        return new Actor(actorId, name, imagePath);
    }

    @NotNull
    private List<String> getImages(String actorId) throws ExecutionException, InterruptedException {
        restService.setEndPointURL(this.reader.getActorImagesEndPoint(actorId));
        String jsonResult = executor.submit(restService).get();
        List<String> images = new ArrayList<>();
        ArrayList<HashMap<String, Object>> imagePaths = JsonPath.read(jsonResult, "$.profiles");
        for (int i = 0; i < imagePaths.size(); i++) {
            images.add(this.reader.getPosterEndPoint((String) imagePaths.get(i).get("file_path")));
        }
        return images;
    }

    public LiveData<List<Actor>> getActors(String movieId) {
        List<Actor> actorList = new ArrayList<>(10);
        try {
            restService.setEndPointURL(this.reader.getCreditsEndPoint(movieId));
            String jsonResult = executor.submit(restService).get();

            Integer statusCode = JsonPath.using((Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))).parse(jsonResult).read("status_code");
            if (statusCode != null)
                return null;

            ArrayList<HashMap<String, Object>> credits = JsonPath.read(jsonResult, "$.cast");
            if (credits == null)
                return null;

            for (int i = 0; i < credits.size(); i++) {
                String posterPath = (String) credits.get(i).get("profile_path");
                if (posterPath == null) continue;
                else posterPath = this.reader.getPosterEndPoint(posterPath);

                String name = (String) credits.get(i).get("name");
                int id = (int) credits.get(i).get("id");
                actorList.add(new Actor(String.valueOf(id), name, posterPath));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>(actorList);
    }

    @Override
    protected void finalize() throws Throwable {
        executor.shutdown();
        super.finalize();
    }
}
