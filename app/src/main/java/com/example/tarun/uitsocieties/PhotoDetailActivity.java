package com.example.tarun.uitsocieties;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tarun.uitsocieties.photos_fragment.PhotoData;
import com.example.tarun.uitsocieties.photos_fragment.Photo_Serial;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.sdk.InMobiSdk;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

import static android.content.Intent.ACTION_VIEW;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.PHOTO_DATA;
import static com.example.tarun.uitsocieties.ClubContract.PHOTO_FILE;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PHOTO_NAME;
import static com.example.tarun.uitsocieties.InClub.photos_data;


/**
 * Created by Tarun on 18-Oct-17.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    ImageView image;
    Toolbar toolbar;
    int pos,curr_pos;
//    ArrayList<PhotoParcel> photos_data_local;
    ArrayList<Photo_Serial> photos_data;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    public transient Context con;
    InMobiBanner banner2;
    /*private RelativeLayout detail_layout;
    private TextView caption_view;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_photo);

        con = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        InMobiSdk.init(PhotoDetailActivity.this,"6c2ca29688614264bd77f77cc38cd923");
        banner2 = (InMobiBanner) findViewById(R.id.banner2);
        banner2.load();

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.image);

//        photos_data = getIntent().getParcelableArrayListExtra("photo_parcel");
        try {
            FileInputStream fis = openFileInput(PHOTO_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            photos_data =(ArrayList<Photo_Serial>) ois.readObject();
            ois.close();
        }
        catch (Exception e){
            Log.v("PhotoDet Excep---",e.toString());
            e.printStackTrace();
        }
        Log.v("Photos Count---",String.valueOf(photos_data.size()));
        pos = getIntent().getIntExtra("position",-1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), photos_data);


        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(pos);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Photo_Serial> all_photos = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Photo_Serial> data) {
            super(fm);
            all_photos = data;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(all_photos.get(position).getBig_image_link(),all_photos.get(position).getCaption());
        }

        @Override
        public int getCount() {
            return all_photos.size();
        }

    }

    public static class PlaceholderFragment extends Fragment{

        String big_image_link;
        String caption;

        public static PlaceholderFragment newInstance(String url, String cap) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("image_url", url);
            args.putString("caption",cap);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            big_image_link = args.getString("image_url");
            caption = args.getString("caption");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.detail_photo_fragment, container, false);

            final ImageViewTouch imageView = rootView.findViewById(R.id.detail_image);
            imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            final ProgressBar photo_prog = rootView.findViewById(R.id.photo_progress);
            final TextView unloaded = rootView.findViewById(R.id.not_loaded);

            Glide.with(getActivity()).asBitmap().load(big_image_link).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    imageView.setVisibility(VISIBLE);
                    unloaded.setVisibility(GONE);
                    photo_prog.setVisibility(GONE);
                    imageView.setImageBitmap(resource);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    imageView.setVisibility(GONE);
                    unloaded.setVisibility(VISIBLE);
                    photo_prog.setVisibility(GONE);

                }
            });

            return rootView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            /*case R.id.save: imageDownload(getApplicationContext(),"dump_image");
                            break;

            case R.id.share://  TODO --> SHARE INTENT
                            break;*/

            case R.id.details_photo:
                Intent detail_in = new Intent(this, PhotoData.class);
                detail_in.putExtra(PHOTO_NAME,photos_data.get(viewPager.getCurrentItem()).getCaption());
                detail_in.putExtra(CREATED_TIME,photos_data.get(viewPager.getCurrentItem()).getCreated_time());
                startActivity(detail_in);
                    break;

            case R.id.facebook_link:
                Intent facebook_intent = new Intent(ACTION_VIEW);
                facebook_intent.setData(Uri.parse(photos_data.get(viewPager.getCurrentItem()).getFacebook_link()));
                if((facebook_intent.resolveActivity(getPackageManager())!=null))
                startActivity(facebook_intent);
                            break;

            case android.R.id.home:
                finish();
                break;

            default :       break;
        }

        return true;
    }
    /*public void saveImage(){
        AsyncTask downloadImage;
        downloadImage = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.v("Save Image---","Running");
                try {
                    URL url = new URL("https://www.google.co.in/images/icons/material/system/1x/email_grey600_24dp.png"*//*photos_data.get(pos).getBig_image_link()*//*);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    urlConnection.setRequestProperty("Accept","");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    Log.v("Response Code IMG---",String.valueOf(urlConnection.getResponseCode()));

                    File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
                    String fileName = photos_data.get(pos).getAlbum_name() + pos;
                    File image_file = new File(SDCardRoot,fileName);
                    boolean created = false;
                    if(!image_file.exists()){
                        created = image_file.createNewFile();
                    }
                    else
                        created = true;
                    if(created) {
                        FileOutputStream fileOutput = new FileOutputStream(image_file);
                        InputStream inputStream = urlConnection.getInputStream();
                        int totalSize = urlConnection.getContentLength();
                        int downloadedSize = 0;
                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutput.write(buffer, 0, bufferLength);
                            downloadedSize += bufferLength;
                            Log.i("Progress:", "downloadedSize:" + downloadedSize + " totalSize:" + totalSize);
                        }
                        fileOutput.close();
                        if (downloadedSize == totalSize) {
                            Log.v("FilePath----", image_file.getPath());
                            Toast.makeText(getApplicationContext(), "Photo Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    Log.v("Save Exception---","Caught:" + e.toString());
                    e.printStackTrace();
                }
                return null;
            }
        };
        downloadImage.execute();
    }


        public void imageDownload(Context con, String url){
            Picasso.with(con)
                    .load("https://openclipart.org/download/216413/coniglio_rabbit_small.svg")
                    .into(getTarget(url));
        }

        //target to save
        private Target getTarget(final String url){
            Target target = new Target(){

                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.flush();
                                ostream.close();
                            } catch (IOException e) {
                                Log.e("IOException", e.getLocalizedMessage());
                            }
                        }
                    }).start();

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.v("Bitmap---!","Failed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.v("Preparing---!","doing");
                }
            };
            return target;
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }*/
}
