package com.example.tarun.uitsocieties;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.data;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.CITY;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.COVER;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.DESCRIPTION;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.END_TIME;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LATITUDE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LOCATION;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LONGITUDE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.NAME;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.PLACE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.SOURCE_URL;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.START_TIME;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.R.drawable.c;
import static java.util.Objects.isNull;

import com.example.tarun.uitsocieties.ClubContract.EventsConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFrag extends Fragment {

    ArrayList<EventsDataModel> events_data = new ArrayList<>();

    public EventsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View events_view = inflater.inflate(R.layout.fragment_events, container, false);

        if(savedInstanceState!=null){
            events_data = savedInstanceState.getParcelableArrayList("events_parcel");
            Log.v("Condition---","true");
        }
        else{
            eventJSONRequest();
        }



        return events_view;
    }

    public void eventJSONRequest(){
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+ club_id +"/events",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        Log.v("JSON Response---",response.toString());
                        events_data = parseJSONData(response.getJSONObject());

//                        response.getResponseCode()
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,start_time,end_time,place,type,description,cover");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public ArrayList<EventsDataModel> parseJSONData(JSONObject response){
        ArrayList<EventsDataModel> events = new ArrayList<>();
        if(response!=null&&response.length()>0){
            try{
                JSONArray data = response.getJSONArray("data");
                for(int i=0;i<data.length();i++){
                    String event_name = "";
                    String start_date = "";
                    String end_date = "";
                    String place_name = "";
                    String city = "";
                    Double latitude = -1d;
                    Double longitude = -1d;
                    String descp = "";
                    String cover_source = "";

                    JSONObject curr_event = data.getJSONObject(i);

                    if(curr_event.has(NAME)&&!curr_event.isNull(NAME)){
                        event_name = curr_event.getString(NAME);
                    }
                    if(curr_event.has(START_TIME)&&!curr_event.isNull(START_TIME)){
                        start_date = curr_event.getString(START_TIME);
                    }
                    if(curr_event.has(END_TIME)&&!curr_event.isNull(END_TIME)){
                        end_date = curr_event.getString(END_TIME);
                    }
                    Log.v("name---", event_name);

                    JSONObject place_obj = curr_event.getJSONObject(PLACE);

                    if(place_obj.has(NAME)&&!place_obj.isNull(NAME)){
                        place_name = place_obj.getString(NAME);
                    }

                    JSONObject location = place_obj.getJSONObject(LOCATION);

                    if(location.has(CITY)&&!location.isNull(CITY)){
                        city = location.getString(CITY);
                    }
                    if(location.has(LATITUDE)&&!location.isNull(LATITUDE)){
                        latitude = location.getDouble(LATITUDE);
                    }
                    if(location.has(LONGITUDE)&&!location.isNull(LONGITUDE)){
                        longitude = location.getDouble(LONGITUDE);
                    }

                    if(curr_event.has(DESCRIPTION)&&!curr_event.isNull(DESCRIPTION)){
                        descp = curr_event.getString(DESCRIPTION);
                    }

                    JSONObject cover_obj = curr_event.getJSONObject(COVER);

                    if(cover_obj.has(SOURCE_URL)&&!cover_obj.isNull(SOURCE_URL)){
                        cover_source = cover_obj.getString(SOURCE_URL);
                    }
                    Log.v("N---","");

                    events.add(new EventsDataModel(event_name, start_date, end_date, place_name, city, latitude, longitude, descp, cover_source));

                }
                Toast.makeText(getContext(),data.length()+" events fetched",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Log.v("Exception---",e.toString());
            }
        }
    return events;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("events_parcel",events_data);
    }
}
