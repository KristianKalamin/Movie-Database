package com.kalamin.moviedatabase.model.remote;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesRestService implements Callable<String> {

    private OkHttpClient client;
    private String url;

    public MoviesRestService() {
        client = new OkHttpClient();
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

    public void setEndPointURL(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        return callRest(this.url);
    }
}
