package com.example.tarun.uitsocieties.photos_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tarun.uitsocieties.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.tarun.uitsocieties.ClubContract.PHOTO_DATA;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PHOTO_NAME;

/**
 * Created by Tarun on 22-Dec-17.
 */

public class PhotoData extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_data);

        Intent received = getIntent();
        String cap = received.getStringExtra(PHOTO_NAME);
        String cTime = received.getStringExtra(CREATED_TIME);

        TextView caption = (TextView) findViewById(R.id.descp_p);
        TextView time_view = (TextView) findViewById(R.id.time_p);

        if(cap.isEmpty())
            caption.setText("No Caption Available.");
        else
            caption.setText(cap);

        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date cDate;
        String time;
        try {
            cDate = incoming.parse(cTime);

            if(cDate!=null) {
                cTime = new SimpleDateFormat(" EEEE, dd MMMM yyyy").format(cDate);
            }

            time_view.setText(cTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
