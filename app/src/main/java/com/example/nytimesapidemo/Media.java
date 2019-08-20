package com.example.nytimesapidemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Media implements Parcelable {

    @SerializedName("media-metadata")
    @Expose
    public List<MediaMetadata> metadata;

    protected Media(Parcel in) {
        metadata = in.createTypedArrayList(MediaMetadata.CREATOR);
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public Media() {

    }

    public List<MediaMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MediaMetadata> metadata) {
        this.metadata = metadata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(metadata);
    }
}
