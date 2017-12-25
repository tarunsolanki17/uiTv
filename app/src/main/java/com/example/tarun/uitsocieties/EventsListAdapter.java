package com.example.tarun.uitsocieties;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.R.id.event_item;

/**
 * Created by Tarun on 16-Sep-17.
 */

public class EventsListAdapter extends ArrayAdapter<EventsDataModel> {

    private Context con;
    private ArrayList<EventsDataModel> events_data;
    private TextView name, month, date, day, time, place, header;
    private RelativeLayout event_item_rel;
    private int comparision;


    public EventsListAdapter(Context context, ArrayList<EventsDataModel> data) {
        super(context, 0, data);
        con = context;
        events_data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View eventListItemView;
        EventsDataModel curr_obj = getItem(position);

        eventListItemView = convertView;
        if(eventListItemView==null)
            eventListItemView = LayoutInflater.from(con).inflate(R.layout.event_list_item,parent,false);

        name = eventListItemView.findViewById(R.id.ename);
        month = eventListItemView.findViewById(R.id.emonth);
        date = eventListItemView.findViewById(R.id.edate);
        day = eventListItemView.findViewById(R.id.eday);
        time = eventListItemView.findViewById(R.id.etime);
        place = eventListItemView.findViewById(R.id.eplace);

        try {
            name.setText(curr_obj.getEvent_name());
            month.setText(curr_obj.getMonth());
            date.setText(curr_obj.getDate());
            day.setText(curr_obj.getDay());
            time.setText(curr_obj.getTime());
            place.setText(curr_obj.getPlace_name());

        }
        catch (Exception e){
            Log.v("Exception---"," In EventsListAdapter getView()---"+e.toString());
        }

        event_item_rel = eventListItemView.findViewById(event_item);
        event_item_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(con,EventsDetailedActivity.class);
                in.putParcelableArrayListExtra("events_parcel",events_data);
                in.putExtra("Event Position",position);
                in.putExtra("Club ID",club_id);
                con.startActivity(in);
            }
        });

        return eventListItemView;

        /*try {
            SimpleDateFormat format = new SimpleDateFormat(" EEEE, dd MMMM yyyy");
            Date event_date = format.parse(curr_obj.getStart_date());
            Date today = new Date();
            comparision = today.compareTo(event_date);
            Log.v("Comparision------", String.valueOf(comparision));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        View eventListItemView;

        if(comparision==-1&&!started1) {        *//**     UPCOMING*//*
            Log.v("Upcoming------",curr_obj.getEvent_name());
            list_item = false;
            eventListItemView = LayoutInflater.from(con).inflate(R.layout.event_header_item,parent,false);
            started1 = true;
            //      ASSIGN VALUES
            assignValues(eventListItemView,comparision,curr_obj, position);
        }

        else if(comparision==1&&!started2){     *//**     PAST    *//*
            Log.v("Past------",curr_obj.getEvent_name());
            list_item = false;
            eventListItemView = LayoutInflater.from(con).inflate(R.layout.event_header_item,parent,false);
            started2 = true;
            //      ASSIGN VALUES
            assignValues(eventListItemView,comparision,curr_obj, position);
        }

        else{
            Log.v("List Item------",curr_obj.getEvent_name());
            if(list_item){
                eventListItemView = convertView;
                if(eventListItemView==null){
                    eventListItemView = LayoutInflater.from(con).inflate(R.layout.event_list_item,parent,false);
                }
                assignValues(eventListItemView,0,curr_obj, position);
            }
            else{
                eventListItemView = LayoutInflater.from(con).inflate(R.layout.event_list_item,parent,false);
                assignValues(eventListItemView,0,curr_obj, position);
            }
            Log.v("View Type---",String.valueOf(getViewTypeCount()));
            list_item = true;
        }*/


    }

    /*private void assignValues(View eventItem, int comp, EventsDataModel curr_eve,final int pos){
        if(comp==-1){
            header = eventItem.findViewById(R.id.event_header);
            header.setVisibility(VISIBLE);
            header.setText("Upcoming Events");
        }
        if(comp==1){
            header = eventItem.findViewById(R.id.event_header);
            header.setVisibility(VISIBLE);
            header.setText("Past Events");
        }

        name = eventItem.findViewById(R.id.ename);
        month = eventItem.findViewById(R.id.emonth);
        date = eventItem.findViewById(R.id.edate);
        day = eventItem.findViewById(R.id.eday);
        time = eventItem.findViewById(R.id.etime);
        place = eventItem.findViewById(R.id.eplace);

        try {
            name.setText(curr_eve.getEvent_name());
            month.setText(curr_eve.getMonth());
            date.setText(curr_eve.getDate());
            day.setText(curr_eve.getDay());
            time.setText(curr_eve.getTime());
            place.setText(curr_eve.getPlace_name());

        }
        catch (Exception e){
            Log.v("Exception---"," In EventsListAdapter getView()---"+e.toString());
        }

        event_item_rel = eventItem.findViewById(event_item);
        event_item_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(con,EventsDetailedActivity.class);
                in.putParcelableArrayListExtra("events_parcel",events_data);
                in.putExtra("Event Position",pos);
                in.putExtra("Club ID",club_id);
                con.startActivity(in);
            }
        });
    }*/
}
