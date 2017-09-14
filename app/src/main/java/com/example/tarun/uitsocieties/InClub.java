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

import com.facebook.AccessToken;

/**
 * Created by Tarun on 17-Aug-17.
 */

public class InClub extends AppCompatActivity {

    static boolean login;
    static FragmentManager fmag;
    static FragmentTransaction ft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inclub_main);

        Intent in2 = getIntent();
        activity_name(in2.getFlags());

        ViewPager viewpgr = (ViewPager) findViewById(R.id.viewpg);
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
}
