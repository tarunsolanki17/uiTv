package com.example.tarun.uitsocieties.videos_fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.tarun.uitsocieties.R;

import java.util.ArrayList;

public class VideosDetail extends Activity {

    VideoView videoView;
    MediaController mediaController;
    ArrayList<VideoParcel> videoParcel;
    VideoParcel curr_video;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_detail);
//        setTheme(R.style.AppTheme);

        Intent in = getIntent();
        //  TODO --> HANDLE pos = -1
        pos = in.getIntExtra("position",-1);
        videoParcel = in.getParcelableArrayListExtra("video_parcel");
        curr_video = videoParcel.get(pos);

        videoView = (VideoView) findViewById(R.id.video);

        if(mediaController==null){
            mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
        }

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(curr_video.getSource_url()));
        videoView.start();
    }
}
