package com.example.tarun.uitsocieties;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tarun.uitsocieties.updates_fragment.UpdateParcel;
import com.example.tarun.uitsocieties.videos_fragment.VideoParcel;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.io.File;
import java.util.ArrayList;

import static com.example.tarun.uitsocieties.ClubContract.CLUB_IDS;
import static com.example.tarun.uitsocieties.ClubContract.CLUB_NAMES_VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.PHOTO_FILE;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

/**
 * Created by Tarun on 17-Aug-17.
 */

public class InClub extends AppCompatActivity {

    public static ArrayList<UpdateParcel> updates_data;
    public static ArrayList<EventsDataModel> events_data;
    public static ArrayList<PhotoParcel> photos_data;
    public static ArrayList<VideoParcel> videos_data;
    public static boolean login;
    static FragmentTransaction ft;
    public static ViewPager viewpgr;
    public static String club_id;
    public static boolean event_detail=false;
    public static AsyncTask fetchAsyncU, fetchAsyncE, fetchAsyncP, fetchAsyncV;
    public static boolean started1=false, started2=false, list_item=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inclub_main);

        updates_data = new ArrayList<>();
        events_data = new ArrayList<>();
        photos_data = new ArrayList<>();
        videos_data = new ArrayList<>();

//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        login_checker();

        Intent in2 = getIntent();
        int position=0;
        if(event_detail){
            for(int i=0;i<CLUB_IDS.length;i++)
                if(club_id.equals(CLUB_IDS[i])) {
                    position = i;
                    break;
                }
        }
        else {
            position = in2.getFlags();
        }
        setTitle(CLUB_NAMES_VISIBLE[position]);


        boolean notif_intent = in2.getBooleanExtra("Notif_intent",false);

        club_id = CLUB_IDS[position];
        if(club_id!=null)
        Log.v("Clubid----",club_id);

        viewpgr = (ViewPager) findViewById(R.id.viewpg);
        MyPagerAdap adap = new MyPagerAdap(getSupportFragmentManager());
        viewpgr.setAdapter(adap);

        if(event_detail) {
            viewpgr.setCurrentItem(2);      /**     SET POSITION TO EVENTS FRAGMENT   */
        }
        event_detail = false;
        if(notif_intent)
            viewpgr.setCurrentItem(2);      /**     SET POSITION TO EVENTS FRAGMENT   */

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inclub_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.settings_button){
            Intent settings_intent = new Intent(getApplicationContext(),Settings.class);
            startActivity(settings_intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        started1 = false;
        started2 = false;
        list_item = false;

        File photoFile = new File(PHOTO_FILE);
        if(photoFile.exists()){
            boolean deleted = photoFile.delete();
            Log.v("Photo File Del---",String.valueOf(deleted));
        }
    }
}
