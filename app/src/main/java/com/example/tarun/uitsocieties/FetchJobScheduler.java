package com.example.tarun.uitsocieties;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import static java.security.AccessController.getContext;

/**
 * Created by Tarun on 06-Oct-17.
 */

public class FetchJobScheduler {

    public static int HOURS = 3;
    public static int SECONDS = (int) TimeUnit.HOURS.toSeconds(HOURS);
    public static String TAG = "JobTag";
    public static FirebaseJobDispatcher dispatcher;

    private static boolean sInitialized;

    synchronized public static void scheduleFetching(Context con){
        Log.v("Running---","SCHEDULAR");

        if(sInitialized)
            return;
        Driver driver = new GooglePlayDriver(con);
        dispatcher = new FirebaseJobDispatcher(driver);
        Job fetchingJob = dispatcher.newJobBuilder()
                .setService(FetchingJobService.class)
                .setTag(TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(5,SECONDS))
                .build();

        dispatcher.schedule(fetchingJob);
        sInitialized = true;
        Log.v("Schedular-------","Dispatched");
    }

}
