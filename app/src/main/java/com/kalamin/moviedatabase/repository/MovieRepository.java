package com.kalamin.moviedatabase.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.kalamin.moviedatabase.model.entity.Movie;
import com.kalamin.moviedatabase.model.entity.MovieDetails;
import com.kalamin.moviedatabase.model.remote.MoviesRestService;
import com.kalamin.moviedatabase.utils.Reader;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieRepository {
    private static MovieRepository instance;
    private Reader reader;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private MoviesRestService restService;

    public synchronized static MovieRepository getInstance() {
        if (instance == null)
            instance = new MovieRepository();
        return instance;
    }

    private MovieRepository() {
        this.reader = Reader.getInstance();
        restService = new MoviesRestService();
    }

    public LiveData<List<Movie>> discoverMovies() throws ExecutionException, InterruptedException {
        restService.setEndPointURL(this.reader.getDiscoverMoviesEndPoint(1));
        String jsonResult = executor.submit(restService).get();

        return new MutableLiveData<>(readJson(jsonResult));
    }

    public LiveData<Movie> getMovie(@NotNull String id) {
        try {
            restService.setEndPointURL(this.reader.getMovieDetailsEndPoint(id));
            String jsonResult = executor.submit(restService).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

            return new MutableLiveData<>(readJson(document));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>(new Movie());
    }

    @SuppressLint("SimpleDateFormat")
    public LiveData<MovieDetails> getMovieDetails(String id) {
        try {
            restService.setEndPointURL(this.reader.getMovieDetailsEndPoint(id));
            String jsonResult = executor.submit(restService).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

            Integer statusCode = JsonPath.using((Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))).parse(jsonResult).read("status_code");
            if (statusCode != null)
                return null;

            int movieId = JsonPath.read(document, "$['id']");
            String title = JsonPath.read(document, "$['title']");
            String overview = JsonPath.read(document, "$['overview']");
            String posterPath = (String) JsonPath.read(document, "$['backdrop_path']");
            if (posterPath != null)
                posterPath = this.reader.getPosterEndPoint(posterPath);

            DateFormat localFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            SimpleDateFormat restDateFormat = new SimpleDateFormat("YYYY-mm-dd");
            Date date = restDateFormat.parse((String) (JsonPath.read(document, "$['release_date']")));
            assert date != null;
            String releaseDate = localFormat.format(date);
            double averageVote = ((Number) ((JsonPath.read(document, "$['vote_average']") == null) ? 0 : JsonPath.read(document, "$['vote_average']"))).doubleValue();

            int runtime = (int) ((JsonPath.read(document, "$['runtime']") == null) ? 0 : JsonPath.read(document, "$['runtime']"));

            double popularity = ((Number) ((JsonPath.read(document, "$['popularity']") == null) ? 0 : JsonPath.read(document, "$['popularity']"))).doubleValue();
            return new MutableLiveData<>(new MovieDetails(movieId, title, overview, posterPath, releaseDate, averageVote, runtime, popularity));

        } catch (InterruptedException | ExecutionException | ParseException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>(new MovieDetails());
    }

    public LiveData<List<Movie>> searchMovies(String movieName) throws ExecutionException, InterruptedException {
        restService.setEndPointURL(this.reader.getSearchMovieEndPoint(movieName));
        String jsonResult = executor.submit(restService).get();

        return new MutableLiveData<>(readJson(jsonResult));
    }

    @NotNull
    @SuppressWarnings("ConstantConditions")
    private List<Movie> readJson(String json) {
        List<Movie> movieList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> movies = JsonPath.read(json, "$.results");
        for (int i = 0; i < movies.size(); i++) {
            String posterPath = (String) movies.get(i).get("poster_path");
            if (posterPath == null) continue;

            movieList.add(new Movie(
                    String.valueOf(movies.get(i).get("id")),
                    (String) movies.get(i).get("title"),
                    this.reader.getPosterEndPoint(posterPath),
                    ((Number) movies.get(i).get("vote_average")).doubleValue()
            ));
        }
        return movieList;
    }

    @NotNull
    private Movie readJson(@NotNull Object document) {
        int movieId = JsonPath.read(document, "$['id']");
        String title = JsonPath.read(document, "$['title']");
        String posterPath = this.reader.getPosterEndPoint((String) JsonPath.read(document, "$['backdrop_path']"));
        double averageVote = ((Number) ((JsonPath.read(document, "$['vote_average']") == null) ? 0 : JsonPath.read(document, "$['vote_average']"))).doubleValue();

        return new Movie(String.valueOf(movieId), title, posterPath, averageVote);
    }

    @Override
    protected void finalize() throws Throwable {
        executor.shutdown();
        super.finalize();
    }
}
