package com.codepath.nytarticlesearchapp.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mallikaa on 11/16/16.
 */

public class Article {
    private String headline;
    private String image_url;
    private String article_url;
    private final static String base_url = "http://www.nytimes.com/";
    private final static String use_image_wide = "wide";

    public Article(JSONObject jsonObject) {
        try {
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.article_url = jsonObject.getString("web_url");
            this.image_url = jsonObject.getString("image_url");

        } catch (JSONException jsone) {
            Log.d("DEBUG", "Could not convert JSONObject to POJO");
        }
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getHeadline() {
        return headline;
    }

    public String getImage_url() {
        return image_url;
    }

    public static List<Article> getArticles(JSONArray jsonArray) {
        final Gson gson = new Gson();
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray imageArray = jsonObject.getJSONArray("multimedia");
                for (int j = 0; j < imageArray.length(); j++) {
                    JSONObject imageObject = imageArray.getJSONObject(j);
                    String imageUrl = imageObject.getString("subtype");
                    if (imageUrl.equals(use_image_wide)) {
                        jsonObject.put("image_url", base_url + imageObject.get("url"));
                        break;
                    }
                }
                if (!jsonObject.has("image_url")) {
                    jsonObject.put("image_url", null);
                }
                //Article article = gson.fromJson(jsonObject.toString(), Article.class);
                Article article = new Article(jsonObject);
                articles.add(article);
            } catch (JSONException jsone) {
                Log.d("DEBUG", "Could not extract object from jsonArray");
            }
        }
        return articles;
    }

}
