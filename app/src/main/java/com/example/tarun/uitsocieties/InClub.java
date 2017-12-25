package com.example.tarun.uitsocieties;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.tarun.uitsocieties.photos_fragment.Photo_Serial;
import com.example.tarun.uitsocieties.updates_fragment.UpdateParcel;
import com.example.tarun.uitsocieties.videos_fragment.VideoParcel;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.sdk.InMobiSdk;

import java.io.File;
import java.util.ArrayList;

import static com.example.tarun.uitsocieties.ClubContract.CLUB_IDS;
import static com.example.tarun.uitsocieties.ClubContract.CLUB_NAMES_VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.NOTIF_EVENT;
import static com.example.tarun.uitsocieties.ClubContract.NOTIF_UPDATE;
import static com.example.tarun.uitsocieties.ClubContract.PHOTO_FILE;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

/**
 * Created by Tarun on 17-Aug-17.
 */

public class InClub extends AppCompatActivity {

    public static ArrayList<UpdateParcel> updates_data;
    public static ArrayList<EventsDataModel> events_data;
    public static ArrayList<Photo_Serial> photos_data;
    public static ArrayList<VideoParcel> videos_data;
    public static boolean login;
    public static ViewPager viewpgr;
    public static String club_id;
    public static AsyncTask fetchAsyncU, fetchAsyncE, fetchAsyncP, fetchAsyncV;
    public static int albumNo,data_len,album_len,album_len_max_index;
    public static RecycPhotosAdap recycAdapter;
    public static int position;
    public InMobiBanner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inclub_main);

        updates_data = new ArrayList<>();
        events_data = new ArrayList<>();
        photos_data = new ArrayList<>();
        videos_data = new ArrayList<>();

        albumNo = 0;    /**     TO START FROM THE FIRST ALBUM   */

        banner = (InMobiBanner) findViewById(R.id.banner1);
        banner.load();
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHideOnContentScrollEnabled(true);
//        Log.v("Action Bar---",String.valueOf(getSupportActionBar().isHideOnContentScrollEnabled()));


        login_checker();

        Intent in2 = getIntent();
        position=0;

        position = in2.getFlags();

        setTitle(CLUB_NAMES_VISIBLE[position]);

        club_id = CLUB_IDS[position];
        if(club_id!=null)
        Log.v("Clubid----",club_id);

        viewpgr = (ViewPager) findViewById(R.id.viewpg);
        MyPagerAdap adap = new MyPagerAdap(getSupportFragmentManager());
        viewpgr.setAdapter(adap);

        boolean notif_event = in2.getBooleanExtra(NOTIF_EVENT,false);
        boolean notif_update = in2.getBooleanExtra(NOTIF_UPDATE,false);

        if(notif_event)
            viewpgr.setCurrentItem(2);      /**     SET POSITION TO EVENTS FRAGMENT   */

        else if(notif_update){
            viewpgr.setCurrentItem(1);      /**     SET POSITION TO UPDATES FRAGMENT   */
        }

        TabLayout tablay = (TabLayout) findViewById(R.id.tabl);
        tablay.setupWithViewPager(viewpgr);

        login = AccessToken.getCurrentAccessToken()!=null;
        Log.v("login---",String.valueOf(login));
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.v("Access Token---",AccessToken.getCurrentAccessToken().toString());
        }

    }
    public static void login_checker(){
        login = AccessToken.getCurrentAccessToken()!=null;
        Log.v("login---",String.valueOf(login));
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.v("Access Token---",AccessToken.getCurrentAccessToken().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("onDestroy inclub---","Running");
        /**     Cancelling all the fetching tasks because they were still running if someone navigated to another club.     */

        if(fetchAsyncU!=null){
            fetchAsyncU.cancel(true);
            fetchAsyncU = null;
        }
        if(fetchAsyncE!=null) {
            fetchAsyncE.cancel(true);
            fetchAsyncE = null;
        }

        if(fetchAsyncP!=null) {
            fetchAsyncP.cancel(true);
            fetchAsyncP = null;
        }

        if(fetchAsyncV!=null) {
            fetchAsyncV.cancel(true);
            fetchAsyncV = null;
        }

        if(updates_data!=null){
            updates_data.clear();
        }
        if(events_data!=null){
            events_data.clear();
        }
        if(photos_data!=null){
            photos_data.clear();
        }
        if(videos_data!=null){
            videos_data.clear();
        }

        File photoFile = new File(getApplicationContext().getFilesDir(),PHOTO_FILE);
        if(photoFile.exists()){
            boolean deleted = photoFile.delete();
            Log.v("Photo File Del---",String.valueOf(deleted));
        }

        albumNo = 0;
        album_len = 0;
        album_len_max_index = 0;
        data_len = 0;
        recycAdapter = null;

        Runtime.getRuntime().gc();
    }
}
