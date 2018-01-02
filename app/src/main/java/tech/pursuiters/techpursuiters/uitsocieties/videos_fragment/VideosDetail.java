package tech.pursuiters.techpursuiters.uitsocieties.videos_fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import tech.pursuiters.techpursuiters.uitsocieties.R;

import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerView;
import com.appnext.base.Appnext;

import java.util.ArrayList;

import tech.pursuiters.techpursuiters.uitsocieties.ClubContract;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static tech.pursuiters.techpursuiters.uitsocieties.R.id.banner3;

public class VideosDetail extends AppCompatActivity implements EasyVideoCallback {

    EasyVideoPlayer easyVideoPlayer;
    ArrayList<VideoParcel> videoParcel;
    VideoParcel curr_video;
    int pos;
    TextView error;
    Toolbar toolbar;
    BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_detail);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar_v);
        setSupportActionBar(toolbar);

        Appnext.init(getApplicationContext());
        bannerView = (BannerView) findViewById(R.id.banner3);
        BannerAdRequest banner_request = new BannerAdRequest();
        banner_request
                .setCategories("Action, Adventure, Racing");
        bannerView.loadAd(banner_request);

        bannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(easyVideoPlayer.isPlaying())
                    easyVideoPlayer.stop();
            }
        });
//      TODO -> ADD AUDIO FOCUS

        String source_url;
        Intent in = getIntent();
        //  TODO --> HANDLE pos = -1
        if(in.getBooleanExtra(ClubContract.UpdatesConstants.UPDATE_VIDEO,false)){
            source_url = in.getStringExtra(ClubContract.UpdatesConstants.UPDATE_VIDEO_URL);
        }
        else {
            pos = in.getIntExtra("position", -1);
            videoParcel = in.getParcelableArrayListExtra("video_parcel");
            curr_video = videoParcel.get(pos);
            source_url = curr_video.getSource_url();
        }

        error = (TextView) findViewById(R.id.easy_player_error);
        error.setVisibility(GONE);
        easyVideoPlayer = (EasyVideoPlayer) findViewById(R.id.easy_vplayer);

        easyVideoPlayer.setAutoFullscreen(true);

        easyVideoPlayer.setCallback(this);
        easyVideoPlayer.setSource(Uri.parse(source_url));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroy();
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
