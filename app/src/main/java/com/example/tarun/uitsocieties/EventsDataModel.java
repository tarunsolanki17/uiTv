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
    private String updated_date;
    private String year;
    private String month;
    private String date;
    private String day;
    private String time;
    private String place_name;
    private String city;
    private Double latitude;
    private Double longitude;
    private String descp;
    private String cover_url;
    //  TODO --> ADD THE FACEBOOK LINK TO THE URL
    private String facebook_link;

    public EventsDataModel(String event_name, String start_date, String end_date, String year, String month, String date, String day, String time, String place_name, String city, Double latitude, Double longitude, String descp, String cover_url) {
        this.event_name = event_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
        this.time = time;
        this.place_name = place_name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.descp = descp;
        this.cover_url = cover_url;
    }

    public EventsDataModel(Parcel input){
        event_name = input.readString();
        start_date = input.readString();
        end_date = input.readString();
        year = input.readString();
        month = input.readString();
        date = input.readString();
        day = input.readString();
        time = input.readString();
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
        parcel.writeString(event_name);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeString(year);
        parcel.writeString(month);
        parcel.writeString(date);
        parcel.writeString(day);
        parcel.writeString(time);
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
            return new EventsDataModel(in);
        }

        @Override
        public EventsDataModel[] newArray(int size) {
            return new EventsDataModel[size];
        }
    };

    public String getEvent_name() {
        return event_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescp() {
        return descp;
    }

    public String getCover_url() {
        return cover_url;
    }

}
