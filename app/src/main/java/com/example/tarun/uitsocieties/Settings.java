package com.example.tarun.uitsocieties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;

import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.InClub.viewpgr;
import static com.example.tarun.uitsocieties.MainActivity.isConnectedStatic;

/**
 * Created by Tarun on 15-Sep-17.
 */

public class Settings extends AppCompatActivity {

    TextView logout;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        logout = (TextView) findViewById(R.id.logout_butt);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedStatic(getApplicationContext())) {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(logout, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                    login_checker();
                                    viewpgr.getAdapter().notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(cancel, null);
                    builder.create().show();
                }
                else{
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getApplicationContext(), R.string.no_internet_toast, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

}
