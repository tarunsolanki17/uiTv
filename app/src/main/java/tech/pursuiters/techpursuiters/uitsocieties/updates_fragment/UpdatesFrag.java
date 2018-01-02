package tech.pursuiters.techpursuiters.uitsocieties.updates_fragment;


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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import tech.pursuiters.techpursuiters.uitsocieties.InClub;
import tech.pursuiters.techpursuiters.uitsocieties.MainActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tech.pursuiters.techpursuiters.uitsocieties.ClubContract;
import tech.pursuiters.techpursuiters.uitsocieties.EventsFrag;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFrag extends Fragment {

    UpdatesListAdapter updatesAdapter;
    ListView listView;
    ProgressBar pbar;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    Context con;
    int data_len;
    LinearLayout no_internet,no_data;
    ImageView swipe_text;

    public UpdatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View updates_view = inflater.inflate(tech.pursuiters.techpursuiters.uitsocieties.R.layout.fragment_updates,container,false);

        con = updates_view.getContext();

        listView = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.listview_u);
        pbar = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.progress_bar_u);
        no_internet = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.no_internet_u);
        no_data = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.no_data_u);
        swipe = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.swipe_u);
        swipe_text = updates_view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.swipe_text_u);

        View footer = new View(getActivity());
        footer.setFocusable(false);
        footer.setFocusableInTouchMode(false);
        footer.setClickable(false);
        footer.setEnabled(false);
        footer.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        listView.addFooterView(footer);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                if(isConnectedFunc()) {
                    if(InClub.fetchAsyncU!=null) {
                        InClub.fetchAsyncU.cancel(true);
                    }
                    if(InClub.updates_data!=null)
                        InClub.updates_data.clear();
                    else
                        InClub.updates_data = new ArrayList<>();

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
                    if (InClub.fetchAsyncU != null) {
                        if (InClub.fetchAsyncU.getStatus().toString().equals("RUNNING")) {
                            pbar.setVisibility(VISIBLE);
                            listView.setVisibility(GONE);
                        }
                        if (InClub.fetchAsyncU.getStatus().toString().equals("FINISHED")) {
                            showing();
                        }
                    }
                    else {
                        showing();
                    }
                }
                else {
                    InClub.updates_data = savedInstanceState.getParcelableArrayList("updates_parcel");
                    showing();
                }
            }
            else {
                InClub.login_checker();
                if(InClub.login) {
                    updateJSONReq();
                }
            }
        }

        return updates_view;

    }

    private void showing(){
        if (InClub.updates_data != null)
            if (InClub.updates_data.isEmpty()) {
                pbar.setVisibility(GONE);
                listView.setVisibility(GONE);
                no_data.setVisibility(VISIBLE);
                swipe_text.setVisibility(VISIBLE);
            } else {
                data_len = InClub.updates_data.size();
                pbar.setVisibility(GONE);
                no_data.setVisibility(GONE);
                swipe_text.setVisibility(GONE);
                listView.setVisibility(VISIBLE);
                onDataFetched();
            }
    }

    public void updateJSONReq(){
        data_len = 0;

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,caption,created_time,description,link,message,name,permalink_url,picture,full_picture,place,properties,source,status_type,type");
        parameters.putString("limit","25");

        final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                        parseJSONData(response.getJSONObject());
                        data_len = InClub.updates_data.size();
                }
                catch (Exception e) {
                    Log.v("Excep* updatJSONReq---:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        InClub.fetchAsyncU = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + InClub.club_id + "/posts",parameters, HttpMethod.GET, graphCallback).executeAndWait();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                onDataFetched();
            }
        };
        InClub.fetchAsyncU.execute();
    }

    public void parseJSONData(JSONObject response){

        if(response!=null&&response.length()>0) {
            try {
                JSONArray data = response.getJSONArray("data");

                String id;
                String caption;
                String created_time;
                String description;
                String link;
                String message;
                String name;
                String permalink_url;
                String picture;
                String full_picture;
                String place_name;
                String city;
                String source;
                String status_type;
                String type;

                for(int i=0;i<data.length();i++){
                    id = "";
                    caption = "";
                    created_time = "";
                    description = "";
                    link = "";
                    message = "";
                    name = "";
                    permalink_url = "";
                    picture = "";
                    full_picture = "";
                    place_name = "";
                    city = "";
                    source = "";
                    status_type = "";
                    type = "";

                    JSONObject curr_update = data.getJSONObject(i);

                    if(curr_update.has(ClubContract.UpdatesConstants.ID)&&!curr_update.isNull(ClubContract.UpdatesConstants.ID)){
                        id = curr_update.getString(ClubContract.UpdatesConstants.ID);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.CAPTION)&&!curr_update.isNull(ClubContract.UpdatesConstants.CAPTION)){
                        caption = curr_update.getString(ClubContract.UpdatesConstants.CAPTION);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.CREATED_TIME)&&!curr_update.isNull(ClubContract.UpdatesConstants.CREATED_TIME)){
                        created_time = curr_update.getString(ClubContract.UpdatesConstants.CREATED_TIME);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.DESCRIPTION)&&!curr_update.isNull(ClubContract.UpdatesConstants.DESCRIPTION)){
                        description = curr_update.getString(ClubContract.UpdatesConstants.DESCRIPTION);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.LINK)&&!curr_update.isNull(ClubContract.UpdatesConstants.LINK)){
                        link = curr_update.getString(ClubContract.UpdatesConstants.LINK);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.MESSAGE)&&!curr_update.isNull(ClubContract.UpdatesConstants.MESSAGE)){
                        message = curr_update.getString(ClubContract.UpdatesConstants.MESSAGE);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.NAME)&&!curr_update.isNull(ClubContract.UpdatesConstants.NAME)){
                        name = curr_update.getString(ClubContract.UpdatesConstants.NAME);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.PERMA_LINK)&&!curr_update.isNull(ClubContract.UpdatesConstants.PERMA_LINK)){
                        permalink_url = curr_update.getString(ClubContract.UpdatesConstants.PERMA_LINK);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.PICTURE)&&!curr_update.isNull(ClubContract.UpdatesConstants.PICTURE)){
                        picture = curr_update.getString(ClubContract.UpdatesConstants.PICTURE);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.FULL_PICTURE)&&!curr_update.isNull(ClubContract.UpdatesConstants.FULL_PICTURE)){
                        full_picture = curr_update.getString(ClubContract.UpdatesConstants.FULL_PICTURE);
                    }

                    if(curr_update.has(ClubContract.UpdatesConstants.PLACE)&&!curr_update.isNull(ClubContract.UpdatesConstants.PLACE)) {
                        JSONObject place_obj = curr_update.getJSONObject(ClubContract.UpdatesConstants.PLACE);

                        if (place_obj.has(ClubContract.UpdatesConstants.NAME) && !place_obj.isNull(ClubContract.UpdatesConstants.NAME)) {
                            place_name = place_obj.getString(ClubContract.UpdatesConstants.NAME);
                        }

                        if (place_obj.has(ClubContract.UpdatesConstants.LOCATION) && !place_obj.isNull(ClubContract.UpdatesConstants.LOCATION)) {
                            JSONObject location = place_obj.getJSONObject(ClubContract.UpdatesConstants.LOCATION);

                            if (location.has(ClubContract.UpdatesConstants.CITY) && !location.isNull(ClubContract.UpdatesConstants.CITY)) {
                                city = location.getString(ClubContract.UpdatesConstants.CITY);
                            }
                        }
                    }

                    if(curr_update.has(ClubContract.UpdatesConstants.SOURCE)&&!curr_update.isNull(ClubContract.UpdatesConstants.SOURCE)){
                        source = curr_update.getString(ClubContract.UpdatesConstants.SOURCE);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.STATUS_TYPE)&&!curr_update.isNull(ClubContract.UpdatesConstants.STATUS_TYPE)){
                        status_type = curr_update.getString(ClubContract.UpdatesConstants.STATUS_TYPE);
                    }
                    if(curr_update.has(ClubContract.UpdatesConstants.TYPE)&&!curr_update.isNull(ClubContract.UpdatesConstants.TYPE)){
                        type = curr_update.getString(ClubContract.UpdatesConstants.TYPE);
                    }
