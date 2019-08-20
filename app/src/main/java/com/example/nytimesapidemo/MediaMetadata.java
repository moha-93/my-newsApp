package com.example.nytimesapidemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaMetadata implements Parcelable {

    @SerializedName("url")
    @Expose
    private String imgUrl;

    @SerializedName("width")
    @Expose
    private Integer width;

    @SerializedName("height")
    @Expose
    private Integer height;



    protected MediaMetadata(Parcel in) {
        imgUrl = in.readString();
    }

    public static final Creator<MediaMetadata> CREATOR = new Creator<MediaMetadata>() {
        @Override
        public MediaMetadata createFromParcel(Parcel in) {
            return new MediaMetadata(in);
        }

        @Override
        public MediaMetadata[] newArray(int size) {
            return new MediaMetadata[size];
        }
    };

    public MediaMetadata() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeInt(width);
    }

    public String getImgUrl() {
        return  imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
