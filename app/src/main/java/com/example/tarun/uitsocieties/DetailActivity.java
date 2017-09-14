package com.example.tarun.uitsocieties;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import static com.example.tarun.uitsocieties.R.drawable.a;

/**
 * Created by Tarun on 10-Sep-17.
 */

/**
 * *****************  TO SHOW INDIVIDUAL PICTURES  ******************************
 */

public class DetailActivity extends AppCompatActivity {

    public static ArrayList<Integer> photos_data;
    public static int current_pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_photo);


//        Toolbar toolbar = new Toolbar(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent in2 = getIntent();
        photos_data = in2.getIntegerArrayListExtra("Photos");
        current_pos = in2.getIntExtra("position",-1);
        Log.v("Current_pos---",""+current_pos);
        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(photoPagerAdapter);
        viewPager.setCurrentItem(current_pos);
        // TODO --> PLACE THE CORRECT IMAGE AS THE FIRST FRAGMENT

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.share){

        }
        if(item.getItemId()==R.id.save){

        }
        return true;
    }

    public class PhotoPagerAdapter extends FragmentStatePagerAdapter{

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle pos_bundle = new Bundle();
            pos_bundle.putInt("position",position);
            FullPhotoFragment photoFragment = new FullPhotoFragment();
            photoFragment.setArguments(pos_bundle);
            return photoFragment;
        }

        @Override
        public int getCount() {
            return photos_data.size();
        }
    }

    public static class FullPhotoFragment extends Fragment{

        public FullPhotoFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View photo_view = inflater.inflate(R.layout.detail_photo_fragment,container,false);
            Bundle photo_pos = getArguments();
            int pos = photo_pos.getInt("position");
            ImageView image = (ImageView) photo_view.findViewById(R.id.image);
            image.setImageResource(photos_data.get(pos));
            return photo_view;
        }

    }
}