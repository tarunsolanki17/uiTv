package tech.pursuiters.techpursuiters.uitsocieties.events_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Tarun on 14-Dec-17.
 */

public class EventsDeatiledPhotoActivity extends AppCompatActivity {

    Intent detail_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(tech.pursuiters.techpursuiters.uitsocieties.R.style.PhotoDetailTheme);
        super.onCreate(savedInstanceState);
        setContentView(tech.pursuiters.techpursuiters.uitsocieties.R.layout.detail_photo_fragment);

        final ImageViewTouch cover_detail = (ImageViewTouch) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.detail_image);
        cover_detail.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        final ProgressBar pbar = (ProgressBar) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.photo_progress);
        final TextView unloaded = (TextView) findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.not_loaded);

        detail_photo = getIntent();

        Glide.with(getApplicationContext()).asBitmap().load(detail_photo.getStringExtra("cover_url")).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                cover_detail.setVisibility(VISIBLE);
                unloaded.setVisibility(GONE);
                pbar.setVisibility(GONE);
                cover_detail.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                cover_detail.setVisibility(GONE);
                unloaded.setVisibility(VISIBLE);
                pbar.setVisibility(GONE);
            }
        });


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_update,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.detail_photo_update){
            Intent photo_detail = new Intent(this, PhotoData.class);
            photo_detail.putExtra(PHOTO_NAME,detail_photo.getStringExtra(PHOTO_NAME));
            photo_detail.putExtra(CREATED_TIME,detail_photo.getStringExtra(CREATED_TIME));
            startActivity(photo_detail);
        }
        return true;
    }*/
}
