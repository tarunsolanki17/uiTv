package com.example.tarun.uitsocieties;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.data;
import static android.R.attr.end;
import static android.R.attr.finishOnCloseSystemDialogs;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
import static com.example.tarun.uitsocieties.InClub.login;
import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.R.drawable.c;
import static com.example.tarun.uitsocieties.R.id.photo_recyc_view;
import static com.facebook.login.widget.ProfilePictureView.TAG;
import static java.util.Objects.isNull;

import com.example.tarun.uitsocieties.ClubContract.EventsConstants;
import com.facebook.Profile;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFrag extends Fragment {

    ArrayList<EventsDataModel> events_data = new ArrayList<>();
    EventsListAdapter eventsAdapter;
    ListView listView;
    ProgressBar pbar;
    TextView no_internet;
    TextView no_data;
    SwipeRefreshLayout swipe;
    boolean isConnected;

    public EventsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View events_view = inflater.inflate(R.layout.fragment_events, container, false);

        listView = events_view.findViewById(R.id.listview);
        pbar = events_view.findViewById(R.id.progress_bar);
        no_internet = events_view.findViewById(R.id.no_internet);
        no_data = events_view.findViewById(R.id.no_data);
        swipe = events_view.findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                if(isConnectedFunc())
                    eventJSONRequest();
                swipe.setRefreshing(false);
            }
        });

//        if(isConnectedFunc()){
//
//        }
//        else
            if(isConnectedFunc()){

            if(savedInstanceState!=null&&savedInstanceState.getInt("Login")==1)
                savedInstanceState = null;

            if (savedInstanceState != null) {
                events_data = savedInstanceState.getParcelableArrayList("events_parcel");
                if(events_data.isEmpty()||events_data==null){
                    //  TODO --> REMOVE NULL POINTER EXCEPTION IN isEmpty()
                    pbar.setVisibility(GONE);
                    listView.setVisibility(GONE);
                    no_data.setVisibility(VISIBLE);
                }
                else{
                    pbar.setVisibility(GONE);
                    no_data.setVisibility(GONE);
                    listView.setVisibility(VISIBLE);
                    onDataFetched();
                }

            }
            else {
                login_checker();
                if(login) {
                    eventJSONRequest();
                }
            }
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
                        onDataFetched();
                        //  TODO --> HANDLE RESPONSE CODES
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
                    String month = "";
                    String date = "";
                    String day = "";
                    String time = "";
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

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(start_date);

                        start_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                        month = new SimpleDateFormat("MMM").format(d);
                        date = new SimpleDateFormat("dd").format(d);
                        day = new SimpleDateFormat("EEE").format(d);
                        time = new SimpleDateFormat("h:mm a").format(d);

                    }
                    if(curr_event.has(END_TIME)&&!curr_event.isNull(END_TIME)){
                        end_date = curr_event.getString(END_TIME);

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(end_date);

                        end_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                    }
                    Log.v("name---", event_name);

                    if(curr_event.has(PLACE)&&!curr_event.isNull(PLACE)) {
                        JSONObject place_obj = curr_event.getJSONObject(PLACE);

                        if (place_obj.has(NAME) && !place_obj.isNull(NAME)) {
                            place_name = place_obj.getString(NAME);
                        }

                        if(place_obj.has(LOCATION)&&!place_obj.isNull(LOCATION)) {
                            JSONObject location = place_obj.getJSONObject(LOCATION);

                            if (location.has(CITY) && !location.isNull(CITY)) {
                                city = location.getString(CITY);
                            }
                            if (location.has(LATITUDE) && !location.isNull(LATITUDE)) {
                                latitude = location.getDouble(LATITUDE);
                            }
                            if (location.has(LONGITUDE) && !location.isNull(LONGITUDE)) {
                                longitude = location.getDouble(LONGITUDE);
                            }
                        }
                    }
                    if(curr_event.has(DESCRIPTION)&&!curr_event.isNull(DESCRIPTION)){
                        descp = curr_event.getString(DESCRIPTION);
                    }

                    if(curr_event.has(COVER)&&!curr_event.isNull(COVER)) {
                        JSONObject cover_obj = curr_event.getJSONObject(COVER);

                        if (cover_obj.has(SOURCE_URL) && !cover_obj.isNull(SOURCE_URL)) {
                            cover_source = cover_obj.getString(SOURCE_URL);
                        }
                    }
                    Log.v("N---","");

                    events.add(new EventsDataModel(event_name, start_date, end_date, month, date, day, time, place_name, city, latitude, longitude, descp, cover_source));

                }
                Toast.makeText(getContext(),data.length()+" events fetched",Toast.LENGTH_SHORT).show();
                if(data.length()==0){
                    no_internet.setVisibility(GONE);
                    pbar.setVisibility(GONE);
                    listView.setVisibility(GONE);
                    no_data.setVisibility(VISIBLE);
                }
                else{
                    no_internet.setVisibility(GONE);
                    pbar.setVisibility(GONE);
                    no_data.setVisibility(GONE);
                    listView.setVisibility(VISIBLE);
                }
            }
            catch (Exception e){
                Log.v("Exception---",e.toString());
            }
        }
    return events;
    }
    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnectedOrConnecting();

        if(!isConnected){
//            TODO --> IMAGE WITH TEXT IN NO INTERNET
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            listView.setVisibility(GONE);
            no_internet.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("events_parcel",events_data);
    }
    public void onDataFetched(){
        eventsAdapter = new EventsListAdapter(getContext(),events_data);
        listView.setAdapter(eventsAdapter);
    }

}
