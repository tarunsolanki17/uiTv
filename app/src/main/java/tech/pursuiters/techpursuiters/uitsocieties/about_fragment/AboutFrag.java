package tech.pursuiters.techpursuiters.uitsocieties.about_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tech.pursuiters.techpursuiters.uitsocieties.InClub;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AboutFrag extends Fragment {

    String theContactNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(tech.pursuiters.techpursuiters.uitsocieties.R.layout.fragment_about,container,false);

        TextView name = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.society_name);
        ImageView society_logo = (ImageView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.society_logo);
        ImageView mission_image= (ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.mission_image);
        ImageView about_image=(ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.about_image);
        ImageView contact = (ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.contact);
        ImageView fbpage=(ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.fbpage);
        ImageView webpage=(ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.webpage);
        ImageView mail= (ImageView)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.mail);

        TextView about = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.about);
        TextView objective = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.objective);
        TextView contact_number = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.contact_number);
        TextView e_mail = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.e_mail);
        final TextView fb_link = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.fblink);
        TextView website = (TextView) view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.website);


        AboutDataModel detail = AboutDataModel.details[InClub.position];
        name.setText(detail.getName());
        society_logo.setImageResource(detail.getImageResourceId());
        about.setText(detail.getAbout());
        objective.setText(detail.getObjective());
        contact_number.setText(detail.getNumber());
        website.setText(detail.getWebsite());
        e_mail.setText(detail.getEmail());
        fb_link.setText(detail.getFblink());

        String theAbout = about.getText().toString().trim();
        String theObjective = objective.getText().toString().trim();
        final String theContactNumber = contact_number.getText().toString();
        final String theEmail = e_mail.getText().toString();
        final String theWebsite = website.getText().toString();
        final String theFbLink=fb_link.getText().toString();

        LinearLayout mission_layout=(LinearLayout)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.mission_layout);
        LinearLayout about_layout=(LinearLayout)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.about_layout);
        LinearLayout contact_layout=(LinearLayout)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.contact_layout);
        LinearLayout mail_layout=(LinearLayout)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.mail_layout);
        LinearLayout webpage_layout = (LinearLayout)view.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.webpage_layout);

        if (theObjective.isEmpty()){
            mission_layout.setVisibility(View.GONE);
        }
        else{
            mission_layout.setVisibility(View.VISIBLE);
        }

        if(theAbout.isEmpty()){
            about_layout.setVisibility(View.GONE);
        }
        else {
            about_layout.setVisibility(View.VISIBLE);
        }

        if(theContactNumber.isEmpty()){
            contact_layout.setVisibility(View.GONE);
        }
        else {
            contact_layout.setVisibility(View.VISIBLE);
        }
        if(theEmail.isEmpty()){
            mail_layout.setVisibility(View.GONE);
        }
        else {
            mail_layout.setVisibility(View.VISIBLE);
        }
        if(theWebsite.isEmpty()){
            webpage_layout.setVisibility(View.GONE);

        }
        else{
            webpage_layout.setVisibility(View.VISIBLE);
        }

        final Toast toast = Toast.makeText(getApplicationContext(),"Opening Link",Toast.LENGTH_SHORT);

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(theWebsite));
                if((intent.resolveActivity(getActivity().getPackageManager())!=null))
                {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        fb_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(theFbLink));
                if((intent.resolveActivity(getActivity().getPackageManager())!=null)){
                    startActivity(intent);
                    toast.show();
                }
            }
        });
        

        e_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse(theEmail));
                if((intent.resolveActivity(getActivity().getPackageManager())!=null))
                    startActivity(intent);
            }
        });

        return view;
    }
}
