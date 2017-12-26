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
import tech.pursuiters.techpursuiters.uitsocieties.R;

import static android.content.Intent.ACTION_VIEW;

public class AboutTheDevelopers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(tech.pursuiters.techpursuiters.uitsocieties.R.layout.activity_about_the_developers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView shubhi_email = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.shubhi_email);
        shubhi_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:shubhishukla31@gmail.com"));
                startActivity(intent);
            }
        });
        TextView tarun_email = (TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.tarun_email);
        tarun_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:tarunsolanki97@gmail.com"));
                startActivity(intent);
            }
        });

        final Toast toast = Toast.makeText(getApplicationContext(),"Opening Link",Toast.LENGTH_SHORT);

        TextView tarun_fb =(TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.tarun_fb);
        tarun_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebook_intent = new Intent(ACTION_VIEW);
                facebook_intent.setData(Uri.parse("https://www.facebook.com/tarunsolanki97"));
                if((facebook_intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(facebook_intent);
                    toast.show();
                }
            }
        });

        TextView shubhi_fb=(TextView)findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.shubhi_fb);
        shubhi_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebook_intent = new Intent(ACTION_VIEW);
                facebook_intent.setData(Uri.parse("https://www.facebook.com/shubhi.shukla.562"));
                if((facebook_intent.resolveActivity(getPackageManager())!=null)) {
                    startActivity(facebook_intent);
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
