package tech.pursuiters.techpursuiters.uitsocieties.videos_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import tech.pursuiters.techpursuiters.uitsocieties.R;

/**
 * Created by Tarun on 20-Dec-17.
 */

public class VideosData extends AppCompatActivity {

    VideoParcel curr_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_data);

        Intent received = getIntent();
        curr_video = received.getParcelableExtra("Data");

        TextView descp, time, length;

        descp = (TextView) findViewById(R.id.descp_v);
        time = (TextView) findViewById(R.id.time_v);
        length = (TextView) findViewById(R.id.length_v);

        descp.setText(curr_video.getDescp());
        time.setText(curr_video.getCreated_time());
        length.setText(String.valueOf(curr_video.getLength()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
