package com.example.tarun.uitsocieties;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

import static com.example.tarun.uitsocieties.InClub.viewpgr;

/**
 * Created by Tarun on 09-Oct-17.
 */

public class EventsDetailedActivity extends AppCompatActivity {

    private int position;
    private ArrayList<EventsDataModel> detailedInfo;
    ImageView cover;
    TextView name,date_time,venue,descp;
    Button button;
    private AsyncTask coverAsync;
    Bitmap image;
    private EventsDataModel curr_event;
    private Bundle new_event_bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_detailed);

        Intent receivedIntent = getIntent();
        boolean notif = false;
        if(receivedIntent.getBooleanExtra("Notification",notif)){
//            new_event_bundle = receivedIntent.getBundleExtra("new_event_model");
            //  TODO --> NULL ERROR OF PARCELABLE
//            curr_event = new_event_bundle.getParcelable("new_event");
            curr_event = receivedIntent.getParcelableExtra("New Event Parcel");
        }
        else{
            detailedInfo = receivedIntent.getParcelableArrayListExtra("events_parcel");
            position = receivedIntent.getIntExtra("Event Position",-1);
            curr_event = detailedInfo.get(position);
        }

        //  TODO --> ATTACH AN ONCLICKLISTENER TO THE IMAGE VIEW FOR LEADING TO DETAILED ACTIVITY WITH ZOOMING AND SAVING OPTIONS
        //  TODO --> ATTACH INTENT TO FACEBOOK LINK
        //  TODO --> ATTACH INTENT TO SHOW IN MAP

        cover = (ImageView) findViewById(R.id.cover);
        name = (TextView) findViewById(R.id.event_name);
        date_time = (TextView) findViewById(R.id.date_time);
        venue = (TextView) findViewById(R.id.venue);
        descp = (TextView) findViewById(R.id.descp);
        button = (Button) findViewById(R.id.map);

        coverAsync = new AsyncTask() {
            @Override
            protected Bitmap doInBackground(Object[] objects) {
                try{

                    URL cover_url = new URL(curr_event.getCover_url());
                    image = BitmapFactory.decodeStream(cover_url.openConnection().getInputStream());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                cover.setImageBitmap(image);
            }
        };
        coverAsync.execute();

        name.setText(curr_event.getEvent_name());
        String date = curr_event.getDate();
        String mon = curr_event.getMonth();
        String year = curr_event.getYear();
        String time = curr_event.getTime();
        String date_text = "On " + date + " " + mon + " " + year + " at " + time;
        date_time.setText(date_text);
        venue.setText("Venue: " + curr_event.getPlace_name() + ", " + curr_event.getCity());
        descp.setText(curr_event.getDescp());

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO --> DETAILED IMAGE VIEW WITH SAVE AND SHARE OPTIONS AS WELL
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  TODO --> MOVING TO THE MAP WITH LATITUDE AND LONGITUDE
                Intent map_intent = new Intent(Intent.ACTION_VIEW);
                map_intent.setData(Uri.parse("geo:"+curr_event.getLatitude()+", "+curr_event.getLongitude()));
                if((map_intent.resolveActivity(getPackageManager())!=null)){
                    startActivity(map_intent);
                }
            }
        });
    }
}
