package tech.pursuiters.techpursuiters.uitsocieties;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static tech.pursuiters.techpursuiters.uitsocieties.updates_fragment.UpdatesFrag.getClubIndex;

import com.facebook.HttpMethod;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFrag extends Fragment {
    
    EventsListAdapter eventsAdapter;
    ListView listView;
    ProgressBar pbar;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    Context con;
    int data_len;
    LinearLayout no_internet,no_data;
    ImageView swipe_text;

    public EventsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View events_view = inflater.inflate(R.layout.fragment_events, container, false);

        con = events_view.getContext();

        listView = events_view.findViewById(R.id.listview);
        pbar = events_view.findViewById(R.id.progress_bar_e);
        no_internet = events_view.findViewById(R.id.no_internet_e);
        no_data = events_view.findViewById(R.id.no_data_e);
        swipe = events_view.findViewById(R.id.swipe);
        swipe_text = events_view.findViewById(R.id.swipe_text_e);

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
                    if(InClub.fetchAsyncE!=null) {
                        InClub.fetchAsyncE.cancel(true);
                    }
                    if(InClub.events_data!=null)
                        InClub.events_data.clear();
                    else
                        InClub.events_data = new ArrayList<>();

                    pbar.setVisibility(VISIBLE);
                    listView.setVisibility(GONE);

                    if(eventsAdapter!=null)
                        eventsAdapter.notifyDataSetChanged();
                    eventJSONRequest();
                }
                swipe.setRefreshing(false);
            }
        });

        if(isConnectedFunc()){

            if(savedInstanceState!=null&&savedInstanceState.getInt("Login")==1)
                savedInstanceState = null;
            

            if (savedInstanceState != null) {
                if(savedInstanceState.getBoolean("Incomplete")) {
                    Log.v("fetchAync---<","was incomplete");
                    if (InClub.fetchAsyncE != null) {
                        Log.v("fetchAync---<","not null");
                        if (InClub.fetchAsyncE.getStatus().toString().equals("RUNNING")) {    /** fetchAsyncE still running*/
                            Log.v("fetchAync---<","still running");
                            pbar.setVisibility(VISIBLE);
                            listView.setVisibility(GONE);
                        }
                        if (InClub.fetchAsyncE.getStatus().toString().equals("FINISHED")) {   /** fetchAsyncE complete*/
                            Log.v("fetchAync---<","finished");
                            showing();
                        }
                    } else {
                        Log.v("fetchAync---<","is null");
                        Log.v("eventsdata---<",String.valueOf(InClub.events_data.size()));
                        showing();
                    }
                }
                else {
                    Log.v("fetchAync---<","had already finished");
                    InClub.events_data = savedInstanceState.getParcelableArrayList("events_parcel");
                    showing();
                }
            }
            else {
                InClub.login_checker();
                if(InClub.login) {
                    eventJSONRequest();
                }
            }
        }

        return events_view;
    }

    private void showing(){
        if (InClub.events_data != null)
            if (InClub.events_data.isEmpty()) {
                pbar.setVisibility(GONE);
                listView.setVisibility(GONE);
                no_data.setVisibility(VISIBLE);
                swipe_text.setVisibility(VISIBLE);
            } else {
                data_len = InClub.events_data.size();
                pbar.setVisibility(GONE);
                no_data.setVisibility(GONE);
                listView.setVisibility(VISIBLE);
                swipe_text.setVisibility(GONE);
                onDataFetched();
            }
    }

    public void eventJSONRequest(){
        data_len = 0;

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "name,start_time,end_time,place,type,description,cover");
        parameters.putString("limit","25");

        final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    parseJSONData(response.getJSONObject());
                    data_len = InClub.events_data.size();
                    Log.v("Event Res---",response.toString());
                    Log.v("data_len---",String.valueOf(data_len));
                    //  TODO --> HANDLE RESPONSE CODES

                    GraphRequest newRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                    if(newRequest!=null) {
                        newRequest.setGraphPath("/" + InClub.club_id + "/events");
                        newRequest.setCallback(this);
                        newRequest.setParameters(parameters);
                        newRequest.executeAndWait();
                    }
                }
                    catch (Exception e) {
                    Log.v("Excep* eventJSONReq---:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        InClub.fetchAsyncE = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + InClub.club_id + "/events",parameters, HttpMethod.GET, graphCallback).executeAndWait();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                onDataFetched();
            }
        };
        InClub.fetchAsyncE.execute();

    }

    public void parseJSONData(JSONObject response){

        if(response!=null&&response.length()>0){
            try{
                JSONArray data = response.getJSONArray("data");
                String event_name;
                String start_date;
                String end_date;
                String year;
                String month;
                String date;
                String day;
                String time;
                String place_name;
                String city;
                Double latitude ;
                Double longitude ;
                String descp;
                String cover_source;
                for(int i=0;i<data.length();i++){
                    event_name = "";
                    start_date = "";
                    end_date = "";
                    year = "";
                    month = "";
                    date = "";
                    day = "";
                    time = "";
                    place_name = "";
                    city = "";
                    latitude = -1d;
                    longitude = -1d;
                    descp = "";
                    cover_source = "";

                    JSONObject curr_event = data.getJSONObject(i);

                    if(curr_event.has(ClubContract.EventsConstants.NAME)&&!curr_event.isNull(ClubContract.EventsConstants.NAME)){
                        event_name = curr_event.getString(ClubContract.EventsConstants.NAME);
                    }
                    if(curr_event.has(ClubContract.EventsConstants.START_TIME)&&!curr_event.isNull(ClubContract.EventsConstants.START_TIME)){
                        start_date = curr_event.getString(ClubContract.EventsConstants.START_TIME);

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(start_date);

                        start_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                        year = new SimpleDateFormat("yyyy").format(d);
                        month = new SimpleDateFormat("MMM").format(d);
                        date = new SimpleDateFormat("dd").format(d);
                        day = new SimpleDateFormat("EEE").format(d);
                        time = new SimpleDateFormat("h:mm a").format(d);

                    }
                    if(curr_event.has(ClubContract.EventsConstants.END_TIME)&&!curr_event.isNull(ClubContract.EventsConstants.END_TIME)){
                        end_date = curr_event.getString(ClubContract.EventsConstants.END_TIME);

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(end_date);

                        end_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                    }

                    if(curr_event.has(ClubContract.EventsConstants.PLACE)&&!curr_event.isNull(ClubContract.EventsConstants.PLACE)) {
                        JSONObject place_obj = curr_event.getJSONObject(ClubContract.EventsConstants.PLACE);

                        if (place_obj.has(ClubContract.EventsConstants.NAME) && !place_obj.isNull(ClubContract.EventsConstants.NAME)) {
                            place_name = place_obj.getString(ClubContract.EventsConstants.NAME);
                        }

                        if(place_obj.has(ClubContract.EventsConstants.LOCATION)&&!place_obj.isNull(ClubContract.EventsConstants.LOCATION)) {
                            JSONObject location = place_obj.getJSONObject(ClubContract.EventsConstants.LOCATION);

                            if (location.has(ClubContract.EventsConstants.CITY) && !location.isNull(ClubContract.EventsConstants.CITY)) {
                                city = location.getString(ClubContract.EventsConstants.CITY);
                            }
                            if (location.has(ClubContract.EventsConstants.LATITUDE) && !location.isNull(ClubContract.EventsConstants.LATITUDE)) {
                                latitude = location.getDouble(ClubContract.EventsConstants.LATITUDE);
                            }
                            if (location.has(ClubContract.EventsConstants.LONGITUDE) && !location.isNull(ClubContract.EventsConstants.LONGITUDE)) {
                                longitude = location.getDouble(ClubContract.EventsConstants.LONGITUDE);
                            }
                        }
                    }
                    if(curr_event.has(ClubContract.EventsConstants.DESCRIPTION)&&!curr_event.isNull(ClubContract.EventsConstants.DESCRIPTION)){
                        descp = curr_event.getString(ClubContract.EventsConstants.DESCRIPTION);
                    }

                    if(curr_event.has(ClubContract.EventsConstants.COVER)&&!curr_event.isNull(ClubContract.EventsConstants.COVER)) {
                        JSONObject cover_obj = curr_event.getJSONObject(ClubContract.EventsConstants.COVER);

                        if (cover_obj.has(ClubContract.EventsConstants.SOURCE_URL) && !cover_obj.isNull(ClubContract.EventsConstants.SOURCE_URL)) {
                            cover_source = cover_obj.getString(ClubContract.EventsConstants.SOURCE_URL);
                        }
                    }
                    if(!InClub.fetchAsyncE.isCancelled())
                    InClub.events_data.add(new EventsDataModel(event_name, start_date, end_date, year,month, date, day, time, place_name, city, latitude, longitude, descp, cover_source));

                    Log.v("eventsdata---<",String.valueOf(InClub.events_data.size()));
                }


            }
            catch (Exception e){
                Log.v("Exception---",e.toString());
            }
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
            no_internet.setVisibility(VISIBLE);
            swipe_text.setVisibility(VISIBLE);
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

        if(InClub.fetchAsyncE!=null){
            if(InClub.fetchAsyncE.getStatus().toString().equals("RUNNING"))
                outState.putBoolean("Incomplete",true);
            else if(InClub.fetchAsyncE.getStatus().toString().equals("FINISHED")) {
                outState.putParcelableArrayList("events_parcel", InClub.events_data);
                outState.putBoolean("Incomplete", false);
            }
        }
        else {
            outState.putParcelableArrayList("events_parcel", InClub.events_data);
            outState.putBoolean("Incomplete", false);
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
            listView.setVisibility(VISIBLE);
            swipe_text.setVisibility(GONE);

            eventsAdapter = new EventsListAdapter(con, InClub.events_data);
            listView.setAdapter(eventsAdapter);
        }
    }

    public static void eventNotification(Context con, final ArrayList<EventsDataModel> new_events, String club_name, String clubID, int index){
        EventsDataModel new_event = new_events.get(index);
        String date_time = "on " + new_event.getDate() + " " + new_event.getMonth();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(con)
                .setSmallIcon(R.drawable.ic_notification)
                //  TODO --> SET LARGE ICON ACCORDING TO THE CLUB LOGO
                .setLargeIcon(BitmapFactory.decodeResource(con.getResources(),R.mipmap.ic_launcher))
                .setContentTitle(new_event.getEvent_name())
                .setContentText("New event by " + absoluteClubName(club_name) + " " + date_time)
                .setColor(ContextCompat.getColor(con,R.color.colorPrimary))
                .setContentIntent(contentIntent(con,new_event,clubID,index,club_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("New Event by " + absoluteClubName(club_name))
                .setAutoCancel(true);

        if(new_event.getCover_url().isEmpty()||new_event.getCover_url()==null){
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(new_event.getDescp()));
        }
        else{
            Bitmap image = null;
            try{
                URL cover_url = new URL(new_event.getCover_url());
                image = BitmapFactory.decodeStream(cover_url.openConnection().getInputStream());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(image!=null)
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) con.getSystemService(con.NOTIFICATION_SERVICE);
        Log.v("Notification---:","Issue");
        notificationManager.notify(getClubIndex(clubID)+100,builder.build());
    }

    public static PendingIntent contentIntent(Context con, EventsDataModel new_event, String clubID, int index, String clubName){

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(con);

        Intent detailedEvent = new Intent(con,EventsDetailedActivity.class);
        detailedEvent.putExtra("CLUB_ID",clubID);
        detailedEvent.putExtra("Notification",true);
        detailedEvent.putExtra("New Event Parcel",new_event);

        Intent eventsFrag = new Intent(con,InClub.class);
        //  TODO --> SET CORRECT FLAG FOR CLUBS
        eventsFrag.addFlags(getFlags(clubName));
        eventsFrag.putExtra("CLUB_ID",clubID);
        eventsFrag.putExtra(ClubContract.NOTIF_EVENT,true);

        Intent mainAct = new Intent(con,MainActivity.class);

        stackBuilder.addNextIntentWithParentStack(mainAct);
        stackBuilder.addNextIntent(eventsFrag);
        stackBuilder.addNextIntent(detailedEvent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(index,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static int getFlags(String clubName){
            for(int i = 0; i< ClubContract.CLUB_NAMES.length; i++)
            if(clubName.equals(ClubContract.CLUB_NAMES[i]))
                return i;

        return -1;
    }

    public static String absoluteClubName(String club_name){
        for(int i = 0; i< ClubContract.CLUB_NAMES_VISIBLE.length; i++){
            if(club_name.equals(ClubContract.CLUB_NAMES[i]))
                return ClubContract.CLUB_NAMES_VISIBLE[i];
        }
        return "a Society";
    }

    public static class NotificationBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("BCast Receiver---","Running");
            if(AccessToken.getCurrentAccessToken()!=null)
                FetchJobScheduler.scheduleFetching(context);
        }
    }
}
