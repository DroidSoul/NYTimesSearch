package com.droidsoul.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bear&bear on 9/9/2017.
 */
public class Article {
    String webUrl;
    String headline;
    String thumbNail;
    boolean isTopStories;

    public boolean isTopStories() {
        return isTopStories;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article(JSONObject jsonObject, boolean isTopStories) throws JSONException {
        this.webUrl = isTopStories ? jsonObject.getString("url"):jsonObject.getString("web_url");
        this.headline = isTopStories ? jsonObject.getString("abstract"):jsonObject.getJSONObject("headline").getString("main");
        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if (multimedia.length() > 0) {
            JSONObject multimediaJson = multimedia.getJSONObject(0);
            this.thumbNail = isTopStories ? ("" + multimediaJson.get("url")) :("http://www.nytimes.com/" + multimediaJson.get("url"));
        } else {
            this.thumbNail = null;
        }
    }
    public static ArrayList<Article> fromJSONArray(JSONArray array, boolean isTopStories) {
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Article(array.getJSONObject(i), isTopStories));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
