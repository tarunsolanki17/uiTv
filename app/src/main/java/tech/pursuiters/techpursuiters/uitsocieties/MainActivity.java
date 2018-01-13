package tech.pursuiters.techpursuiters.uitsocieties;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

import tech.pursuiters.techpursuiters.uitsocieties.drawer.AboutTheDevelopers;
import tech.pursuiters.techpursuiters.uitsocieties.drawer.App_info;

import  tech.pursuiters.techpursuiters.uitsocieties.R;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.GONE;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.AAVAHAN;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.ACM_RGPV;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.COHERENT;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.E_CELL;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.GREEN_ARMY;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.HACKER_EARTH;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.INSYNC;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.I_SPEAK_AALAY;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.MAHASANGRAM;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.PHOENIX;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.SHANKHNAAD;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.SRIJAN;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.SUNDARBAN;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.TECHNOPHILIC;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.TECH_PURSUITERS_EMAIL;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.TECH_PURSUITERS_PASS;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.TEDX_RGPV;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.UserData.EMAIL;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.uiTv_LINK;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.login;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.login_checker;
import static tech.pursuiters.techpursuiters.uitsocieties.R.id.update;
import static tech.pursuiters.techpursuiters.uitsocieties.R.string.email;

public class MainActivity extends AppCompatActivity implements Runnable, NavigationView.OnNavigationItemSelectedListener{

    /**********************  GLOBAL VARIABLES  ********************************/

    public static int width;
    ArrayList<Data1> club_data;
    public RecyclerView mainRecyclerView;
    RecyclerView.LayoutManager L_layoutManager,G_layoutManager;
    MyArrayAdap mainAdap;
    MenuItem icon;
    Toolbar main_toolbar;
    Toast toast;

    /**     DRAWER VARIABLES    */
    TextView contact_promo;
    TextView version_name;
    TextView version_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.containsKey("Update")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uiTv_LINK));
                if ((intent.resolveActivity(getPackageManager()) != null)) {
                    startActivity(intent);
                }
            } else if (bundle.containsKey("Link")){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(bundle.getString("Link")));
                if ((intent.resolveActivity(getPackageManager()) != null)) {
                    startActivity(intent);
                }
            }
        }


        main_toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(main_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contact_promo = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.contact_promo));
        initializeCountDrawer();

        int versionNumber= BuildConfig.VERSION_CODE;
        version_number = (TextView)findViewById(R.id.version_number);
        version_number.setText("Current App Version : "+versionNumber);

        String versionName = BuildConfig.VERSION_NAME;
        version_name=(TextView)findViewById(R.id.version_name);
        version_name.setText("Version Name : "+versionName);

        new MyWidthThread().start();

        club_data = new ArrayList<>();

        club_data.add(new Data1(R.drawable.aavahan,"Aavahan",AAVAHAN,1));
        club_data.add(new Data1(R.drawable.acm,"ACM Student Chapter RGPV",ACM_RGPV,2));
        club_data.add(new Data1(R.drawable.coherent,"Coherent",COHERENT,3));

//        club_data.add(new Data1(R.drawable.dishanjali,"Dishanjali",DISHANJALI,4));

        club_data.add(new Data1(R.drawable.ecell,"E-Cell",E_CELL,4));
        club_data.add(new Data1(R.drawable.green_army,"Green Army",GREEN_ARMY,5));
        club_data.add(new Data1(R.drawable.hacker_earth,"HackerEarth",HACKER_EARTH,6));
        club_data.add(new Data1(R.drawable.insync,"Insync",INSYNC,7));
        club_data.add(new Data1(R.drawable.ispeakaalay,"ISpeakAalay",I_SPEAK_AALAY,8));
        club_data.add(new Data1(R.drawable.mahasangram,"Mahasangram",MAHASANGRAM,9));
        club_data.add(new Data1(R.drawable.phoenix,"Phoenix",PHOENIX,10));
        club_data.add(new Data1(R.drawable.shankhnaad,"शंखनाद",SHANKHNAAD,11));
        club_data.add(new Data1(R.drawable.srijan,"Srijan",SRIJAN,12));
        club_data.add(new Data1(R.drawable.sundarban,"Sundarban",SUNDARBAN,13));
        club_data.add(new Data1(R.drawable.technophilic,"Technophilic",TECHNOPHILIC,14));
        club_data.add(new Data1(R.drawable.tedxrgpv,"TEDx RGPV",TEDX_RGPV,15));

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
            Log.v("Exception readFile",e.toString());
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

    boolean doubleBack = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBack) {
                super.onBackPressed();
                return;
            }
            doubleBack = true;
            Toast.makeText(getApplicationContext(),"Press again to exit",Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBack = false;
                }
            },2000);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.about_the_developers) {
            Intent intent = new Intent(this,AboutTheDevelopers.class);
            startActivity(intent);

        } else if (id == R.id.app_info) {
            Intent intent = new Intent(this,App_info.class);
            startActivity(intent);
        }

        else if (id == R.id.contact_promo) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:techpursuiters@gmail.com"));
            startActivity(intent);
        }

        else if (id == R.id.feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:techpursuiters@gmail.com"));
            startActivity(intent);
        }

        else if (id == R.id.share_app) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.share));
            intent.putExtra(Intent.EXTRA_TEXT,uiTv_LINK);
            startActivity(Intent.createChooser(intent, "Share app via"));
        }

        else if (id == R.id.rate_it) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uiTv_LINK));
            if((intent.resolveActivity(getPackageManager())!=null)){
                startActivity(intent);
            }
        }

        else if (id == update) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uiTv_LINK));
            if((intent.resolveActivity(getPackageManager())!=null)){
                startActivity(intent);
            }
        }

        else if (id == R.id.log_out) {
            if (isConnectedStatic(getApplicationContext())) {
                login_checker();
                if(!login){
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getApplicationContext(), "You are not logged in.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    String logout = getResources().getString(com.facebook.R.string.com_facebook_loginview_log_out_action);
                    String cancel = getResources().getString(com.facebook.R.string.com_facebook_loginview_cancel_action);
                    String message;
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null && profile.getName() != null) {
                        message = getResources().getString(com.facebook.R.string.com_facebook_loginview_logged_in_as, profile.getName());
                        Log.v("Profile!=null----", message);
                    } else {
                        message = getResources().getString(com.facebook.R.string.com_facebook_loginview_logged_in_using_facebook);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(logout, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                    login_checker();
                                }
                            })
                            .setNegativeButton(cancel, null);
                    builder.create().show();
                }
            }
            else{
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), R.string.no_internet_toast, Toast.LENGTH_LONG);
                toast.show();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void initializeCountDrawer(){
        contact_promo.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
        contact_promo.setText("New");
        contact_promo.setTextSize(13);
        contact_promo.setGravity(Gravity.CENTER_VERTICAL);
        contact_promo.setTypeface(null, Typeface.BOLD);
    }

}
