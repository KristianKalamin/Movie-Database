package com.kalamin.moviedatabase.repository;

import android.content.Context;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public MovieDetails getMovieDetails(int id) {
        try {
            String jsonResult = new MoviesRestService().execute(this.reader.getMovieDetailsEndPoint(String.valueOf(id))).get();
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

            String releaseDate = JsonPath.read(document, "$['release_date']");
            double averageVote = ((Number) ((JsonPath.read(document, "$['vote_average']") == null) ? 0 : JsonPath.read(document, "$['vote_average']"))).doubleValue();

            int runtime = (int) ((JsonPath.read(document, "$['runtime']") == null) ? 0 : JsonPath.read(document, "$['runtime']"));

            double popularity = ((Number) ((JsonPath.read(document, "$['popularity']") == null) ? 0 : JsonPath.read(document, "$['popularity']"))).doubleValue();
            return new MovieDetails(movieId, title, overview, posterPath, releaseDate, averageVote, runtime, popularity);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    public LiveData<List<Movie>> searchMovies(String movieName) throws ExecutionException, InterruptedException {
        MutableLiveData<List<Movie>> listLiveData = new MutableLiveData<>();
        String jsonResult = new MoviesRestService().execute(this.reader.getSearchMovieEndPoint(movieName)).get();
        listLiveData.postValue(readJson(jsonResult));

        return listLiveData;
    }

    @SuppressWarnings("ConstantConditions")
    private List<Movie> readJson(String json) {
        List<Movie> movieList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> movies = JsonPath.read(json, "$.results");
        for (int i = 0; i < movies.size(); i++) {
            String posterPath = (String) movies.get(i).get("poster_path");
            if (posterPath == null) continue;

            movieList.add(new Movie(
                    (int) movies.get(i).get("id"),
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

        return new Movie(movieId, title, posterPath, averageVote);
    }
}
