package com.example.tarun.uitsocieties.events_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tarun.uitsocieties.R;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.R.id.cover;
import static com.example.tarun.uitsocieties.R.layout.detail_photo;

/**
 * Created by Tarun on 14-Dec-17.
 */

public class EventsDeatiledPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.PhotoDetailTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_photo_fragment);

        final ImageViewTouch cover_detail = (ImageViewTouch) findViewById(R.id.detail_image);
        cover_detail.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        final ProgressBar pbar = (ProgressBar) findViewById(R.id.photo_progress);
        final TextView unloaded = (TextView) findViewById(R.id.not_loaded);

        Intent detail_photo = getIntent();

        Glide.with(getApplicationContext())
                .load(detail_photo.getStringExtra("cover_url"))
                .into(cover_detail);

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
}
