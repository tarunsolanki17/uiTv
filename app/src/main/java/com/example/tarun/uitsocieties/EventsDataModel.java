package com.example.tarun.uitsocieties;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Tarun on 16-Sep-17.
 */

public class EventsDataModel implements Parcelable{

    private String event_name;
    private String start_date;
    private String end_date;
    private String place_name;
    private String city;
    private Double latitude;
    private Double longitude;
    private String descp;
    private String cover_url;

    public EventsDataModel(String event_name, String start_date, String end_date, String place_name, String city, Double latitude, Double longitude, String descp, String cover_url) {
        this.event_name = event_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.place_name = place_name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.descp = descp;
        this.cover_url = cover_url;
    }
    public EventsDataModel(Parcel input){
        Log.v("Constructor args Par---","Running");
        event_name = input.readString();
        start_date = input.readString();
        end_date = input.readString();
        place_name = input.readString();
        city = input.readString();
        latitude = input.readDouble();
        longitude = input.readDouble();
        descp = input.readString();
        cover_url = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.v("writeToParcel---","Running");
        parcel.writeString(event_name);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeString(place_name);
        parcel.writeString(city);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(descp);
        parcel.writeString(cover_url);
    }
    public static final Parcelable.Creator<EventsDataModel> CREATOR = new Parcelable.Creator<EventsDataModel>(){
        @Override
        public EventsDataModel createFromParcel(Parcel in) {
            Log.v("createFromParcel---","Running");
            return new EventsDataModel(in);
        }

        @Override
        public EventsDataModel[] newArray(int size) {
            return new EventsDataModel[size];
        }
    };
}
