package com.example.tarun.uitsocieties.about_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarun.uitsocieties.R;

import static com.example.tarun.uitsocieties.InClub.position;

public class AboutFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about,container,false);

        TextView name = (TextView) view.findViewById(R.id.society_name);
        ImageView society_logo = (ImageView) view.findViewById(R.id.society_logo);

        TextView about = (TextView) view.findViewById(R.id.about);
        String theAbout = about.getText().toString().trim();

        TextView objective = (TextView) view.findViewById(R.id.objective);
        String theObjective = objective.getText().toString().trim();

        TextView contact_number = (TextView) view.findViewById(R.id.contact_number);
        String theContactNumber = (contact_number.getText().toString().trim());

        TextView e_mail = (TextView) view.findViewById(R.id.mail);
        String theEmail = e_mail.getText().toString().trim();

        TextView fb_link = (TextView) view.findViewById(R.id.fblink);

        TextView website = (TextView) view.findViewById(R.id.website);
        String theWebsite = website.getText().toString().trim();

        TextView webpage = (TextView) view.findViewById(R.id.webpage);
        TextView contact = (TextView) view.findViewById(R.id.contact);
        TextView fbpage = (TextView) view.findViewById(R.id.fbpage);
        TextView email = (TextView) view.findViewById(R.id.email);

        AboutDataModel detail = AboutDataModel.details[position];
        name.setText(detail.getName());
        society_logo.setImageResource(detail.getImageResourceId());
        about.setText(detail.getAbout());
        objective.setText(detail.getObjective());
        contact_number.setText(detail.getNumber());
        website.setText(detail.getWebsite());
        e_mail.setText(detail.getEmail());
        fb_link.setText(detail.getFblink());

        return view;
    }
}
