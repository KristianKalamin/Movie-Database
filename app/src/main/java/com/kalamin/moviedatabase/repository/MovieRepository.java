package com.kalamin.moviedatabase.repository;

import android.annotation.SuppressLint;
import android.content.Context;

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

public class MovieRepository {
    private static MovieRepository instance;
    private Reader reader;

    public synchronized static MovieRepository getInstance(Context applicationContext) {
        if (instance == null)
            instance = new MovieRepository(applicationContext);
        return instance;
    }

    private MovieRepository(Context applicationContext) {
        this.reader = Reader.getInstance(applicationContext);
    }

    public List<Movie> discoverMovies() throws ExecutionException, InterruptedException {
        String jsonResult = new MoviesRestService().execute(this.reader.getDiscoverMoviesEndPoint(1)).get();

        return readJson(jsonResult);
    }

    public Movie getMovie(@NotNull String id) {
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getMovieDetailsEndPoint(id)).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

            return readJson(document);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public MovieDetails getMovieDetails(String id) {
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getMovieDetailsEndPoint(id)).get();
            Object document = Configuration.defaultConfiguration()
                    .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .jsonProvider()
                    .parse(jsonResult);

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
            return new MovieDetails(movieId, title, overview, posterPath, releaseDate, averageVote, runtime, popularity);

        } catch (InterruptedException | ExecutionException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Movie> searchMovies(String movieName) throws ExecutionException, InterruptedException {
        String jsonResult = new MoviesRestService().execute(this.reader.getSearchMovieEndPoint(movieName)).get();

        return readJson(jsonResult);
    }

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
}
