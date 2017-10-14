package com.example.tarun.uitsocieties;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import static android.R.attr.id;
import static android.R.attr.lines;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.tarun.uitsocieties.ClubContract.CLUB_IDS;
import static com.example.tarun.uitsocieties.ClubContract.CLUB_NAMES;
import static com.example.tarun.uitsocieties.ClubContract.E_CELL;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.ID;
import static com.example.tarun.uitsocieties.ClubContract.GREEEN_ARMY;
import static com.example.tarun.uitsocieties.ClubContract.INSYNC;
import static com.example.tarun.uitsocieties.ClubContract.PHOENIX;
import static com.example.tarun.uitsocieties.ClubContract.SUNDARBAN;
import static com.example.tarun.uitsocieties.EventsFrag.eventNotification;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.R.drawable.c;
import static com.example.tarun.uitsocieties.R.drawable.d;
import static java.security.AccessController.getContext;

import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.CITY;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.COVER;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.DESCRIPTION;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.END_TIME;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LATITUDE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LOCATION;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.LONGITUDE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.NAME;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.PLACE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.SOURCE_URL;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.START_TIME;
import static com.example.tarun.uitsocieties.ClubContract.COHERENT;

/**
 * Created by Tarun on 05-Oct-17.
 */

public class FetchingJobService extends JobService {

    private AsyncTask fetchAsyncTask;
    File events_ids;
    public ArrayList<EventsDataModel> events_data_service;
//    String id;
    PrintWriter printWriter;
    private ArrayList<DoubleStrings> fresh_event_ids;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        fetchAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.v("Running---","JobService");

                /********************** INITIALISING IDS AND NAMES ARRAY *************************/



                events_ids = new File(getApplicationContext().getFilesDir(),"event_ids_file");
                Log.v("CLUB_IDS SIZE----:",String.valueOf(CLUB_IDS.length));
                fresh_event_ids = new ArrayList<>(CLUB_IDS.length);
                fresh_event_ids.clear();
                boolean file_created = false;

                if(!events_ids.exists()){
                    try {
                        file_created = events_ids.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(file_created==true||events_ids.length()==0){
                    for (int i = 0; i < 6 /*CLUB_IDS.size()*/; i++) {
                        Log.v("First Loop---", String.valueOf(i));
                        eventIDJSONRequest(CLUB_IDS[i], CLUB_NAMES[i], true, i);
                    }
                }
                else if(events_ids.exists()&&events_ids.length()>0){
                    for (int i = 0; i < 6 /*CLUB_IDS.size()*/; i++){
                        eventIDJSONRequest(CLUB_IDS[i],CLUB_NAMES[i],false, i);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters,false);
            }
        };
        fetchAsyncTask.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.v("onStopJob---","Called");
        if(fetchAsyncTask!=null)
            fetchAsyncTask.cancel(true);
        return true;
    }

    public void eventJSONRequest(String club_id) {
        //  TODO --> PUT A LIMIT TO THE ESTABLISHMENT OF THE CONNECTION OF AROUND 10-15 SECS
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + club_id + "/events",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        Log.v("JSON Response---", response.toString());
//                        events_data_service = parseFullJSONData(response.getJSONObject());

                        //  TODO --> HANDLE RESPONSE CODES
//                        response.getResponseCode()
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,start_time,end_time,place,type,description,cover");
        request.setParameters(parameters);
        request.executeAsync();
    }


