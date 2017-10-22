package com.example.tarun.uitsocieties;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.R.drawable.a;

/**
 * Created by Tarun on 18-Oct-17.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    ImageView image;
    Toolbar toolbar;
    int pos;
    ArrayList<PhotoParcel> photos_data;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    /*private RelativeLayout detail_layout;
    private TextView caption_view;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.PhotoDetailTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_photo);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        image = (ImageView) findViewById(R.id.image);
//        caption_view = (TextView) findViewById(R.id.caption);

        photos_data = getIntent().getParcelableArrayListExtra("photo_parcel");
        Log.v("Photos Count---",String.valueOf(photos_data.size()));
        pos = getIntent().getIntExtra("position",-1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), photos_data);

        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(pos);

    }

    public  class SectionsPagerAdapter extends FragmentPagerAdapter{

        ArrayList<PhotoParcel> all_photos = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<PhotoParcel> data) {
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

            final ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
            final ProgressBar photo_prog = (ProgressBar) rootView.findViewById(R.id.photo_progress);
            final TextView unloaded = (TextView) rootView.findViewById(R.id.not_loaded);
            RelativeLayout detail_layout = (RelativeLayout) rootView.findViewById(R.id.detailed_layout);
            final TextView caption_view = (TextView) rootView.findViewById(R.id.caption);

            detail_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(caption_view.getVisibility()==VISIBLE){
                        caption_view.setVisibility(GONE);
                    }
                    else{
                        caption_view.setVisibility(VISIBLE);
                    }
                }
            });

            caption_view.setText(caption);

            Glide.with(getActivity()).load(big_image_link).thumbnail(0.1f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageView.setVisibility(GONE);
                            unloaded.setVisibility(VISIBLE);
                            photo_prog.setVisibility(GONE);
                            caption_view.setVisibility(GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageView.setVisibility(VISIBLE);
                            caption_view.setVisibility(VISIBLE);
                            unloaded.setVisibility(GONE);
                            photo_prog.setVisibility(GONE);

                            return false;
                        }
                    })
                    .into(imageView);

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
            case R.id.save: //  TODO --> SAVE PHOTO TO DEVICE
                            //  TODO --> TOAST WHEN SAVED
                            break;
            case R.id.share://  TODO --> SHARE INTENT
                            break;
            case R.id.facebook_link:    // TODO --> IMPLICIT INTENT TO FACEBOOK
                            break;

            default :       break;
        }

        return true;
    }
}
