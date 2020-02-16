package com.kalamin.moviedatabase.repository;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.kalamin.moviedatabase.model.entity.Actor;
import com.kalamin.moviedatabase.model.entity.ActorDetails;
import com.kalamin.moviedatabase.model.remote.MoviesRestService;
import com.kalamin.moviedatabase.utils.Reader;

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

public class ActorsRepository {
    private static ActorsRepository instance;
    private Reader reader;

    public synchronized static ActorsRepository getInstance(Context applicationContext) {
        if (instance == null)
            instance = new ActorsRepository(applicationContext);
        return instance;
    }

    private ActorsRepository(Context applicationContext) {
        this.reader = Reader.getInstance(applicationContext);
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SimpleDateFormat")
    public ActorDetails getActorDetails(String id) {
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getActorEndPoint(id)).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

            DateFormat localFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            SimpleDateFormat restDateFormat = new SimpleDateFormat("YYYY-mm-dd");
            Date date = restDateFormat.parse((String) (JsonPath.read(document, "$['birthday']")));

            Date today = Calendar.getInstance().getTime();
            long diff = Math.abs(today.getTime() - date.getTime());

            String age = Long.toString(diff / 31556952000L);

            String birthday = localFormat.format(date);

            String deathday = JsonPath.read(document, "$['deathday']");
            if (deathday != null) {
                date = restDateFormat.parse(deathday);
                assert date != null;
                deathday = localFormat.format(date);
            }

            String name = JsonPath.read(document, "$['name']");
            String bio = JsonPath.read(document, "$['biography']");
            String placeOfBirth = JsonPath.read(document, "$['place_of_birth']");
            String imagePath = (String) JsonPath.read(document, "$['profile_path']");
            imagePath = this.reader.getPosterEndPoint(imagePath);

            double popularity = ((Number) ((JsonPath.read(document, "$['popularity']") == null) ? 0 : JsonPath.read(document, "$['popularity']"))).doubleValue();

            return new ActorDetails(name, birthday, deathday, bio, placeOfBirth, imagePath, popularity, age, getImages(id));
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Actor> searchActors(String actorName) {
        List<Actor> searchResult = new ArrayList<>();
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getSearchActorsEndPoint(actorName)).get();
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
        return searchResult;
    }

    @Nullable
    private Actor getActor(String actorId) throws ExecutionException, InterruptedException {
        String jsonResult = new MoviesRestService().execute(this.reader.getActorEndPoint(actorId)).get();

        String imagePath = (String) JsonPath.read(jsonResult, "$['profile_path']");
        if (imagePath == null) return null;

        imagePath = this.reader.getPosterEndPoint(imagePath);
        String name = JsonPath.read(jsonResult, "$['name']");
        return new Actor(actorId, name, imagePath);
    }

    private List<String> getImages(String actorId) throws ExecutionException, InterruptedException {
        String jsonResult = new MoviesRestService().execute(this.reader.getActorImagesEndPoint(actorId)).get();
        List<String> images = new ArrayList<>();
        ArrayList<HashMap<String, Object>> imagePaths = JsonPath.read(jsonResult, "$.profiles");
        for (int i = 0; i < imagePaths.size(); i++) {
            images.add(this.reader.getPosterEndPoint((String) imagePaths.get(i).get("file_path")));
        }
        return images;
    }

    public List<Actor> getActors(String movieId) {
        List<Actor> actorList = new ArrayList<>(3);
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getCreditsEndPoint(movieId)).get();
            ArrayList<HashMap<String, Object>> credits = JsonPath.read(jsonResult, "$.cast");

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
        return actorList;
    }
}
