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

import java.util.ArrayList;

import static android.R.attr.width;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
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
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getMetrics(metrics);
//        width = metrics.widthPixels;
//        Log.v("Width---",""+width);

        ArrayList<Data1> club_data = new ArrayList<>();

//        TODO --> ADD THE PHOTOS WITH DIFFERENT DPIs
        club_data.add(new Data1(R.drawable.coherent,"Coherent"));
        club_data.add(new Data1(R.drawable.ecell,"E-cell"));
        club_data.add(new Data1(R.drawable.green_army,"Green Army"));
        club_data.add(new Data1(R.drawable.insync,"Insync"));
        club_data.add(new Data1(R.drawable.phoenix,"Phoenix"));
        club_data.add(new Data1(R.drawable.sundarban,"Sundarban"));

        MyArrayAdap madap = new MyArrayAdap(this,R.layout.image_layout,club_data);
        GridView gridv = (GridView) findViewById(R.id.gridview);
        gridv.setAdapter(madap);



/*        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.v("---Width---",""+width);


        ImageView imgvar = (ImageView) findViewById(R.id.imgv);
        int img_base = imgvar.getBottom();
        Log.v("---Base---",""+img_base);
        imgvar.setLayoutParams(new RelativeLayout.LayoutParams(width/2,width/2));
        imgvar.setImageResource(R.drawable.d);
        imgvar.setBaselineAlignBottom(true);
        int bott = imgvar.getBottom();
        Log.v("---bott---",""+bott);


        TextView tvar = (TextView) findViewById(tv);
        tvar.setText("Hello here");
//        float text_size = tvar.getTextSize();
//        Log.v("---Ht---",""+text_size);
        tvar.setLayoutParams(new RelativeLayout.LayoutParams(width/2, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup.LayoutParams lparams = tvar.getLayoutParams();
        int ht = lparams.height;

        Log.v("---Ht---",""+ht);
//        tvar.setX(0);
//        tvar.setY((width/2)-50);

*/

/*        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.family_son);
        images.add(R.drawable.family_daughter);
        images.add(R.drawable.family_father);
        images.add(R.drawable.family_mother);
        images.add(R.drawable.family_grandfather);
        images.add(R.drawable.family_grandmother);

        MyArrayAdap madap = new MyArrayAdap(this,0);


        GridView gv = (GridView) findViewById(R.id.gridview);
        gv.setAdapter(madap);
*/


/*        Log.v("---Width---",""+width);

        RelativeLayout imglay = (RelativeLayout) findViewById(R.id.Rellay);

        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(width,height);
        lparams.setMargins(0,0,0,0);

        ImageView imgv = new ImageView(getApplicationContext());
        TextView tv = new TextView(getApplicationContext());

        tv.setLayoutParams(lparams);

        imglay.addView(imgv,width/2,width/2);
        imglay.addView(tv);

        imgv.setImageResource(R.drawable.color_black);
        tv.setText("Hey Here!!");

        setContentView(R.layout.image_layout);
*/
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
}
