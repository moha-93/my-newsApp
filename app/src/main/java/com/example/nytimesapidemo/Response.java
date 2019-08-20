package com.example.nytimesapidemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("results")
    @Expose
    public List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
