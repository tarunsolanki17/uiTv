package com.example.tarun.uitsocieties.videos_fragment;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tarun.uitsocieties.PhotoParcel;

/**
 * Created by Tarun on 27-Oct-17.
 */

public class VideoParcel implements Parcelable{

    private String id;
    private String created_time;
    private String title;
    private String descp;
    private int length;          //  CAN BE INTEGER
    private String place;
    private String picture;
    private String source_url;
    private String thumbnail_url;

    public VideoParcel(String id, String created_time,  String descp, int length, String picture, String source_url, String thumbnail) {
        this.id = id;
        this.created_time = created_time;
        this.descp = descp;
        this.length = length;
        this.picture = picture;
        this.source_url = source_url;
        this.thumbnail_url = thumbnail;
    }

    protected VideoParcel(Parcel input){
        id = input.readString();
        created_time = input.readString();
        descp = input.readString();
        length = input.readInt();
        picture = input.readString();
        source_url = input.readString();
        thumbnail_url = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(created_time);
        parcel.writeString(descp);
        parcel.writeInt(length);
        parcel.writeString(picture);
        parcel.writeString(source_url);
        parcel.writeString(thumbnail_url);
    }

    public static final Creator<VideoParcel> CREATOR = new Creator<VideoParcel>(){
        @Override
        public VideoParcel createFromParcel(Parcel in) {
            return new VideoParcel(in);
        }

        @Override
        public VideoParcel[] newArray(int size) {
            return new VideoParcel[size];
        }
    };

    public String getDescp() {
        return descp;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}
