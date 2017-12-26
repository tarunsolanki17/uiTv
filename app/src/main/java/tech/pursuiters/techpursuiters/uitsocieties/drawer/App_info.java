package tech.pursuiters.techpursuiters.uitsocieties.drawer;

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

public class App_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(tech.pursuiters.techpursuiters.uitsocieties.R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(tech.pursuiters.techpursuiters.uitsocieties.R.layout.activity_app_info);

        final Toast toast = Toast.makeText(getApplicationContext(),"Opening Link",Toast.LENGTH_SHORT);

        TextView text_email = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.textEmail);
        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:techpursuiters@gmail.com"));
                startActivity(intent);
            }
        });

        TextView logo = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.logomaker);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String logoText = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.logo);
                intent.setData(Uri.parse(logoText));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView roundicons = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.roundicons);
        roundicons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.roundicon);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView google = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.googleIcon);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.icons);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView freepik = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.freepik);
        freepik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.freepik);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView vectors = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.vectors);
        vectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.vector);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView budd = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.pixbuddha);
        budd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.buddha);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView flaticons = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.flaticons);
        flaticons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.flaticon);
                intent.setData(Uri.parse(Text));
                if((intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(intent);
                    toast.show();
                }
            }
        });

        TextView cc = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.creativeCommons);
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String Text = getString(tech.pursuiters.techpursuiters.uitsocieties.R.string.cc);
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