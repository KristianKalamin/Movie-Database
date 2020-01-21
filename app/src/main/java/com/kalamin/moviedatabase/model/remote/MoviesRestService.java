package com.kalamin.moviedatabase.model.remote;

import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesRestService extends AsyncTask<String, Void, String> {

    private OkHttpClient client;

    public MoviesRestService() {
        client = new OkHttpClient();
    }

    @Override
    protected String doInBackground(@NotNull String... strings) {
        try {
            return callRest(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private String callRest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
