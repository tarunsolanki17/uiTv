package com.example.tarun.uitsocieties;


import android.content.DialogInterface;
import android.content.Intent;
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

import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.InClub.viewpgr;
import static com.example.tarun.uitsocieties.R.id.loginbutt2;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFrag extends Fragment {

    CallbackManager callbackManager;
    LoginManager loginManager;

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
                switch (view.getId()){
                    case R.id.loginbutt2:
                        if(AccessToken.getCurrentAccessToken()==null) {
                            login();
                        }
                    default: break;
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
                Log.v("Error---",error.toString());
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
        Toast.makeText(getContext(), "Logged in", Toast.LENGTH_SHORT).show();
        login_checker();
        viewpgr.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Login",1);
    }
}
