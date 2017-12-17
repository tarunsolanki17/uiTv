package com.example.tarun.uitsocieties;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tarun.uitsocieties.events_fragment.EventsDeatiledPhotoActivity;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.example.tarun.uitsocieties.InClub.event_detail;
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
    private String curr_club_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_detailed);

        event_detail = true;

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
            curr_club_id = receivedIntent.getStringExtra("Club ID");
            position = receivedIntent.getIntExtra("Event Position",-1);
            curr_event = detailedInfo.get(position);
        }

        //  TODO --> ATTACH AN ONCLICKLISTENER TO THE IMAGE VIEW FOR LEADING TO DETAILED ACTIVITY WITH ZOOMING AND SAVING OPTIONS
        //  TODO --> ATTACH INTENT TO FACEBOOK LINK

        cover = (ImageView) findViewById(R.id.cover);
        name = (TextView) findViewById(R.id.event_name);
        date_time = (TextView) findViewById(R.id.date_time);
        venue = (TextView) findViewById(R.id.venue);
        descp = (TextView) findViewById(R.id.descp);
        button = (Button) findViewById(R.id.map);

        if(curr_event.getCover_url().isEmpty())
            cover.setVisibility(GONE);
        Glide.with(getApplicationContext())
                .load(curr_event.getCover_url())
                .into(cover);

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
                Intent photo_detail = new Intent(getApplicationContext(), EventsDeatiledPhotoActivity.class);
                photo_detail.putExtra("cover_url",curr_event.getCover_url());
                startActivity(photo_detail);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map_intent = new Intent(Intent.ACTION_VIEW);
                map_intent.setData(Uri.parse("geo:"+curr_event.getLatitude()+", "+curr_event.getLongitude()));
                if((map_intent.resolveActivity(getPackageManager())!=null)){
                    startActivity(map_intent);
                }
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(InClub);
        }
    }*/
}
