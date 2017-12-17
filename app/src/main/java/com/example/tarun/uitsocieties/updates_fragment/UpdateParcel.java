package com.example.tarun.uitsocieties.updates_fragment;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tarun.uitsocieties.EventsDataModel;

/**
 * Created by Tarun on 09-Dec-17.
 */

public class UpdateParcel implements Parcelable {

    private String id;
    private String caption;
    private String created_time;
    private String description;
    private String link;
    private String message;
    private String name;
    private String permalink_url;
    private String picture;
    private String full_picture;
    private String place_name;
    private String city;
    private String source;
    private String status_type;
    private String type;

    public UpdateParcel(String id, String caption, String created_time, String description, String link, String message, String name, String permalink_url, String picture, String full_picture, String place_name, String city, String source, String status_type, String type) {
        this.id = id;
        this.caption = caption;
        this.created_time = created_time;
        this.description = description;
        this.link = link;
        this.message = message;
        this.name = name;
        this.permalink_url = permalink_url;
        this.picture = picture;
        this.full_picture = full_picture;
        this.place_name = place_name;
        this.city = city;
        this.source = source;
        this.status_type = status_type;
        this.type = type;
    }

    public UpdateParcel(Parcel input){
        id = input.readString();
        caption = input.readString();
        created_time = input.readString();
        description = input.readString();
        link = input.readString();
        message = input.readString();
        name = input.readString();
        permalink_url = input.readString();
        picture = input.readString();
        full_picture = input.readString();
        place_name = input.readString();
        city = input.readString();
        source = input.readString();
        status_type = input.readString();
        type = input.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(caption);
        parcel.writeString(created_time);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(message);
        parcel.writeString(name);
        parcel.writeString(permalink_url);
        parcel.writeString(picture);
        parcel.writeString(full_picture);
        parcel.writeString(place_name);
        parcel.writeString(city);
        parcel.writeString(source);
        parcel.writeString(status_type);
        parcel.writeString(type);
    }

    public static final Parcelable.Creator<UpdateParcel> CREATOR = new Parcelable.Creator<UpdateParcel>(){
        @Override
        public UpdateParcel createFromParcel(Parcel in) {
            return new UpdateParcel(in);
        }

        @Override
        public UpdateParcel[] newArray(int size) {
            return new UpdateParcel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public String getPicture() {
        return picture;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getSource() {
        return source;
    }

    public String getStatus_type() {
        return status_type;
    }

    public String getType() {
        return type;
    }
}
