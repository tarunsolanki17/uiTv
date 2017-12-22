package com.example.tarun.uitsocieties;

import android.content.ClipData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.facebook.AccessToken;

import static com.example.tarun.uitsocieties.ClubContract.ACM_RGPV;
import static com.example.tarun.uitsocieties.ClubContract.COHERENT;
import static com.example.tarun.uitsocieties.ClubContract.E_CELL;
import static com.example.tarun.uitsocieties.ClubContract.GREEN_ARMY;
import static com.example.tarun.uitsocieties.ClubContract.HACKER_EARTH;
import static com.example.tarun.uitsocieties.ClubContract.INSYNC;
import static com.example.tarun.uitsocieties.ClubContract.I_SPEAK_AALAY;
import static com.example.tarun.uitsocieties.ClubContract.MAHASANGRAM;
import static com.example.tarun.uitsocieties.ClubContract.PHOENIX;
import static com.example.tarun.uitsocieties.ClubContract.SHANKHNAAD;
import static com.example.tarun.uitsocieties.ClubContract.SUNDARBAN;
import static com.example.tarun.uitsocieties.ClubContract.TECHNOPHILIC;
import static com.example.tarun.uitsocieties.ClubContract.TEDX_RGPV;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements Runnable{

    /**********************  GLOBAL VARIABLES  ********************************/

    public static int width;
    ArrayList<Data1> club_data;
    public RecyclerView mainRecyclerView;
    RecyclerView.LayoutManager L_layoutManager,G_layoutManager;
    MyArrayAdap mainAdap;
    MenuItem icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyWidthThread().start();

        club_data = new ArrayList<>();

//        TODO --> ADD THE PHOTOS WITH DIFFERENT DPIs
        club_data.add(new Data1(R.drawable.acm,"ACM Student Chapter RGPV",ACM_RGPV,1));
        club_data.add(new Data1(R.drawable.coherent,"Coherent",COHERENT,2));
        club_data.add(new Data1(R.drawable.ecell,"E-Cell",E_CELL,3));
        club_data.add(new Data1(R.drawable.green_army,"Green Army",GREEN_ARMY,4));
        club_data.add(new Data1(R.drawable.hacker_earth,"Hacker Earth",HACKER_EARTH,5));
        club_data.add(new Data1(R.drawable.insync,"Insync",INSYNC,6));
        club_data.add(new Data1(R.drawable.ispeakaalay,"I Speak Aalay",I_SPEAK_AALAY,7));
        club_data.add(new Data1(R.drawable.mahasangram,"Mahasangram",MAHASANGRAM,8));
        club_data.add(new Data1(R.drawable.phoenix,"Phoenix",PHOENIX,9));
        club_data.add(new Data1(R.drawable.shankhnaad,"शंखनाद",SHANKHNAAD,10));
        club_data.add(new Data1(R.drawable.sundarban,"Sundarban",SUNDARBAN,11));
        club_data.add(new Data1(R.drawable.technophilic,"Technophilic",TECHNOPHILIC,12));
        club_data.add(new Data1(R.drawable.tedxrgpv,"TEDx RGPV",TEDX_RGPV,13));

        L_layoutManager = new LinearLayoutManager(this);
        G_layoutManager = new GridLayoutManager(this,3);

        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recyc_view);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(G_layoutManager);

        mainAdap = new MyArrayAdap(this,club_data,R.layout.main_image_layout_grid);
        mainRecyclerView.setAdapter(mainAdap);

        readFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_changer,menu);
        icon = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.layout_changer){
            if(mainRecyclerView.getLayoutManager()==L_layoutManager) {
                mainAdap = new MyArrayAdap(this, club_data, R.layout.main_image_layout_grid);
                mainRecyclerView.setLayoutManager(G_layoutManager);
                mainRecyclerView.setAdapter(mainAdap);
                icon.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_format_list_bulleted_black_24dp));
            }
            else if(mainRecyclerView.getLayoutManager()==G_layoutManager){
                mainAdap = new MyArrayAdap(this,club_data,R.layout.main_image_layout);
                mainRecyclerView.setLayoutManager(L_layoutManager);
                mainRecyclerView.setAdapter(mainAdap);
                icon.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_grid_on_black_24dp));
            }
        }

        return true;
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

    public static boolean isConnectedStatic(Context con){
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        boolean isConnectedStat = ninfo != null && ninfo.isConnected();

        if(!isConnectedStat)
            return false;
        else
            return true;
    }
}
