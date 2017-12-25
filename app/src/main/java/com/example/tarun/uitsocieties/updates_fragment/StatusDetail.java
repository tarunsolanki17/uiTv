package com.example.tarun.uitsocieties.updates_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tarun.uitsocieties.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.STATUS_DATA;

/**
 * Created by Tarun on 24-Dec-17.
 */

public class StatusDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_detail);

        Intent received = getIntent();
        UpdateParcel curr_status = received.getParcelableExtra(STATUS_DATA);

        TextView descp = (TextView) findViewById(R.id.descp_status);
        TextView time = (TextView) findViewById(R.id.date_time_status);

        descp.setText(curr_status.getMessage());

        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date statusDate = incoming.parse(curr_status.getCreated_time());
            if(statusDate!=null){
                time.setText(new SimpleDateFormat(" EEEE, dd MMMM yyyy").format(statusDate));
            }
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
