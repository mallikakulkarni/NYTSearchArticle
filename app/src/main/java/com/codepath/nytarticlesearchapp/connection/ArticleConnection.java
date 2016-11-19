package com.codepath.nytarticlesearchapp.connection;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by mallikaa on 11/16/16.
 */

public class ArticleConnection {
    private final String base_url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final String api_query_param = "?api-key=";
    private final String api_key = "a9505a85500a4a95a1d2ca4ee6f84495";
    private final String query_string = "&q=";
    private OkHttpClient client;

    public ArticleConnection() {
        this.client = new OkHttpClient();
    }

    public void getArticles(String query, Callback callback) {
        String url = base_url + api_query_param + api_key + query;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

}
