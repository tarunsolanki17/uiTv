package com.example.tarun.uitsocieties;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.attr.data;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.ALBUMS;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.ALBUM_NAME;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.FACEBOOK_LINK;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.HEIGHT;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.IMAGES;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.NEXT;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PAGING;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PHOTOS;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PHOTO_COUNT;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PHOTO_NAME;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.PLACE;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.SOURCE;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.THUMB_PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.WIDTH;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.InClub.ft;
import static com.example.tarun.uitsocieties.InClub.login;
import static com.example.tarun.uitsocieties.InClub.login_checker;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFrag extends Fragment{

    ArrayList<PhotoParcel> photos_data = new ArrayList<>();
    TextView no_internet, no_data;
    RecyclerView photo_recyc_view;
    ProgressBar pbar;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    RecycPhotosAdap recycAdapter;
    AsyncTask fetchAsync;
    int data_len;

    public PhotosFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO --> LOOK FOR INTERNET CONNECTION
        // TODO --> LOOK FOR DATA AVAILABLE

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        no_internet = view.findViewById(R.id.no_internet_photos);
        no_data = view.findViewById(R.id.no_photos);
        photo_recyc_view = view.findViewById(R.id.photo_recyc_view);
        pbar = view.findViewById(R.id.progress_bar_photos);
        swipe = view.findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                if(isConnectedFunc()) {
                    if(fetchAsync!=null) {
                        fetchAsync.cancel(true);
                    }
                        photos_data.clear();
                        pbar.setVisibility(VISIBLE);
                        photo_recyc_view.setVisibility(GONE);

                        if(recycAdapter!=null)
                        recycAdapter.notifyDataSetChanged();
                        photoJSONRequest();
                }
                swipe.setRefreshing(false);
            }
        });

        if(isConnectedFunc()){

            if(savedInstanceState!=null&&savedInstanceState.getInt("Login")==1)
                savedInstanceState = null;

            if (savedInstanceState != null) {
                photos_data = savedInstanceState.getParcelableArrayList("photos_parcel");
                if(photos_data.isEmpty()||photos_data==null){
                    //  TODO --> REMOVE NULL POINTER EXCEPTION IN isEmpty()
                    pbar.setVisibility(GONE);
                    photo_recyc_view.setVisibility(GONE);
                    no_data.setVisibility(VISIBLE);
                }
                else{
                    data_len = photos_data.size();
                    pbar.setVisibility(GONE);
                    no_data.setVisibility(GONE);
                    photo_recyc_view.setVisibility(VISIBLE);
                    onDataFetched();
                }
            }
            else {
                login_checker();
                if(login) {
                    photoJSONRequest();
                }
            }
        }

        return view;
    }

    /******************************  END OF onCreateView  *******************************/


    public void photoJSONRequest(){
        data_len = 0;

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "albums{name,photo_count,photos.limit(25){name,created_time,link,place,height,width,picture,images}}");

        final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse temp_response) {
                try {
                        Log.v("onComplete---","Running");
                        Log.v("Photo Response---",temp_response.toString());
                        fetchPhotosData(temp_response.getJSONObject());

                } catch (Exception e) {
                    Log.v("JSON Exception---:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        fetchAsync = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/"+ club_id,parameters, HttpMethod.GET, graphCallback).executeAndWait();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                onDataFetched();
            }
        };
        fetchAsync.execute();

    }

    public void fetchPhotosData(JSONObject response){
//        ArrayList<PhotoParcel> photos = new ArrayList<>();
        Log.v("Fetching Data---","Running");
        if(response!=null&&response.length()>0) {
            try {
                JSONObject albums = response.getJSONObject(ALBUMS);
                Log.v("albums---:",albums.toString());

                JSONArray data = albums.getJSONArray("data");
                data_len = data.length();
                Log.v("data---",data.toString());
                Log.v("Album Count in Res---",String.valueOf(data_len));

                for (int i = 0; i < data.length(); i++) {

                    String album_name = "";
                    int photo_count = -1;
                    String caption = "";
                    String created_time = "";
                    String facebook_link = "";
                    String place = "";
                    String height = "";
                    String width = "";
                    String thumb_link = "";
                    String image_link = "";
                    String big_image_link = "";
                    boolean next = false;

                    JSONObject curr_album = data.getJSONObject(i);

                    if(curr_album.has(ALBUM_NAME)&&!curr_album.isNull(ALBUM_NAME)){
                        album_name = curr_album.getString(ALBUM_NAME);
                    }
                    Log.v("ALBUM NAME---",album_name);
                    if(curr_album.has(PHOTO_COUNT)&&!curr_album.isNull(PHOTO_COUNT)){
                        photo_count = curr_album.getInt(PHOTO_COUNT);
                    }
                    if(curr_album.has(PHOTOS)&&!curr_album.isNull(PHOTOS)){
                        JSONObject photo_object = curr_album.getJSONObject(PHOTOS);
                        JSONArray photo_array = photo_object.getJSONArray("data");

                        Log.v("PhotoArray LEN---",String.valueOf(photo_array.length()));

                        for(int j=0;j<photo_array.length();j++){

                            JSONObject curr_image = photo_array.getJSONObject(j);

                            if(curr_image.has(PHOTO_NAME)&&!curr_image.isNull(PHOTO_NAME)) {
                                caption = curr_image.getString(PHOTO_NAME);
                            }
                            if(curr_image.has(CREATED_TIME)&&!curr_image.isNull(CREATED_TIME)) {
                                created_time = curr_image.getString(CREATED_TIME);
                            }
                            if(curr_image.has(FACEBOOK_LINK)&&!curr_image.isNull(FACEBOOK_LINK)) {
                                facebook_link = curr_image.getString(FACEBOOK_LINK);
                            }
                            if(curr_image.has(PLACE)&&!curr_image.isNull(PLACE)) {
                                place = curr_image.getString(PLACE);
                            }
                            if(curr_image.has(HEIGHT)&&!curr_image.isNull(HEIGHT)) {
                                height = curr_image.getString(HEIGHT);
                            }
                            if(curr_image.has(WIDTH)&&!curr_image.isNull(WIDTH)) {
                                width = curr_image.getString(WIDTH);
                            }
                            if(curr_image.has(THUMB_PICTURE)&&!curr_image.isNull(THUMB_PICTURE)) {
                                thumb_link = curr_image.getString(THUMB_PICTURE);

                            }

                            if(curr_image.has(IMAGES)&&!curr_image.isNull(IMAGES)){
                                JSONArray curr_sources_array = curr_image.getJSONArray(IMAGES);
                                JSONObject curr_source = curr_sources_array.getJSONObject(curr_sources_array.length()-1);

                                if(curr_source.has(SOURCE)&&!curr_source.isNull(SOURCE)){
                                    image_link = curr_source.getString(SOURCE);
                                }

                                curr_source = curr_sources_array.getJSONObject(0);

                                if(curr_source.has(SOURCE)&&!curr_source.isNull(SOURCE)){
                                    big_image_link = curr_source.getString(SOURCE);
                                }
                            }
                            photos_data.add(new PhotoParcel(album_name,photo_count,caption,created_time,facebook_link,place,height,width,thumb_link,image_link,big_image_link));
                        }
                        if(photo_object.has(PAGING)&&!photo_object.isNull(PAGING)){
                            JSONObject pagingObject = photo_object.getJSONObject(PAGING);

                            if(pagingObject.has(NEXT)&&!pagingObject.isNull(NEXT)){
                                photoNextJSONReq(pagingObject.getString(NEXT),album_name,photo_count,caption,created_time,facebook_link,place,height,width,thumb_link);
                            }
                        }
                    }


                }
                Log.v("photos_data len---",String.valueOf(photos_data.size()));

            } catch (Exception e) {
                Log.v("Exception---",e.toString());
                e.printStackTrace();
            }
        }
    }

    public void photoNextJSONReq(String nextResponseLink,String album_name, int photo_count, String caption, String created_time, String facebook_link, String place, String height, String width, String thumb_link){
        URL nextURL;
        HttpURLConnection httpUConn = null;
        InputStream inputStream = null;
        try {
            nextURL = new URL(nextResponseLink);
            httpUConn = (HttpURLConnection) nextURL.openConnection();
            httpUConn.setRequestMethod("GET");
            httpUConn.setReadTimeout(10000);
            httpUConn.setConnectTimeout(15000);
            httpUConn.connect();
            inputStream = httpUConn.getInputStream();
            InputStreamReader inStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line=bufferedReader.readLine())!=null){
                builder.append(line);
            }
            Log.v("NEXT STRING---",builder.toString());
            fetchNextPhotosData(builder.toString(),album_name,photo_count,caption,created_time,facebook_link,place,height,width,thumb_link);
        } catch (Exception e) {
            Log.v("Network Exception---","Caught");
            e.printStackTrace();
        }
        finally {
            if(httpUConn!=null){
                httpUConn.disconnect();
            }
            if(inputStream!=null){
                try{
                    inputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void fetchNextPhotosData(String nextPhotoString, String album_name, int photo_count, String caption, String created_time, String facebook_link, String place, String height, String width, String thumb_link){
        String image_link = "";
        String big_image_link = "";
        try {
            JSONObject nextObj = new JSONObject(nextPhotoString);
            JSONArray new_array = nextObj.getJSONArray("data");

            for(int k=0;k<new_array.length();k++) {
                JSONObject curr_image_next = new_array.getJSONObject(k);

                if (curr_image_next.has(IMAGES) && !curr_image_next.isNull(IMAGES)) {
                    JSONArray curr_sources_array = curr_image_next.getJSONArray(IMAGES);
                    JSONObject curr_source = curr_sources_array.getJSONObject(curr_sources_array.length() - 1);

                    if (curr_source.has(SOURCE) && !curr_source.isNull(SOURCE)) {
                        image_link = curr_source.getString(SOURCE);
                    }

                    curr_source = curr_sources_array.getJSONObject(0);

                    if (curr_source.has(SOURCE) && !curr_source.isNull(SOURCE)) {
                        big_image_link = curr_source.getString(SOURCE);
                    }
                }
                photos_data.add(new PhotoParcel(album_name,photo_count,caption,created_time,facebook_link,place,height,width,thumb_link,image_link,big_image_link));
            }
            if(nextObj.has(PAGING)&&!nextObj.isNull(PAGING)){
                JSONObject pagingObject = nextObj.getJSONObject(PAGING);

                if(pagingObject.has(NEXT)&&!pagingObject.isNull(NEXT)){
                    photoNextJSONReq(pagingObject.getString(NEXT),album_name,photo_count,caption,created_time,facebook_link,place,height,width,thumb_link);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("photos_parcel",photos_data);
    }

    public void onDataFetched(){
        if(data_len==0){
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
            no_data.setVisibility(VISIBLE);
        }
        else{
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            photo_recyc_view.setVisibility(VISIBLE);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
            photo_recyc_view.setHasFixedSize(true);
            photo_recyc_view.setLayoutManager(layoutManager);
            //  TODO --> SET A LISTENER WHICH RUNS WHEN THE LIST COMES TO END.

            recycAdapter = new RecycPhotosAdap(photos_data,getContext());
            photo_recyc_view.setAdapter(recycAdapter);
        }
    }

    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnectedOrConnecting();

        if(!isConnected){
//            TODO --> IMAGE WITH TEXT IN NO INTERNET
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
            no_internet.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            return true;
        }
    }
}
