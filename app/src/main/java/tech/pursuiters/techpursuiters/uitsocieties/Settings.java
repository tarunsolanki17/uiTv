package tech.pursuiters.techpursuiters.uitsocieties;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Shubhi on 23-Dec-17.
 */

public class Settings extends AppCompatActivity {

    private boolean state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mobileCheck();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onRadioButtonClicked(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int id = radioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.cellular:
                state=true;
                mobileCheck();
                toggleMobileDataConnection(true);

            case R.id.wifi:

                try {
                    WifiManager myWifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    myWifimanager.setWifiEnabled(false);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Oops! You're not connected to a Wi-fi network!", Toast.LENGTH_SHORT);
                }
                break;
        }}

    private void mobileCheck(){
        state=isMobileDataEnabled();
    }

    public boolean isMobileDataEnabled(){
        boolean mobileDataEnabled=false;
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // Some problem
        }
        return mobileDataEnabled;
    }

    public boolean toggleMobileDataConnection(boolean ON) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class
                    .forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass
                    .getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField
                    .get(conman);
            final Class iConnectivityManagerClass = Class
                    .forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
        }
        catch (Exception e) {
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
