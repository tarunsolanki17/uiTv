package com.example.tarun.uitsocieties.updates_fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tarun.uitsocieties.EventsDataModel;
import com.example.tarun.uitsocieties.EventsDetailedActivity;
import com.example.tarun.uitsocieties.EventsListAdapter;
import com.example.tarun.uitsocieties.InClub;
import com.example.tarun.uitsocieties.MainActivity;
import com.example.tarun.uitsocieties.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.icu.lang.UCharacter.JoiningGroup.E;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.CLUB_IDS;
import static com.example.tarun.uitsocieties.ClubContract.NOTIF_UPDATE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.CAPTION;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.CITY;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.DESCRIPTION;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.FULL_PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.ID;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.LOCATION;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.MESSAGE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.NAME;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.PERMA_LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.PLACE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.SOURCE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.STATUS_TYPE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.TYPE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_PHOTO;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_STATUS;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_VIDEO;
import static com.example.tarun.uitsocieties.EventsFrag.absoluteClubName;
import static com.example.tarun.uitsocieties.EventsFrag.getFlags;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.InClub.events_data;
import static com.example.tarun.uitsocieties.InClub.fetchAsyncE;
import static com.example.tarun.uitsocieties.InClub.fetchAsyncU;
import static com.example.tarun.uitsocieties.InClub.login;
import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.InClub.updates_data;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFrag extends Fragment {

    UpdatesListAdapter updatesAdapter;
    ListView listView;
    ProgressBar pbar;
    TextView no_internet;
    TextView no_data, swipe_text;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    Context con;
    int data_len;
    ImageView down_arrow;

    public UpdatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View updates_view = inflater.inflate(R.layout.fragment_updates,container,false);

        con = updates_view.getContext();

        listView = updates_view.findViewById(R.id.listview_u);
        pbar = updates_view.findViewById(R.id.progress_bar_u);
        no_internet = updates_view.findViewById(R.id.no_internet_u);
        no_data = updates_view.findViewById(R.id.no_data_u);
        swipe = updates_view.findViewById(R.id.swipe_u);
        swipe_text = updates_view.findViewById(R.id.swipe_text);
        down_arrow = updates_view.findViewById(R.id.down_arrow);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                if(isConnectedFunc()) {
                    if(fetchAsyncU!=null) {
                        fetchAsyncU.cancel(true);
                    }
                    if(updates_data!=null)
                        updates_data.clear();
                    else
                        updates_data = new ArrayList<>();

                    pbar.setVisibility(VISIBLE);
                    listView.setVisibility(GONE);

                    if(updatesAdapter!=null)
                        updatesAdapter.notifyDataSetChanged();
                    updateJSONReq();
                }
                swipe.setRefreshing(false);
            }
        });

        if(isConnectedFunc()){

            if(savedInstanceState!=null&&savedInstanceState.getInt("Login")==1)
                savedInstanceState = null;

            if (savedInstanceState != null) {
                if(savedInstanceState.getBoolean("Incomplete")) {
                    if (fetchAsyncU != null) {
                        if (fetchAsyncU.getStatus().toString().equals("RUNNING")) {
                            pbar.setVisibility(VISIBLE);
                            listView.setVisibility(GONE);
                        }
                        if (fetchAsyncU.getStatus().toString().equals("FINISHED")) {
                            showing();
                        }
                    }
                    else {
                        showing();
                    }
                }
                else {
                    updates_data = savedInstanceState.getParcelableArrayList("updates_parcel");
                    showing();
                }
            }
            else {
                login_checker();
                if(login) {
                    updateJSONReq();
                }
            }
        }

        return updates_view;

    }

    private void showing(){
        if (updates_data != null)
            if (updates_data.isEmpty()) {
                pbar.setVisibility(GONE);
                listView.setVisibility(GONE);
                no_data.setVisibility(VISIBLE);
                swipe_text.setVisibility(VISIBLE);
                down_arrow.setVisibility(VISIBLE);
            } else {
                data_len = updates_data.size();
                pbar.setVisibility(GONE);
                no_data.setVisibility(GONE);
                swipe_text.setVisibility(GONE);
                down_arrow.setVisibility(GONE);
                listView.setVisibility(VISIBLE);
                onDataFetched();
            }
    }

    public void updateJSONReq(){
        data_len = 0;

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,caption,created_time,description,link,message,name,permalink_url,picture,full_picture,place,properties,source,status_type,type");
        parameters.putString("limit","10");

        final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                        parseJSONData(response.getJSONObject());
                        data_len = updates_data.size();
                    //  TODO --> HANDLE RESPONSE CODES
                }
                catch (Exception e) {
                    Log.v("Excep* updatJSONReq---:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        fetchAsyncU = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + club_id + "/posts",parameters, HttpMethod.GET, graphCallback).executeAndWait();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                onDataFetched();
            }
        };
        fetchAsyncU.execute();
    }

    public void parseJSONData(JSONObject response){

        if(response!=null&&response.length()>0) {
            try {
                JSONArray data = response.getJSONArray("data");

                for(int i=0;i<data.length();i++){
                    String id = "";
                    String caption = "";
                    String created_time = "";
                    String description = "";
                    String link = "";
                    String message = "";
                    String name = "";
                    String permalink_url = "";
                    String picture = "";
                    String full_picture = "";
                    String place_name = "";
                    String city = "";
                    String source = "";
                    String status_type = "";
                    String type = "";

                    JSONObject curr_update = data.getJSONObject(i);

                    if(curr_update.has(ID)&&!curr_update.isNull(ID)){
                        id = curr_update.getString(ID);
                    }
                    if(curr_update.has(CAPTION)&&!curr_update.isNull(CAPTION)){
                        caption = curr_update.getString(CAPTION);
                    }
                    if(curr_update.has(CREATED_TIME)&&!curr_update.isNull(CREATED_TIME)){
                        created_time = curr_update.getString(CREATED_TIME);
                    }
                    if(curr_update.has(DESCRIPTION)&&!curr_update.isNull(DESCRIPTION)){
                        description = curr_update.getString(DESCRIPTION);
                    }
                    if(curr_update.has(LINK)&&!curr_update.isNull(LINK)){
                        link = curr_update.getString(LINK);
                    }
                    if(curr_update.has(MESSAGE)&&!curr_update.isNull(MESSAGE)){
                        message = curr_update.getString(MESSAGE);
                    }
                    if(curr_update.has(NAME)&&!curr_update.isNull(NAME)){
                        name = curr_update.getString(NAME);
                    }
                    if(curr_update.has(PERMA_LINK)&&!curr_update.isNull(PERMA_LINK)){
                        permalink_url = curr_update.getString(PERMA_LINK);
                    }
                    if(curr_update.has(PICTURE)&&!curr_update.isNull(PICTURE)){
                        picture = curr_update.getString(PICTURE);
                    }
                    if(curr_update.has(FULL_PICTURE)&&!curr_update.isNull(FULL_PICTURE)){
                        full_picture = curr_update.getString(FULL_PICTURE);
                    }

                    if(curr_update.has(PLACE)&&!curr_update.isNull(PLACE)) {
                        JSONObject place_obj = curr_update.getJSONObject(PLACE);

                        if (place_obj.has(NAME) && !place_obj.isNull(NAME)) {
                            place_name = place_obj.getString(NAME);
                        }

                        if (place_obj.has(LOCATION) && !place_obj.isNull(LOCATION)) {
                            JSONObject location = place_obj.getJSONObject(LOCATION);

                            if (location.has(CITY) && !location.isNull(CITY)) {
                                city = location.getString(CITY);
                            }
                        }
                    }

                    if(curr_update.has(SOURCE)&&!curr_update.isNull(SOURCE)){
                        source = curr_update.getString(SOURCE);
                    }
                    if(curr_update.has(STATUS_TYPE)&&!curr_update.isNull(STATUS_TYPE)){
                        status_type = curr_update.getString(STATUS_TYPE);
                    }
                    if(curr_update.has(TYPE)&&!curr_update.isNull(TYPE)){
                        type = curr_update.getString(TYPE);
                    }
///                 TODO --> fetchAsync MIGHT BECOME NULL
                    if(!fetchAsyncU.isCancelled()) {
                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date update_date = format.parse(created_time);

                        int diffInDays = (int) ((today.getTime() - update_date.getTime()) / (1000*60*60*24));
                        Log.v("Diff in days---",String.valueOf(diffInDays));
                        if(diffInDays>=0&&diffInDays<=14)
                        updates_data.add(new UpdateParcel(id, caption, created_time, description, link, message, name, permalink_url, picture, full_picture, place_name, city, source, status_type, type));
                    }
                }
            Log.v("Updates size---",String.valueOf(updates_data.size()));
            }
            catch (Exception e) {
                Log.v("Excep UpdatParseJSON---",e.toString());
            }
        }
    }

    public void onDataFetched(){
        if(data_len==0){
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            listView.setVisibility(GONE);
            no_data.setVisibility(VISIBLE);
            swipe_text.setVisibility(VISIBLE);
            down_arrow.setVisibility(VISIBLE);
        }
        else {
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            swipe_text.setVisibility(GONE);
            down_arrow.setVisibility(GONE);
            listView.setVisibility(VISIBLE);

            updatesAdapter = new UpdatesListAdapter(con, updates_data);
            listView.setAdapter(updatesAdapter);
        }
    }

    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnected();

        if(!isConnected){
//            TODO --> IMAGE WITH TEXT IN NO INTERNET
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            listView.setVisibility(GONE);
            swipe_text.setVisibility(VISIBLE);
            down_arrow.setVisibility(VISIBLE);
            no_internet.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            swipe_text.setVisibility(GONE);
            down_arrow.setVisibility(GONE);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(fetchAsyncU!=null){
            if(fetchAsyncU.getStatus().toString().equals("RUNNING"))
                outState.putBoolean("Incomplete",true);
            else if(fetchAsyncU.getStatus().toString().equals("FINISHED")) {
                outState.putParcelableArrayList("updates_parcel", updates_data);
                outState.putBoolean("Incomplete", false);
            }
        }
        else {
            outState.putParcelableArrayList("updates_parcel",updates_data);
            outState.putBoolean("Incomplete", false);
        }
    }

    /****************************   NOTIFICATIONS   *****************************************/

    public static void updateNotification(Context con, final ArrayList<UpdateParcel> new_updates, String club_name, String clubID, int index){
        Log.v("Update Notif----","Sent");
        UpdateParcel new_update = new_updates.get(index);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(con)
//                .setSmallIcon(R.drawable.ic_notification)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(con.getResources(),R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(con,R.color.colorPrimary))
                .setContentTitle("Update from " + absoluteClubName(club_name))
                .setContentText(getNotifTitle(new_update) + absoluteClubName(club_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(new_update.getMessage()))
                .setContentIntent(updateContentIntent(con,new_update,clubID,index,club_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("New Update by " + absoluteClubName(club_name))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) con.getSystemService(con.NOTIFICATION_SERVICE);
        Log.v("Notification---:","Issue");
        notificationManager.notify(getClubIndex(clubID),builder.build());
    }

    public static PendingIntent updateContentIntent(Context con, UpdateParcel new_event, String clubID, int index, String clubName){

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(con);

        Intent updatesFrag = new Intent(con,InClub.class);
        //  TODO --> SET CORRECT FLAG FOR CLUBS
        updatesFrag.addFlags(getFlags(clubName));
        updatesFrag.putExtra("CLUB_ID",clubID);
        updatesFrag.putExtra(NOTIF_UPDATE,true);

        Intent mainAct = new Intent(con,MainActivity.class);

        stackBuilder.addNextIntentWithParentStack(mainAct);
        stackBuilder.addNextIntent(updatesFrag);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(getClubIndex(clubID),PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private static String getNotifTitle(UpdateParcel new_update){
        String line = "New ";

        switch (new_update.getType()){
            case UP_STATUS: return line + "Post by ";
            case UP_LINK: return line + "Link by ";
            case UP_PHOTO: return line + "Photo uploaded by ";
            case UP_VIDEO: return line + "Video uploaded by ";
        }
        return "New Update by ";
    }

    public static int getClubIndex(String clubID){
        for(int k=0;k<CLUB_IDS.length;k++){
            if(clubID.equals(CLUB_IDS[k]))
                return k;
        }
        return -1;
    }
}
