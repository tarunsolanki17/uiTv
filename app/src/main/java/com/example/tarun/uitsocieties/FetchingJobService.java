package com.example.tarun.uitsocieties;

import com.example.tarun.uitsocieties.updates_fragment.UpdateParcel;
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
import static com.example.tarun.uitsocieties.ClubContract.GREEN_ARMY;
import static com.example.tarun.uitsocieties.ClubContract.INSYNC;
import static com.example.tarun.uitsocieties.ClubContract.PHOENIX;
import static com.example.tarun.uitsocieties.ClubContract.SUNDARBAN;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.CAPTION;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.FULL_PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.MESSAGE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.PERMA_LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.SOURCE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.STATUS_TYPE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.TYPE;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_EVENT;
import static com.example.tarun.uitsocieties.EventsFrag.eventNotification;
import static com.example.tarun.uitsocieties.InClub.club_id;

import static com.example.tarun.uitsocieties.updates_fragment.UpdatesFrag.updateNotification;
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

    private AsyncTask fetchAsyncTask, fetchAsyncUpdate;
    File events_ids, update_ids;
    public ArrayList<EventsDataModel> events_data_service;
    //    String id;
    PrintWriter printWriter, printWriterUpdate;
    private ArrayList<DoubleStrings> fresh_event_ids, fresh_update_ids;


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        fetchAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.v("Running---", "JobService");

                /*events_ids = new File(getApplicationContext().getFilesDir(), "event_ids_file");
                Log.v("CLUB_IDS SIZE----:", String.valueOf(CLUB_IDS.length));
                fresh_event_ids = new ArrayList<>(CLUB_IDS.length);
                fresh_event_ids.clear();
                boolean file_created = false;

                if (!events_ids.exists()) {
                    try {
                        file_created = events_ids.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file_created || events_ids.length() == 0) {
                    for (int i = 0; i < CLUB_IDS.length; i++) {
                        Log.v("First Loop---", String.valueOf(i));
                        eventIDJSONRequest(CLUB_IDS[i], CLUB_NAMES[i], true, i);
                    }
                } else if (events_ids.exists() && events_ids.length() > 0) {
                    for (int i = 0; i < CLUB_IDS.length; i++) {
                        eventIDJSONRequest(CLUB_IDS[i], CLUB_NAMES[i], false, i);
                    }
                }*/

                fetchUpdate();      /**     UPDATES FETCHING    **/

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.v("onPostExecute---", "Running");
                jobFinished(jobParameters, false);
            }
        };
        fetchAsyncTask.execute();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.v("onStopJob---", "Called");
        if (fetchAsyncTask != null)
            fetchAsyncTask.cancel(true);
        if (fetchAsyncUpdate != null)
            fetchAsyncUpdate.cancel(true);
        return true;
    }

    synchronized private void eventIDJSONRequest(final String club_id, final String club_name, final boolean new_file, final int index) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + club_id + "/events",
                new GraphRequest.Callback() {
                    @Override
                    synchronized public void onCompleted(GraphResponse response) {
                        if (new_file) {
                            Log.v("eventIDJSONReq---", String.valueOf(index));
                            readNewID(response.getJSONObject(), index);
                            if (fresh_event_ids.size() == CLUB_NAMES.length) {
                                writeFile();
                            }
                        } else {
                            readNewID(response.getJSONObject(), index);
                            if (fresh_event_ids.size() == CLUB_NAMES.length) {
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

    synchronized private void readNewID(JSONObject response, int index) {
        if (response != null && response.length() > 0) {
            try {
                JSONArray data = response.getJSONArray("data");
                if (data.length() > 0) {
                    JSONObject topmost_event = data.getJSONObject(0);

                    if (topmost_event.has(ID) && !topmost_event.isNull(ID)) {
                        String id = topmost_event.getString(ID);
                        String lineToBeAdded = CLUB_NAMES[index] + " " + id;
                        boolean added = fresh_event_ids.add(new DoubleStrings(CLUB_NAMES[index], id));
                        if (added) {
                            Log.v("Added---", lineToBeAdded);
                        }
                    }
                } else {
                    fresh_event_ids.add(new DoubleStrings(CLUB_NAMES[index], "-1"));
                    Log.v("Added---", CLUB_NAMES[index] + " " + "-1");
                }
            } catch (Exception e) {
                Log.v("Exception---", e.toString());
            }
        }

    }

    public class DoubleStrings {
        public String name;
        public String id;

        DoubleStrings(String name, String id) {
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

    public void readFile() {
        File eventids = new File(getApplicationContext().getFilesDir(), "event_ids_file");
        Log.v("Length2---", String.valueOf(eventids.length()));
        InputStream inStream;
        try {
            inStream = openFileInput(eventids.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.v("FileLine----", line);
//                String[] old_id = line.split(" ");
//                Log.v("old_id----",old_id[0] + " " + old_id[1]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.v("Exception---", "Caught");
            e.printStackTrace();
        }
    }

    private void writeFile() {
        String line = "nothing";
        try {
            printWriter = new PrintWriter(events_ids);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < CLUB_IDS.length; i++) {
            Log.v("Second Loop---", String.valueOf(i));
            if (!fresh_event_ids.isEmpty()) {
                Log.v("Length", String.valueOf(fresh_event_ids.size()));
            }
            line = fresh_event_ids.get(i).getName() + " " + fresh_event_ids.get(i).getId();
            Log.v("String Line---", line);
            printWriter.println(line);
        }
        printWriter.close();
//        readFile();
    }

    private void compareIDs() {
        Log.v("Comparision---", "Started");
        readFile();
        ArrayList<DoubleStrings> existing_ids = new ArrayList<>();
        File old_ids_file = new File("event_ids_file");
        InputStream inStream;
        try {
            inStream = openFileInput(old_ids_file.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] old_data = line.split(" ");
                existing_ids.add(new DoubleStrings(old_data[0], old_data[1]));
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

        Log.v("Size----", "Existing: " + existing_ids.size() + "  Fresh: " + fresh_event_ids.size());
        if (existing_ids.size() == fresh_event_ids.size()) {
            for (int x = 0; x < existing_ids.size(); x++) {
                for (int y = 0; y < fresh_event_ids.size(); y++) {
                    if (existing_ids.get(x).getName().equals(fresh_event_ids.get(y).getName())) {
                        if (existing_ids.get(x).getId().equals(fresh_event_ids.get(y).getId())) {
                            writeFile();
                            Log.v("Result---", "No Notification");
                            writeLogFile(false, existing_ids.get(x).getId(), fresh_event_ids.get(y).getId());
                        } else {
                            writeFile();
                            Log.v("Result---", "Notification");
                            //  TODO  --> ADD A BACK STACK WHICH WILL OPEN MAIN ACTIVITY IF BACK IS PRESSED
                            //  TODO  --> NAME OF THE CLUB TO BE SHOWN AS THE TITLE AND NOT THE APP
                            writeLogFile(true, existing_ids.get(x).getId(), fresh_event_ids.get(y).getId());
                            String clubID = sendClubID(fresh_event_ids.get(y).getName());
                            singleJSONRequest(clubID, existing_ids.get(x).getId(), fresh_event_ids.get(y).getName());
                            //  *******************   END   **********************
                        }
                        readLogFile();
                    }
                }
            }
        }
    }

    private String sendClubID(String clubName) {

        for (int k = 0; k < CLUB_NAMES.length; k++) {
            if (clubName.equals(CLUB_NAMES[k])) {
                return CLUB_IDS[k];
            }
        }
        return "";
    }

    private void singleJSONRequest(final String clubID, final String existing_old_id, final String club_name) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + clubID + "/events",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        Log.v("Notification JSON---", response.toString());
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

    private void onDataFetched(JSONObject response, String existing_old_id, String club_name, String clubID) {

        ArrayList<EventsDataModel> new_events = new ArrayList<>();
        boolean no_notif = false;
        if (response != null && response.length() > 0) {
            try {
                JSONArray data = response.getJSONArray("data");

                for (int i = 0; i <= 1; i++) {
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

                    if (curr_event.has(ID) && !curr_event.isNull(ID)) {
                        event_id = curr_event.getString(ID);
                        if (event_id.equals(existing_old_id))
                            no_notif = true;
                    }
                    if (no_notif)
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

                    if (new_events != null) {
                        Log.v("New Event---", "Sent");
//                        TODO --> ADD A CONDITION THAT THE NOTIFICATION SHOULD ONLY BE GENERATED WHEN THE EVENT IS ONLY A WEEK OR TWO OLD
                        Date today = new Date();
                        SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date event_date = incoming.parse(start_date);

                        if (event_date.compareTo(today) > 0)
                            eventNotification(getApplicationContext(), new_events, club_name, clubID, i);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeLogFile(boolean notif, String oldID, String freshID) {
        File logFile = new File(getApplicationContext().getFilesDir(), "LogFile");
        PrintWriter logPrintWriter;
        boolean created;
        if (!logFile.exists()) {
            try {
                created = logFile.createNewFile();
                Log.v("Log File Created---", String.valueOf(created));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
//            logPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile.getName())));
            FileOutputStream fOut = openFileOutput(logFile.getName(), MODE_APPEND);
            Date d = new Date();
            String curr_date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String line;
            if (!notif) {
                line = d.toString() + " No Notification" + " Old ID: " + oldID + " New ID:" + freshID + "\n";
            } else {
                line = d.toString() + " Notification" + " Old ID: " + oldID + " New ID:" + freshID + "\n";
            }
            Log.v("Log line---", line);
            fOut.write(line.getBytes());
            fOut.close();
//            logPrintWriter.println(line);
//            logPrintWriter.close();
        } catch (Exception e) {
            Log.v("PrintException---", "True");
            e.printStackTrace();
        }
    }

    private void readLogFile() {
        File logFile = new File(getApplicationContext().getFilesDir(), "LogFile");
        InputStream inStream;
        try {
            Log.v("Log File Length---", String.valueOf(logFile.length()));
            inStream = openFileInput(logFile.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.v("Log File---", line);
            }
            bufferedReader.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***********************************  UPDATE SECTION *********************************************/

    private void fetchUpdate() {
        Log.v("fetchUpdate--->>>","Running");
        update_ids = new File(getApplicationContext().getFilesDir(), "update_ids_file");
        fresh_update_ids = new ArrayList<>();
        fresh_update_ids.clear();
        boolean file_created = false;

        if (!update_ids.exists()) {
            try {
                file_created = update_ids.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file_created || update_ids.length() == 0) {
            for (int i = 0; i < CLUB_IDS.length; i++) {
                Log.v("First Loop Update---", String.valueOf(i));
                updateIDJSONRequest(CLUB_IDS[i], CLUB_NAMES[i], true, i);
            }
        } else if (update_ids.exists() && update_ids.length() > 0) {
            for (int i = 0; i < CLUB_IDS.length; i++) {
                updateIDJSONRequest(CLUB_IDS[i], CLUB_NAMES[i], false, i);
            }
        }

    }

    private void updateIDJSONRequest(final String club_id, final String club_name, final boolean new_file, final int index) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + club_id + "/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (new_file) {
                            Log.v("updateIDJSONReq---", String.valueOf(index));
                            readNewUpdateID(response.getJSONObject(), index);
                            if (fresh_update_ids.size() == CLUB_NAMES.length) {
                                writeUpdateFile();
                            }
                        }
                        else {
                                readNewUpdateID(response.getJSONObject(), index);
                                if (fresh_update_ids.size() == CLUB_NAMES.length) {
                                    compareUpdateIDs();
                                }
                            }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void readNewUpdateID(JSONObject response, int index) {
        if (response != null && response.length() > 0) {
            try {
                JSONArray data = response.getJSONArray("data");
                if (data.length() > 0) {
                    JSONObject topmost_update = data.getJSONObject(0);

                    if (topmost_update.has(ID) && !topmost_update.isNull(ID)) {
                        String id = topmost_update.getString(ID);
                        String lineToBeAdded = CLUB_NAMES[index] + " " + id;
                        boolean added = fresh_update_ids.add(new DoubleStrings(CLUB_NAMES[index], id));
                        if (added) {
                            Log.v("Added---", lineToBeAdded);
                        }
                    }
                } else {
                    fresh_update_ids.add(new DoubleStrings(CLUB_NAMES[index], "-1"));
                    Log.v("Added---", CLUB_NAMES[index] + " " + "-1");
                }
            } catch (Exception e) {
                Log.v("Exception---", e.toString());
            }
        }
    }

    private void writeUpdateFile() {
        String line = "nothing";
        try {
            printWriterUpdate = new PrintWriter(update_ids);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < CLUB_IDS.length; i++) {
            Log.v("Second Loop Update---", String.valueOf(i));
            if (!fresh_update_ids.isEmpty()) {
                Log.v("Length", String.valueOf(fresh_update_ids.size()));
            }
            line = fresh_update_ids.get(i).getName() + " " + fresh_update_ids.get(i).getId();
            Log.v("String Line---", line);
            printWriterUpdate.println(line);
        }
        printWriterUpdate.close();
    }

    public void readUpdateFile() {
        File updateids = new File(getApplicationContext().getFilesDir(), "update_ids_file");
        Log.v("Length2---", String.valueOf(updateids.length()));
        InputStream inStream;
        try {
            inStream = openFileInput(updateids.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.v("FileLine----", line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.v("Exception---", "Caught");
            e.printStackTrace();
        }
    }

    private void compareUpdateIDs() {
        Log.v("Comparision Update---", "Started");
        readUpdateFile();
        ArrayList<DoubleStrings> existing_ids = new ArrayList<>();
        File old_ids_file = new File("update_ids_file");
        InputStream inStream;
        try {
            inStream = openFileInput(old_ids_file.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] old_data = line.split(" ");
                existing_ids.add(new DoubleStrings(old_data[0], old_data[1]));
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

        Log.v("Size----", "Existing: " + existing_ids.size() + "  Fresh: " + fresh_update_ids.size());
        if (existing_ids.size() == fresh_update_ids.size()) {
            for (int x = 0; x < existing_ids.size(); x++) {
                for (int y = 0; y < fresh_update_ids.size(); y++) {
                    if (existing_ids.get(x).getName().equals(fresh_update_ids.get(y).getName())) {
                        if (existing_ids.get(x).getId().equals(fresh_update_ids.get(y).getId())) {
                            /**********************     NO NOTIFICATION     **************************/
                            writeUpdateFile();
                            Log.v("Result---", "No Notification");
// TODO --> WRITE LOG FILE -----------------------------------------------------------------------
//                            writeUpdateLogFile(false,existing_ids.get(x).getId(),fresh_update_ids.get(y).getId());
                        } else {
                            /**********************     ISSUE NOTIFICATION     **************************/
                            writeUpdateFile();
                            String clubID = sendClubID(fresh_update_ids.get(y).getName());
                            singleUpdateJSONRequest(clubID, existing_ids.get(x).getId(), fresh_update_ids.get(y).getName());
                            Log.v("Result---", "Notification");
                            //  *******************   END   **********************
                        }
                    }
                }
            }
        }
    }

    private void singleUpdateJSONRequest(final String clubID, final String existing_old_id, final String club_name) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + clubID + "/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.v("Notification JSON---", response.toString());
                        onUpdateDataFetched(response.getJSONObject(), existing_old_id, club_name, clubID);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,caption,created_time,description,link,message,name,permalink_url,picture,full_picture,place,properties,source,status_type,type");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onUpdateDataFetched(JSONObject response, String existing_old_id, String club_name, String clubID) {
        ArrayList<UpdateParcel> new_update = new ArrayList<>();
        boolean no_notif = false;

        if (response != null && response.length() > 0) {
            try {
                JSONArray data = response.getJSONArray("data");

                for (int i = 0; i <= 0; i++) {
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

                    if (curr_update.has(ID) && !curr_update.isNull(ID)) {
                        id = curr_update.getString(ID);
                    }
                    if (curr_update.has(CAPTION) && !curr_update.isNull(CAPTION)) {
                        caption = curr_update.getString(CAPTION);
                    }
                    if (curr_update.has(CREATED_TIME) && !curr_update.isNull(CREATED_TIME)) {
                        created_time = curr_update.getString(CREATED_TIME);
                    }
                    if (curr_update.has(DESCRIPTION) && !curr_update.isNull(DESCRIPTION)) {
                        description = curr_update.getString(DESCRIPTION);
                    }
                    if (curr_update.has(LINK) && !curr_update.isNull(LINK)) {
                        link = curr_update.getString(LINK);
                    }
                    if (curr_update.has(MESSAGE) && !curr_update.isNull(MESSAGE)) {
                        message = curr_update.getString(MESSAGE);
                    }
                    if (curr_update.has(NAME) && !curr_update.isNull(NAME)) {
                        name = curr_update.getString(NAME);
                    }
                    if (curr_update.has(PERMA_LINK) && !curr_update.isNull(PERMA_LINK)) {
                        permalink_url = curr_update.getString(PERMA_LINK);
                    }
                    if (curr_update.has(PICTURE) && !curr_update.isNull(PICTURE)) {
                        picture = curr_update.getString(PICTURE);
                    }
                    if (curr_update.has(FULL_PICTURE) && !curr_update.isNull(FULL_PICTURE)) {
                        full_picture = curr_update.getString(FULL_PICTURE);
                    }

                    if (curr_update.has(PLACE) && !curr_update.isNull(PLACE)) {
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

                    if (curr_update.has(SOURCE) && !curr_update.isNull(SOURCE)) {
                        source = curr_update.getString(SOURCE);
                    }
                    if (curr_update.has(STATUS_TYPE) && !curr_update.isNull(STATUS_TYPE)) {
                        status_type = curr_update.getString(STATUS_TYPE);
                    }
                    if (curr_update.has(TYPE) && !curr_update.isNull(TYPE)) {
                        type = curr_update.getString(TYPE);
                    }
                    if (type.equals(UP_EVENT))
                        continue;

                    new_update.add(new UpdateParcel(id, caption, created_time, description, link, message, name, permalink_url, picture, full_picture, place_name, city, source, status_type, type));

                    if (new_update != null) {
                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date update_date = format.parse(created_time);

                        int diffInDays = (int) ((today.getTime() - update_date.getTime()) / (1000 * 60 * 60 * 24));
                        Log.v("Diff in days---", String.valueOf(diffInDays));
                        if (diffInDays >= 0 && diffInDays <= 7)
                            updateNotification(getApplicationContext(), new_update, club_name, clubID, i);
                    }
                }


            } catch (Exception e) {
                Log.v("Excep UpdatParseJSON---", e.toString());
            }
        }
    }
}

