package com.example.tarun.uitsocieties.drawer;

/**
 * Created by Shubhi on 22-Dec-17.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarun.uitsocieties.R;

import static com.example.tarun.uitsocieties.R.string.cc;

public class App_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_app_info);

        final Toast toast = Toast.makeText(getApplicationContext(),"Opening Link",Toast.LENGTH_SHORT);

        TextView text_email = (TextView) findViewById(R.id.textEmail);
        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:techpursuiters@gmail.com"));
                startActivity(intent);
            }
        });

        TextView logo = (TextView)findViewById(R.id.logomaker);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String logoText = getString(R.string.logo);
                intent.setData(Uri.parse(logoText));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView roundicons = (TextView)findViewById(R.id.roundicons);
        roundicons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.roundicon);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView google = (TextView)findViewById(R.id.googleIcon);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.icons);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView freepik = (TextView) findViewById(R.id.freepik);
        freepik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.freepik);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView vectors = (TextView) findViewById(R.id.vectors);
        vectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.vector);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView budd = (TextView) findViewById(R.id.pixbuddha);
        budd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.buddha);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView flaticons = (TextView) findViewById(R.id.flaticons);
        flaticons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.flaticon);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView cc = (TextView)findViewById(R.id.creativeCommons);
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(R.string.cc);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}