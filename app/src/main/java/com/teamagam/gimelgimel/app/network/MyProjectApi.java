package com.teamagam.gimelgimel.app.network;

import com.squareup.okhttp.OkHttpClient;
import com.teamagam.gimelgimel.app.network.services.GGService;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Entry point for all requests to **My Project** API.
 * Uses Retrofit library to abstract the actual REST API into a service.
 */
public class MyProjectApi {

    private static MyProjectApi instance;
    private GGService searchService;

    /**
     * Returns the instance of this singleton.
     */
    public static MyProjectApi getInstance() {
        if (instance == null) {
            instance = new MyProjectApi();
        }
        return instance;
    }

    /**
     * Private singleton constructor.
     */
    private MyProjectApi() {
        RestAdapter restAdapter = buildRestAdapter();
        this.searchService = restAdapter.create(GGService.class);
    }

    /**
     * Creates the RestAdapter by setting custom HttpClient.
     */
    private RestAdapter buildRestAdapter() {
        return new RestAdapter.Builder()
            .setEndpoint(ApiConstants.BASE_URL)
            // Out-comment the following line if you want to use the default converter Gson.
            .setConverter(new JacksonConverter())
            .setClient(getHttpClient())
            .build();
    }

    /**
     * Custom Http Client to define connection timeouts.
     */
    private Client getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(ApiConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(ApiConstants.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        return new OkClient(httpClient);
    }

    /**
     * Does a text search for a given query, and returns the results.
     *
     * @param query the query string
     * @return the results
     */
    public Object getSearchEngineResults(String query) {
        //TODO: clean
//        return this.searchService.search(query);
        return null;
    }
}
