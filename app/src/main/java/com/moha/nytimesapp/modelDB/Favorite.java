package com.moha.nytimesapp.modelDB;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites_table")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "imgUrl")
    public String imgUrl;
    @ColumnInfo(name = "headline")
    public String headline;
    @ColumnInfo(name = "summary")
    public String summary;
    @ColumnInfo(name = "publishDate")
    public String publishDate;
    @ColumnInfo(name = "webUrl")
    public String webUrl;

    public Favorite(String title, String description, String publishDate) {
        this.headline = title;
        this.summary = description;
        this.publishDate = publishDate;
    }

    public Favorite(String imgUrl, String headline, String summary, String publishDate) {
        this.imgUrl = imgUrl;
        this.headline = headline;
        this.summary = summary;
        this.publishDate = publishDate;
    }

    public Favorite() {

    }



    public String getSummary() {
        return summary;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getHeadline() {
        return headline;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
