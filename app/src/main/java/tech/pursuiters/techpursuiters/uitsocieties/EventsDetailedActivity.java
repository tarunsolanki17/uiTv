package tech.pursuiters.techpursuiters.uitsocieties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tech.pursuiters.techpursuiters.uitsocieties.events_fragment.EventsDeatiledPhotoActivity;

import java.util.ArrayList;

import static android.view.View.GONE;

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

        cover = (ImageView) findViewById(R.id.cover);
        name = (TextView) findViewById(R.id.event_name);
        date_time = (TextView) findViewById(R.id.date_time);
        venue = (TextView) findViewById(R.id.venue);
        descp = (TextView) findViewById(R.id.descp);

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


        if(curr_event.getCover_url().isEmpty())
            cover.setVisibility(GONE);
        else
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
        venue.setText(curr_event.getPlace_name() + ", " + curr_event.getCity());
        if(curr_event.getDescp().isEmpty())
            descp.setText("No Description Available.");
        else
        descp.setText(curr_event.getDescp());

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo_detail = new Intent(getApplicationContext(), EventsDeatiledPhotoActivity.class);
                photo_detail.putExtra("cover_url",curr_event.getCover_url());
                startActivity(photo_detail);
            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map_intent = new Intent(Intent.ACTION_VIEW);
                map_intent.setData(Uri.parse("geo:"+curr_event.getLatitude()+", "+curr_event.getLongitude()));
                if((map_intent.resolveActivity(getPackageManager())!=null)){
                    startActivity(map_intent);
                }
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
