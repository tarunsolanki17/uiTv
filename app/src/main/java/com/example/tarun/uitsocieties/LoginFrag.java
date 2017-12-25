package com.example.tarun.uitsocieties;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import static android.widget.Toast.makeText;
import static com.example.tarun.uitsocieties.InClub.events_data;
import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.InClub.photos_data;
import static com.example.tarun.uitsocieties.InClub.updates_data;
import static com.example.tarun.uitsocieties.InClub.videos_data;
import static com.example.tarun.uitsocieties.InClub.viewpgr;
import static com.example.tarun.uitsocieties.MainActivity.isConnectedStatic;
import static com.example.tarun.uitsocieties.R.id.loginbutt2;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFrag extends Fragment {

    CallbackManager callbackManager;
    LoginManager loginManager;
    Toast toast;

    public LoginFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View main_view = inflater.inflate(R.layout.fragment_login, container, false);

        //  TODO  --> ADD "NO INTERNET CONNCETION" AND SWIPE TO REFRESH BUTTON

        LinearLayout loginbutt2 = (LinearLayout) main_view.findViewById(R.id.loginbutt2);
        loginbutt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isConnectedStatic(getActivity())) {
                    switch (view.getId()) {
                        case R.id.loginbutt2:
                            if (AccessToken.getCurrentAccessToken() == null) {
                                login();
                            }
                        default:
                            break;
                    }
                }
                else {
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getActivity(), R.string.no_internet_toast, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("logging in---",loginResult.getAccessToken().toString());
                handleLoginResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.v("Cancel---","Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(),"There was an error logging in. Please try again later.",Toast.LENGTH_SHORT).show();
                Log.v("Login Error---",error.toString());
            }
        };

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager,facebookCallback);

    return main_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void login(){
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    public void handleLoginResult(AccessToken accessToken) {
        if(updates_data!=null)
            updates_data.clear();
        if(events_data!=null)
            events_data.clear();
        if(photos_data!=null)
            photos_data.clear();
        if(videos_data!=null)
            videos_data.clear();

        String text;
        Profile profile = Profile.getCurrentProfile();
        if(profile!=null && profile.getName() != null)
            text = "Logged in as: " + profile.getName();
        else
            text = "Logged in using Facebook";

        makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        login_checker();
        viewpgr.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Login",1);
    }

}
