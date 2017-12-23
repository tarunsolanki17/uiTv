package com.example.tarun.uitsocieties.videos_fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.example.tarun.uitsocieties.MainActivity;
import com.example.tarun.uitsocieties.R;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.sdk.InMobiSdk;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideosDetail extends AppCompatActivity implements EasyVideoCallback {

    EasyVideoPlayer easyVideoPlayer;
    ArrayList<VideoParcel> videoParcel;
    VideoParcel curr_video;
    int pos;
    TextView error;
    Toolbar toolbar;
    InMobiBanner banner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_detail);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar_v);
        setSupportActionBar(toolbar);

        InMobiSdk.init(VideosDetail.this, "6c2ca29688614264bd77f77cc38cd923");
        banner3 = (InMobiBanner) findViewById(R.id.banner3);
        banner3.load();
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

//      TODO -> ADD AUDIO FOCUS

        Intent in = getIntent();
        //  TODO --> HANDLE pos = -1
        pos = in.getIntExtra("position",-1);
        videoParcel = in.getParcelableArrayListExtra("video_parcel");
        curr_video = videoParcel.get(pos);

        error = (TextView) findViewById(R.id.easy_player_error);
        error.setVisibility(GONE);
        easyVideoPlayer = (EasyVideoPlayer) findViewById(R.id.easy_vplayer);

        easyVideoPlayer.setAutoFullscreen(true);

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
        easyVideoPlayer.setVisibility(VISIBLE);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.video_details :

                if(easyVideoPlayer.isPlaying())
                easyVideoPlayer.pause();
                Intent detail = new Intent(this,VideosData.class);
                detail.putExtra("Data",curr_video);
                startActivity(detail);
        }
        return true;
    }*/

}