    synchronized private void eventIDJSONRequest(final String club_id,final String club_name, final boolean new_file,final int index){

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+club_id+"/events",
                new GraphRequest.Callback() {
                    @Override
                     synchronized public void onCompleted(GraphResponse response) {
                        if(new_file) {
                            Log.v("eventIDJSONReq---",String.valueOf(index));
                            readNewID(response.getJSONObject(), index);
                            if(fresh_event_ids.size()==CLUB_NAMES.length){
                                writeFile();
                            }
                        }
                        else{
                            readNewID(response.getJSONObject(), index);
                            if(fresh_event_ids.size()==CLUB_NAMES.length) {
                                compareIDs();
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    synchronized private void readNewID(JSONObject response, int index){
        if (response != null && response.length() > 0) {
            try {
                JSONArray data = response.getJSONArray("data");
                if(data.length()>0) {
                    JSONObject topmost_event = data.getJSONObject(0);

                    if (topmost_event.has(ID) && !topmost_event.isNull(ID)) {
                        String id = topmost_event.getString(ID);
                        String lineToBeAdded = CLUB_NAMES[index] + " " + id;
                        boolean added = fresh_event_ids.add(new DoubleStrings(CLUB_NAMES[index],id));
                        if(added){
                            Log.v("Added---",lineToBeAdded);
                        }
                    }
                }
                else{
                    fresh_event_ids.add(new DoubleStrings(CLUB_NAMES[index], "-1"));
                    Log.v("Added---",CLUB_NAMES[index] + " " + "-1");
                }
            }
            catch (Exception e) {
                Log.v("Exception---", e.toString());
            }
        }

    }

    public class DoubleStrings  {
        public String name;
        public String id;

        DoubleStrings(String name,String id){
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
    public void readFile(){
        File eventids = new File(getApplicationContext().getFilesDir(),"event_ids_file");
        Log.v("Length2---",String.valueOf(eventids.length()));
        InputStream inStream;
        try {
            inStream = openFileInput(eventids.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

            String line;
            while ((line = bufferedReader.readLine())!=null){
                Log.v("FileLine----",line);
//                String[] old_id = line.split(" ");
//                Log.v("old_id----",old_id[0] + " " + old_id[1]);
            }
            bufferedReader.close();
        }
        catch (Exception e) {
            Log.v("Exception---","Caught");
            e.printStackTrace();
        }
    }
    private void writeFile(){
        String line = "nothing";
        try {
            printWriter = new PrintWriter(events_ids);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < CLUB_IDS.length; i++){
            Log.v("Second Loop---",String.valueOf(i));
            if(!fresh_event_ids.isEmpty()){
                Log.v("Length",String.valueOf(fresh_event_ids.size()));
            }
            line = fresh_event_ids.get(i).getName() + " " + fresh_event_ids.get(i).getId();
            Log.v("String Line---",line);
            printWriter.println(line);
        }
        printWriter.close();
        readFile();
    }
    private void compareIDs(){
        Log.v("Comparision---","Started");
        readFile();
        ArrayList<DoubleStrings> existing_ids = new ArrayList<>();
        File old_ids_file = new File("event_ids_file");
        InputStream inStream;
        try {
            inStream = openFileInput(old_ids_file.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while((line = bufferedReader.readLine())!=null){
                String[] old_data = line.split(" ");
                existing_ids.add(new DoubleStrings(old_data[0],old_data[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PrintWriter printWriter = new PrintWriter(old_ids_file);
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v("Size----","Existing: "+existing_ids.size()+"  Fresh: " + fresh_event_ids.size());
        if(existing_ids.size()==fresh_event_ids.size()){
            for(int x=0;x<existing_ids.size();x++){
                for(int y=0;y<fresh_event_ids.size();y++)
                {
                    if(existing_ids.get(x).getName().equals(fresh_event_ids.get(y).getName())){
                        if(existing_ids.get(x).getId().equals(fresh_event_ids.get(y).getId())){
                            writeFile();
                            Log.v("Result---","No Notification");
                            writeLogFile(false,existing_ids.get(x).getId(),fresh_event_ids.get(y).getId());
                        }
                        else{
                            writeFile();
                            Log.v("Result---","Notification");
                            //  TODO  --> FETCH FULL DATA OF THAT EVENT AND THEN PASS TO THE NOTIFICATION
                            //  TODO  --> ADD A BACK STACK WHICH WILL OPEN MAIN ACTIVITY IF BACK IS PRESSED
                            //  TODO  --> NAME OF THE CLUB TO BE SHOWN AS THE TITLE AND NOT THE APP
                            writeLogFile(true,existing_ids.get(x).getId(),fresh_event_ids.get(y).getId());
                            String clubID = sendClubID(fresh_event_ids.get(y).getName());
                            singleJSONRequest(clubID,existing_ids.get(x).getId(),fresh_event_ids.get(y).getName());
                            //  *******************   END   **********************
                        }
                        readLogFile();
                    }
                }
            }
        }
    }

    private String sendClubID(String clubName){
        if(clubName.equals("COHERENT")){
            return COHERENT;
        }
        else if(clubName.equals("E_CELL")){
            return E_CELL;
        }
        else if(clubName.equals("GREEN_ARMY")){
            return GREEEN_ARMY;
        }
        else if(clubName.equals("INSYNC")){
            return INSYNC;
        }
        else if(clubName.equals("PHOENIX")){
            return PHOENIX;
        }
        else if(clubName.equals("SUNDARBAN")){
            return SUNDARBAN;
        }
        else
            return "";
    }

    private void singleJSONRequest(final String clubID, final String existing_old_id, final String club_name){
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+ clubID +"/events",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        Log.v("Notification JSON---",response.toString());
                        onDataFetched(response.getJSONObject(), existing_old_id, club_name, clubID);
                        //  TODO --> HANDLE RESPONSE CODES
//                        response.getResponseCode()
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,start_time,end_time,place,type,description,cover");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /********************* FETCHES DATA FOR ALL THE NEW EVENTS AND THEN ISSUES THE NOTIFICATION  ****************/

    private void onDataFetched(JSONObject response,String existing_old_id, String club_name, String clubID){

        ArrayList<EventsDataModel> new_events = new ArrayList<>();
        boolean no_notif = false;
        if(response!=null&&response.length()>0){
            try{
                JSONArray data = response.getJSONArray("data");

                for(int i=0;i<data.length();i++) {
                    String event_id;
                    String event_name = "";
                    String start_date = "";
                    String end_date = "";
                    String year = "";
                    String month = "";
                    String date = "";
                    String day = "";
                    String time = "";
                    String place_name = "";
                    String city = "";
                    Double latitude = -1d;
                    Double longitude = -1d;
                    String descp = "";
                    String cover_source = "";

                    JSONObject curr_event = data.getJSONObject(i);

                    if(curr_event.has(ID) && !curr_event.isNull(ID)){
                        event_id = curr_event.getString(ID);
                        if(event_id.equals(existing_old_id))
                            no_notif = true;
                    }
                    if(no_notif)
                        break;

                    if (curr_event.has(NAME) && !curr_event.isNull(NAME)) {
                        event_name = curr_event.getString(NAME);
                    }
                    if (curr_event.has(START_TIME) && !curr_event.isNull(START_TIME)) {
                        start_date = curr_event.getString(START_TIME);

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(start_date);

                        start_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                        year = new SimpleDateFormat("yyyy").format(d);
                        month = new SimpleDateFormat("MMM").format(d);
                        date = new SimpleDateFormat("dd").format(d);
                        day = new SimpleDateFormat("EEE").format(d);
                        time = new SimpleDateFormat("h:mm a").format(d);

                    }
                    if (curr_event.has(END_TIME) && !curr_event.isNull(END_TIME)) {
                        end_date = curr_event.getString(END_TIME);

                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date d = incoming.parse(end_date);

                        end_date = new SimpleDateFormat(" EEEE, dd MMMM yyyy", java.util.Locale.getDefault()).format(d);
                    }
                    Log.v("name---", event_name);

                    if (curr_event.has(PLACE) && !curr_event.isNull(PLACE)) {
                        JSONObject place_obj = curr_event.getJSONObject(PLACE);

                        if (place_obj.has(NAME) && !place_obj.isNull(NAME)) {
                            place_name = place_obj.getString(NAME);
                        }

                        if (place_obj.has(LOCATION) && !place_obj.isNull(LOCATION)) {
                            JSONObject location = place_obj.getJSONObject(LOCATION);

                            if (location.has(CITY) && !location.isNull(CITY)) {
                                city = location.getString(CITY);
                            }
                            if (location.has(LATITUDE) && !location.isNull(LATITUDE)) {
                                latitude = location.getDouble(LATITUDE);
                            }
                            if (location.has(LONGITUDE) && !location.isNull(LONGITUDE)) {
                                longitude = location.getDouble(LONGITUDE);
                            }
                        }
                    }
                    if (curr_event.has(DESCRIPTION) && !curr_event.isNull(DESCRIPTION)) {
                        descp = curr_event.getString(DESCRIPTION);
                    }

                    if (curr_event.has(COVER) && !curr_event.isNull(COVER)) {
                        JSONObject cover_obj = curr_event.getJSONObject(COVER);

                        if (cover_obj.has(SOURCE_URL) && !cover_obj.isNull(SOURCE_URL)) {
                            cover_source = cover_obj.getString(SOURCE_URL);
                        }
                    }

                    new_events.add(new EventsDataModel(event_name, start_date, end_date, year, month, date, day, time, place_name, city, latitude, longitude, descp, cover_source));

                    if(new_events!=null){
                        Log.v("New Event---","Sent");
                        eventNotification(getApplicationContext(), new_events, club_name, clubID, i);
                    }
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void writeLogFile(boolean notif,String oldID,String freshID){
        File logFile = new File(getApplicationContext().getFilesDir(),"LogFile");
        PrintWriter logPrintWriter;
        boolean created;
        if(!logFile.exists()){
            try {
                created = logFile.createNewFile();
                Log.v("Log File Created---",String.valueOf(created));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
//            logPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile.getName())));
            FileOutputStream fOut = openFileOutput(logFile.getName(),MODE_APPEND);
            Date d = new Date();
            String curr_date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String line;
            if (!notif) {
                line = d.toString() + " No Notification" + " Old ID: " + oldID + " New ID:" + freshID + "\n";
            } else {
                line = d.toString() + " Notification" + " Old ID: " + oldID + " New ID:" + freshID + "\n";
            }
            Log.v("Log line---",line);
            fOut.write(line.getBytes());
            fOut.close();
//            logPrintWriter.println(line);
//            logPrintWriter.close();
        }
        catch (Exception e){
            Log.v("PrintException---","True");
            e.printStackTrace();
        }
    }
    private void readLogFile(){
        File logFile = new File(getApplicationContext().getFilesDir(),"LogFile");
        InputStream inStream;
        try{
            Log.v("Log File Length---",String.valueOf(logFile.length()));
            inStream = openFileInput(logFile.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while((line=bufferedReader.readLine())!=null){
                Log.v("Log File---",line);
            }
            bufferedReader.close();
            inStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

