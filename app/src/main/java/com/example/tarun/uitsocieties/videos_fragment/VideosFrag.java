package com.example.tarun.uitsocieties.videos_fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tarun.uitsocieties.R;
import com.example.tarun.uitsocieties.RecycPhotosAdap;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.tarun.uitsocieties.ClubContract.EventsConstants.ID;
import static com.example.tarun.uitsocieties.ClubContract.PhotosConstants.ALBUM_NAME;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.CREATED_TIME;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.DATA;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.DESCRIPTION;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.IS_PREFERRED;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.LENGTH;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.PICTURE;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.SOURCE;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.THUMBNAILS;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.THUMBNAIL_URL;
import static com.example.tarun.uitsocieties.ClubContract.VideosConstants.VIDEOS;
import static com.example.tarun.uitsocieties.DetailActivity.photos_data;
import static com.example.tarun.uitsocieties.InClub.club_id;
import static com.example.tarun.uitsocieties.InClub.login_checker;
import static com.example.tarun.uitsocieties.InClub.login;
import static com.example.tarun.uitsocieties.R.id.photo_recyc_view;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFrag extends Fragment {

    /******************************* GLOBAL VARIABLES  *******************************/
    ArrayList<VideoParcel> videos_data = new ArrayList<>();
    RecyclerView video_recyclerView;
    ProgressBar pbar;
    TextView no_internet;
    TextView no_data;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    Context con;
    int data_len;
    RecycVideosAdap recycAdapter;

    public VideosFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View video_view = inflater.inflate(R.layout.fragment_videos, container, false);

        con = video_view.getContext();
        video_recyclerView = video_view.findViewById(R.id.video_recyc_view);
        pbar = video_view.findViewById(R.id.progress_bar_v);
        no_internet = video_view.findViewById(R.id.no_internet_v);
        no_data = video_view.findViewById(R.id.no_data_v);
        swipe = video_view.findViewById(R.id.swipe_v);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                if(isConnectedFunc())
                    videoJSONReq();
                swipe.setRefreshing(false);
            }
        });

        if(isConnectedFunc()){

            if(savedInstanceState!=null&&savedInstanceState.getInt("Login")==1)
                savedInstanceState = null;

            if (savedInstanceState != null) {
                videos_data = savedInstanceState.getParcelableArrayList("videos_parcel");
                if(/*videos_data.isEmpty()||*/videos_data==null){
                    //  TODO --> REMOVE NULL POINTER EXCEPTION IN isEmpty()
                    pbar.setVisibility(GONE);
                    video_recyclerView.setVisibility(GONE);
                    no_data.setVisibility(VISIBLE);
                }
                else{
                    data_len = videos_data.size();
                    pbar.setVisibility(GONE);
                    no_data.setVisibility(GONE);
                    video_recyclerView.setVisibility(VISIBLE);
                    onDataFetched();
                }
            }
            else {
                login_checker();
                if(login) {
                    videoJSONReq();
                }
            }
        }

        return video_view;
    }

    public void videoJSONReq(){
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+club_id,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        data_len = 0;

                        Log.v("Video Res---",response.toString());
                        videos_data = fetchVideosData(response);
                        onDataFetched();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "videos{id,created_time,title,description,length,place,picture,captions,source,thumbnails{is_preferred,uri}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public ArrayList<VideoParcel> fetchVideosData(GraphResponse response){
        ArrayList<VideoParcel> video_fetch_data = new ArrayList<>();
        JSONObject JSONresponse = response.getJSONObject();
        if(JSONresponse!=null&&JSONresponse.length()>0) {
            try {
                JSONObject videos = JSONresponse.getJSONObject(VIDEOS);
                JSONArray data = videos.getJSONArray(DATA);
                data_len = data.length();

                Log.v("No of videos---",String.valueOf(data.length()));
                Log.v("videos data---",data.toString());

                for (int i = 0; i < data.length(); i++) {

                    String id = "";
                    String created_time = "";
                    String description = "";
                    int length = -1;
                    String picture = "";
                    String source_url = "";
                    boolean is_preferred = false;
                    String thumb_url = "";

                    JSONObject curr_video = data.getJSONObject(i);

                    if(curr_video.has(ID)&&!curr_video.isNull(ID)){
                        id = curr_video.getString(ID);
                    }
                    if(curr_video.has(CREATED_TIME)&&!curr_video.isNull(CREATED_TIME)){
                        created_time = curr_video.getString(CREATED_TIME);
                    }
                    if(curr_video.has(DESCRIPTION)&&!curr_video.isNull(DESCRIPTION)){
                        description = curr_video.getString(DESCRIPTION);
                    }
                    if(curr_video.has(LENGTH)&&!curr_video.isNull(LENGTH)){
                        length = curr_video.getInt(LENGTH);
                    }
                    if(curr_video.has(LENGTH)&&!curr_video.isNull(LENGTH)){
                        length = curr_video.getInt(LENGTH);
                    }
                    if(curr_video.has(PICTURE)&&!curr_video.isNull(PICTURE)){
                        picture = curr_video.getString(PICTURE);
                    }
                    if(curr_video.has(SOURCE)&&!curr_video.isNull(SOURCE)){
                        source_url = curr_video.getString(SOURCE);
                    }
                    if(curr_video.has(THUMBNAILS)&&!curr_video.isNull(THUMBNAILS)){
                        JSONObject thumbnail_obj = curr_video.getJSONObject(THUMBNAILS);

                        JSONArray thumb_data = thumbnail_obj.getJSONArray(DATA);

                        for(int k=0;k<thumb_data.length();k++){
                            JSONObject curr_thumb = thumb_data.getJSONObject(k);

                            if(curr_thumb.has(IS_PREFERRED)&&!curr_thumb.isNull(IS_PREFERRED)){
                                is_preferred = curr_thumb.getBoolean(IS_PREFERRED);

                                if(is_preferred){
                                    if(curr_thumb.has(THUMBNAIL_URL)&&!curr_thumb.isNull(THUMBNAIL_URL)){
                                        thumb_url = curr_thumb.getString(THUMBNAIL_URL);
                                    }
                                }
                            }
                        }
                    }
                    video_fetch_data.add(new VideoParcel(id,created_time,description,length,picture,source_url,thumb_url));
                }

            }
            catch (Exception e){
                Log.v("Exception---",e.toString());
            }
        }
        return video_fetch_data;
    }

    public void onDataFetched(){
        if(data_len==0){
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            video_recyclerView.setVisibility(GONE);
            no_data.setVisibility(VISIBLE);
        }
        else{
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            video_recyclerView.setVisibility(VISIBLE);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(con,2);
            video_recyclerView.setHasFixedSize(true);
            video_recyclerView.setLayoutManager(layoutManager);

            recycAdapter = new RecycVideosAdap(videos_data,con);
            video_recyclerView.setAdapter(recycAdapter);
        }
    }

    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnected();

        if(!isConnected){
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            video_recyclerView.setVisibility(GONE);
            no_internet.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("videos_parcel",videos_data);
    }
}
