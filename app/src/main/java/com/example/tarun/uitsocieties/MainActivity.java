package com.example.tarun.uitsocieties;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import static android.R.attr.width;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import com.example.tarun.uitsocieties.ClubContract;
import com.facebook.AccessToken;

import static com.example.tarun.uitsocieties.ClubContract.COHERENT;
import static com.example.tarun.uitsocieties.ClubContract.E_CELL;
import static com.example.tarun.uitsocieties.ClubContract.GREEEN_ARMY;
import static com.example.tarun.uitsocieties.ClubContract.INSYNC;
import static com.example.tarun.uitsocieties.ClubContract.PHOENIX;
import static com.example.tarun.uitsocieties.ClubContract.SUNDARBAN;
import static com.example.tarun.uitsocieties.InClub.login;
import static com.example.tarun.uitsocieties.R.id.tv;

public class MainActivity extends AppCompatActivity implements Runnable{

    /**********************  GLOBAL VARIABLES  ********************************/

    public static int width;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyWidthThread().start();

        ArrayList<Data1> club_data = new ArrayList<>();

//        TODO --> ADD THE PHOTOS WITH DIFFERENT DPIs
        club_data.add(new Data1(R.drawable.coherent,"Coherent",COHERENT));
        club_data.add(new Data1(R.drawable.ecell,"E-cell",E_CELL));
        club_data.add(new Data1(R.drawable.green_army,"Green Army",GREEEN_ARMY));
        club_data.add(new Data1(R.drawable.insync,"Insync",INSYNC));
        club_data.add(new Data1(R.drawable.phoenix,"Phoenix",PHOENIX));
        club_data.add(new Data1(R.drawable.sundarban,"Sundarban",SUNDARBAN));

        MyArrayAdap madap = new MyArrayAdap(this,R.layout.image_layout,club_data);
        GridView gridv = (GridView) findViewById(R.id.gridview);
        gridv.setAdapter(madap);

        readFile();
    }

    public class MyWidthThread extends Thread{
        @Override
        public void run() {
            super.run();
            Log.v("Thread---"," Running");
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
        }
    }
    @Override
    public void run() {
        Log.v("Thread---"," Running");
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
    }

    public void readFile(){
        File eventids = new File(getFilesDir(),"event_ids_file");
        InputStream inStream;
        try {
            Log.v("readFile---","Running");
            Log.v("Length---",String.valueOf(eventids.length()));
            inStream = openFileInput(eventids.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

            String line;
            String newline = "";
//            int c=0;
            while ((line = bufferedReader.readLine())!=null){
                Log.v("Line----",line);
//                String[] old_id = line.split(" ");
//                Log.v("old_id----",old_id[0] + " " + old_id[1]);
            }
            bufferedReader.close();
            inStream.close();
        }
        catch (Exception e) {
            Log.v("Exception---","Caught");
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(AccessToken.getCurrentAccessToken()!=null)
            FetchJobScheduler.scheduleFetching(this);
    }
}
