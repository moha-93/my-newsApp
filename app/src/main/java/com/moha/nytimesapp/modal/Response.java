package com.moha.nytimesapp.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.moha.nytimesapp.modal.Article;

import java.util.ArrayList;
import java.util.List;

public class Response {

    @SerializedName("results")
    @Expose
    public List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
