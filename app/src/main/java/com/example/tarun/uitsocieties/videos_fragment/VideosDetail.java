package com.example.tarun.uitsocieties.videos_fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.example.tarun.uitsocieties.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideosDetail extends AppCompatActivity implements EasyVideoCallback {

    EasyVideoPlayer easyVideoPlayer;
    ArrayList<VideoParcel> videoParcel;
    VideoParcel curr_video;
    int pos;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_detail);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//      TODO -> ADD AUDIO FOCUS

        Intent in = getIntent();
        //  TODO --> HANDLE pos = -1
        pos = in.getIntExtra("position",-1);
        videoParcel = in.getParcelableArrayListExtra("video_parcel");
        curr_video = videoParcel.get(pos);

        error = (TextView) findViewById(R.id.easy_player_error);
        error.setVisibility(GONE);
        easyVideoPlayer = (EasyVideoPlayer) findViewById(R.id.easy_vplayer);

        easyVideoPlayer.setCallback(this);
        easyVideoPlayer.setSource(Uri.parse(curr_video.getSource_url()));
        easyVideoPlayer.setAutoPlay(true);
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        player.pause();
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        easyVideoPlayer.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        Log.v("EasyPlayer Excep---",e.toString());
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

}
