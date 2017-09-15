package com.example.tarun.uitsocieties;

import android.content.Intent;
import android.net.Uri;
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

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

/**
 * Created by Tarun on 17-Aug-17.
 */

public class InClub extends AppCompatActivity {

    static boolean login;
    static FragmentManager fmag;
    static FragmentTransaction ft;
    static ViewPager viewpgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inclub_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        login_checker();

        Intent in2 = getIntent();
        activity_name(in2.getFlags());

        viewpgr = (ViewPager) findViewById(R.id.viewpg);
        MyPagerAdap adap = new MyPagerAdap(getSupportFragmentManager());
        viewpgr.setAdapter(adap);

        TabLayout tablay = (TabLayout) findViewById(R.id.tabl);
        tablay.setupWithViewPager(viewpgr);

        login = AccessToken.getCurrentAccessToken()!=null;
        Log.v("login---",String.valueOf(login));
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.v("Access Token---",AccessToken.getCurrentAccessToken().toString());
        }



        /*
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AboutFrag AFrag = new AboutFrag();
        ft.add(R.id.lilay2,AFrag);
        PhotosFrag PFrag = new PhotosFrag();
        ft.add(R.id.lilay2,PFrag);

        ft.commit();
        */
    }
    public static void login_checker(){
        login = AccessToken.getCurrentAccessToken()!=null;
        Log.v("login---",String.valueOf(login));
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.v("Access Token---",AccessToken.getCurrentAccessToken().toString());
        }
    }
    public void activity_name(int flag){
        if(flag==0)
            setTitle("Coherent");
        if(flag==1)
            setTitle("E-Cell");
        if(flag==2)
            setTitle("Green Army");
        if(flag==3)
            setTitle("Insync");
        if(flag==4)
            setTitle("Phoenix");
        if(flag==5)
            setTitle("Sundarban");
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
}
