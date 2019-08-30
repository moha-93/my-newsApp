package com.moha.nytimesapp.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article implements Parcelable {


    @SerializedName("url")
    @Expose
    private String webUrl;

    @SerializedName("title")
    @Expose
    public String headLine;

    @SerializedName("abstract")
    @Expose
    public String summary;

    @SerializedName("media")
    @Expose
    public List<Media> mediaList;

    @SerializedName("published_date")
    @Expose
    public String publishDate;

    public boolean isFavorite;


    public Article() {
    }

    protected Article(Parcel in) {
        webUrl = in.readString();
        headLine = in.readString();
        summary = in.readString();
        publishDate = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };


    public boolean isFavorite() {
        return isFavorite;
    }


    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getSummary() {
        return summary;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(headLine);
        dest.writeString(summary);
        dest.writeString(publishDate);
    }


}
