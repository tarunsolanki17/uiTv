package com.example.tarun.uitsocieties;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tarun on 15-Sep-17.
 */

public class PhotoParcel implements Parcelable {

    private String caption;
    private String thumb_url;
    private String full_url;
    private Long date;
    private int photo_count;

    public PhotoParcel(String caption, String thumb_url, String full_url, Long date, int photo_count) {
        this.caption = caption;
        this.thumb_url = thumb_url;
        this.full_url = full_url;
        this.date = date;
        this.photo_count = photo_count;
    }

    protected PhotoParcel(Parcel in){
        caption = in.readString();
        thumb_url = in.readString();
        full_url = in.readString();
        date = in.readLong();
        photo_count = in.readInt();
    }
    public static final Creator<PhotoParcel> CREATOR = new Creator<PhotoParcel>(){
        @Override
        public PhotoParcel createFromParcel(Parcel in) {
            return new PhotoParcel(in);
        }

        @Override
        public PhotoParcel[] newArray(int i) {
            return new PhotoParcel[0];
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(caption);
        parcel.writeString(thumb_url);
        parcel.writeString(full_url);
        parcel.writeLong(date);
        parcel.writeInt(photo_count);
    }
}