///                 TODO --> fetchAsync MIGHT BECOME NULL
                    if(!InClub.fetchAsyncU.isCancelled()) {
                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date update_date = format.parse(created_time);

                        int diffInDays = (int) ((today.getTime() - update_date.getTime()) / (1000*60*60*24));
                        Log.v("Diff in days---",String.valueOf(diffInDays));
                        if(diffInDays>=0&&diffInDays<=14)
                        InClub.updates_data.add(new UpdateParcel(id, caption, created_time, description, link, message, name, permalink_url, picture, full_picture, place_name, city, source, status_type, type));
                    }
                }
            Log.v("Updates size---",String.valueOf(InClub.updates_data.size()));
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
        }
        else {
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            swipe_text.setVisibility(GONE);
            listView.setVisibility(VISIBLE);

            updatesAdapter = new UpdatesListAdapter(con, InClub.updates_data);
            listView.setAdapter(updatesAdapter);
        }
    }

    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnected();

        if(!isConnected){
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            listView.setVisibility(GONE);
            swipe_text.setVisibility(VISIBLE);
            no_internet.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            swipe_text.setVisibility(GONE);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(InClub.fetchAsyncU!=null){
            if(InClub.fetchAsyncU.getStatus().toString().equals("RUNNING"))
                outState.putBoolean("Incomplete",true);
            else if(InClub.fetchAsyncU.getStatus().toString().equals("FINISHED")) {
                outState.putParcelableArrayList("updates_parcel", InClub.updates_data);
                outState.putBoolean("Incomplete", false);
            }
        }
        else {
            outState.putParcelableArrayList("updates_parcel", InClub.updates_data);
            outState.putBoolean("Incomplete", false);
        }
    }

    /****************************   NOTIFICATIONS   *****************************************/

    public static void updateNotification(Context con, final ArrayList<UpdateParcel> new_updates, String club_name, String clubID, int index){
        Log.v("Update Notif----","Sent");
        UpdateParcel new_update = new_updates.get(index);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(con)
//                .setSmallIcon(R.drawable.ic_notification)
                .setSmallIcon(tech.pursuiters.techpursuiters.uitsocieties.R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(con.getResources(), tech.pursuiters.techpursuiters.uitsocieties.R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(con, tech.pursuiters.techpursuiters.uitsocieties.R.color.colorPrimary))
                .setContentTitle("Update from " + EventsFrag.absoluteClubName(club_name))
                .setContentText(getNotifTitle(new_update) + EventsFrag.absoluteClubName(club_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(new_update.getMessage()))
                .setContentIntent(updateContentIntent(con,new_update,clubID,index,club_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("New Update by " + EventsFrag.absoluteClubName(club_name))
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
        updatesFrag.addFlags(EventsFrag.getFlags(clubName));
        updatesFrag.putExtra("CLUB_ID",clubID);
        updatesFrag.putExtra(ClubContract.NOTIF_UPDATE,true);

        Intent mainAct = new Intent(con,MainActivity.class);

        stackBuilder.addNextIntentWithParentStack(mainAct);
        stackBuilder.addNextIntent(updatesFrag);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(getClubIndex(clubID),PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private static String getNotifTitle(UpdateParcel new_update){
        String line = "New ";

        switch (new_update.getType()){
            case ClubContract.UpdatesConstants.UP_STATUS: return line + "Post by ";
            case ClubContract.UpdatesConstants.UP_LINK: return line + "Link by ";
            case ClubContract.UpdatesConstants.UP_PHOTO: return line + "Photo uploaded by ";
            case ClubContract.UpdatesConstants.UP_VIDEO: return line + "Video uploaded by ";
        }
        return "New Update by ";
    }

    public static int getClubIndex(String clubID){
        for(int k = 0; k< ClubContract.CLUB_IDS.length; k++){
            if(clubID.equals(ClubContract.CLUB_IDS[k]))
                return k;
        }
        return -1;
    }
}
