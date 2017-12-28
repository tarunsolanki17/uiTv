package tech.pursuiters.techpursuiters.uitsocieties;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import tech.pursuiters.techpursuiters.uitsocieties.photos_fragment.Photo_Serial;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.albumNo;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.album_len;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.album_len_max_index;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.club_id;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.data_len;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.fetchAsyncP;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.login;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.login_checker;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.photos_data;
import static tech.pursuiters.techpursuiters.uitsocieties.InClub.recycAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFrag extends Fragment{

    RecyclerView photo_recyc_view;
    ProgressBar pbar;
    SwipeRefreshLayout swipe;
    boolean isConnected;
    RecyclerView.LayoutManager layoutManager;
    int k;
    Toast t;
    LinearLayout no_internet,no_data;
    ImageView swipe_text;

    public PhotosFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        no_internet = view.findViewById(R.id.no_internet_photos);
        no_data = view.findViewById(R.id.no_photos);
        photo_recyc_view = view.findViewById(R.id.photo_recyc_view);
        pbar = view.findViewById(R.id.progress_bar_photos);
        swipe = view.findViewById(R.id.swipe);
        swipe_text = view.findViewById(R.id.swipe_text_p);

        layoutManager = new GridLayoutManager(getContext(),3);
        photo_recyc_view.setHasFixedSize(true);
        photo_recyc_view.setLayoutManager(layoutManager);

        photo_recyc_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.v("AlbumNo in Scroll---=",String.valueOf(albumNo) + "  album_len---=" + String.valueOf(album_len));

                if(photo_recyc_view.canScrollVertically(-1))
                    if(t!=null){
                        t.cancel();
                    }
                if(recycAdapter!=null)
                if(!photo_recyc_view.canScrollVertically(1)) {
                    if (albumNo >= album_len_max_index) {
                    }
                    else {
                        if(t!=null) {
                            t.cancel();
                        }
                        t = Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT);
                        t.show();
                        photoJSONRequest();
                    }
                }

            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                albumNo = 0;
                recycAdapter = null;
                if(isConnectedFunc()) {
                    if(fetchAsyncP!=null) {
                        fetchAsyncP.cancel(true);
                    }
                    if(photos_data!=null)
                        photos_data.clear();
                    else
                        photos_data = new ArrayList<>();

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
                if(savedInstanceState.getBoolean("Incomplete")) {
                    Log.v("fetchAync--->","was incomplete");
                    if (fetchAsyncP != null) {
                        Log.v("fetchAync--->","not null");
                        if (fetchAsyncP.getStatus().toString().equals("RUNNING")) {    /** fetchAsyncP still running*/
                            Log.v("fetchAync--->","still running");
                            if(photos_data.isEmpty()) {
                                pbar.setVisibility(VISIBLE);
                                photo_recyc_view.setVisibility(GONE);
                            }
                            else{
                                showing();
                            }
                        }
                        if (fetchAsyncP.getStatus().toString().equals("FINISHED")) {   /** fetchAsyncP complete*/
                            Log.v("fetchAync--->","finished");
                            showing();
                        }
                    } else {
                        Log.v("fetchAync--->","is null");
                        Log.v("videosdata--->",String.valueOf(photos_data.size()));
                        showing();
                    }
                }
                else {
                    Log.v("fetchAync--->","had already finished");
                    Bundle received_bundle = new Bundle();
                    received_bundle = savedInstanceState.getBundle(ClubContract.PHOTO_BUNDLE);
                    photos_data = (ArrayList<Photo_Serial>) received_bundle.getSerializable("photos");
                    showing();
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

    public void showing(){
        if (photos_data != null)
            if (photos_data.isEmpty()) {
                pbar.setVisibility(GONE);
                photo_recyc_view.setVisibility(GONE);
                no_data.setVisibility(VISIBLE);
                swipe_text.setVisibility(VISIBLE);
            } else {
                data_len = photos_data.size();
                pbar.setVisibility(GONE);
                no_data.setVisibility(GONE);
                photo_recyc_view.setVisibility(VISIBLE);
                swipe_text.setVisibility(GONE);
                onDataFetched();
            }
    }

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

                    GraphRequest newRequest = temp_response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                    if(newRequest!=null) {
                        newRequest.setGraphPath("/" + club_id + "/");
                        newRequest.setCallback(this);
                        newRequest.setParameters(parameters);
                        newRequest.executeAndWait();
                    }
                } catch (Exception e) {
                    Log.v("JSON Exception---:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        fetchAsyncP = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/"+ club_id,parameters, HttpMethod.GET, graphCallback).executeAndWait();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.v("AlbumNo---=",String.valueOf(albumNo) + "  k---=" + String.valueOf(k));
                if(recycAdapter==null)
                onDataFetched();
                else
                    setNextData();
                if(albumNo<album_len_max_index)
                albumNo++;
            }
        };
        fetchAsyncP.execute();

    }

    synchronized public void fetchPhotosData(JSONObject response){

        Log.v("Fetching Data---","Running");
        if(response!=null&&response.length()>0) {
            try {
                JSONObject albums = response.getJSONObject(ClubContract.PhotosConstants.ALBUMS);
                Log.v("albums---:",albums.toString());

                if(recycAdapter==null){
                    album_len = 0;
                    album_len_max_index = 0;
                    data_len = 0;
                }

                JSONArray data = albums.getJSONArray("data");
                album_len = data.length();
                album_len_max_index = album_len - 1;
                Log.v("data---",data.toString());
                Log.v("Album Count in Res---",String.valueOf(album_len));
                data_len = album_len;

                int total_photos = 0;
                int i;

                if(recycAdapter==null) {
                    albumNo = 0;
                }

                Log.v("AlbumNo---=",String.valueOf(albumNo));
                if(albumNo==0){
                    if(photos_data!=null)
                        photos_data.clear();
                    for (k = 0; k < data.length(); k++){
                        JSONObject curr_album = data.getJSONObject(k);
                        if(curr_album.has(ClubContract.PhotosConstants.PHOTO_COUNT)&&!curr_album.isNull(ClubContract.PhotosConstants.PHOTO_COUNT))
                            total_photos = total_photos + curr_album.getInt(ClubContract.PhotosConstants.PHOTO_COUNT);

                        if(total_photos>=51)
                            break;
                    }
                    albumNo = k;
                    i=0;
                }
                else
                    i=albumNo;

                Log.v("i---=",String.valueOf(i));

                String album_name;
                int photo_count;
                String caption;
                String created_time;
                String facebook_link;
                String place;
                String height;
                String width;
                String thumb_link;
                String image_link;
                String big_image_link;

                for(i=i;i<=albumNo;i++){

                    album_name = "";
                    photo_count = -1;
                    caption = "";
                    created_time = "";
                    facebook_link = "";
                    place = "";
                    height = "";
                    width = "";
                    thumb_link = "";
                    image_link = "";
                    big_image_link = "";

                    JSONObject curr_album = data.getJSONObject(i);

                    if(curr_album.has(ClubContract.PhotosConstants.ALBUM_NAME)&&!curr_album.isNull(ClubContract.PhotosConstants.ALBUM_NAME)){
                        album_name = curr_album.getString(ClubContract.PhotosConstants.ALBUM_NAME);
                    }
                    Log.v("ALBUM NAME---",album_name);
                    if(curr_album.has(ClubContract.PhotosConstants.PHOTO_COUNT)&&!curr_album.isNull(ClubContract.PhotosConstants.PHOTO_COUNT)){
                        photo_count = curr_album.getInt(ClubContract.PhotosConstants.PHOTO_COUNT);
                    }
                    if(curr_album.has(ClubContract.PhotosConstants.PHOTOS)&&!curr_album.isNull(ClubContract.PhotosConstants.PHOTOS)){
                        JSONObject photo_object = curr_album.getJSONObject(ClubContract.PhotosConstants.PHOTOS);
                        JSONArray photo_array = photo_object.getJSONArray("data");

                        Log.v("PhotoArray LEN---",String.valueOf(photo_array.length()));

                        for(int j=0;j<photo_array.length();j++){

                            JSONObject curr_image = photo_array.getJSONObject(j);

                            if(curr_image.has(ClubContract.PhotosConstants.PHOTO_NAME)&&!curr_image.isNull(ClubContract.PhotosConstants.PHOTO_NAME)) {
                                caption = curr_image.getString(ClubContract.PhotosConstants.PHOTO_NAME);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.CREATED_TIME)&&!curr_image.isNull(ClubContract.PhotosConstants.CREATED_TIME)) {
                                created_time = curr_image.getString(ClubContract.PhotosConstants.CREATED_TIME);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.FACEBOOK_LINK)&&!curr_image.isNull(ClubContract.PhotosConstants.FACEBOOK_LINK)) {
                                facebook_link = curr_image.getString(ClubContract.PhotosConstants.FACEBOOK_LINK);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.PLACE)&&!curr_image.isNull(ClubContract.PhotosConstants.PLACE)) {
                                place = curr_image.getString(ClubContract.PhotosConstants.PLACE);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.HEIGHT)&&!curr_image.isNull(ClubContract.PhotosConstants.HEIGHT)) {
                                height = curr_image.getString(ClubContract.PhotosConstants.HEIGHT);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.WIDTH)&&!curr_image.isNull(ClubContract.PhotosConstants.WIDTH)) {
                                width = curr_image.getString(ClubContract.PhotosConstants.WIDTH);
                            }
                            if(curr_image.has(ClubContract.PhotosConstants.THUMB_PICTURE)&&!curr_image.isNull(ClubContract.PhotosConstants.THUMB_PICTURE)) {
                                thumb_link = curr_image.getString(ClubContract.PhotosConstants.THUMB_PICTURE);
                            }

                            if(curr_image.has(ClubContract.PhotosConstants.IMAGES)&&!curr_image.isNull(ClubContract.PhotosConstants.IMAGES)){
                                JSONArray curr_sources_array = curr_image.getJSONArray(ClubContract.PhotosConstants.IMAGES);
                                JSONObject curr_source = curr_sources_array.getJSONObject(curr_sources_array.length()-1);

                                if(curr_source.has(ClubContract.PhotosConstants.SOURCE)&&!curr_source.isNull(ClubContract.PhotosConstants.SOURCE)){
                                    image_link = curr_source.getString(ClubContract.PhotosConstants.SOURCE);
                                }

                                curr_source = curr_sources_array.getJSONObject(0);

                                if(curr_source.has(ClubContract.PhotosConstants.SOURCE)&&!curr_source.isNull(ClubContract.PhotosConstants.SOURCE)){
                                    big_image_link = curr_source.getString(ClubContract.PhotosConstants.SOURCE);
                                }
                            }
                            if(!fetchAsyncP.isCancelled()) {
                                photos_data.add(new Photo_Serial(album_name, photo_count, caption, created_time, facebook_link, place, height, width, thumb_link, image_link, big_image_link));
                            }
                            else {
                                Log.v("fetchAsyncP---", "Cancelled");
                                /*if(photos_data!=null)
                                photos_data.clear();*/    /**     CLEARED SO THAT ANOTHER CLUB DOESN'T SHOW PHOTOS OF THIS CLUB   */
                            }
                            /*if(photos_data.size()>=FIRST_SCAN) {
//                                fetchAsyncP.cancel(true);
                                break;
                            }*/
//                           TODO --> VERIFY CLEAR STATEMENT

                        }

                        if(photo_object.has(ClubContract.PhotosConstants.PAGING)&&!photo_object.isNull(ClubContract.PhotosConstants.PAGING)){
                            JSONObject pagingObject = photo_object.getJSONObject(ClubContract.PhotosConstants.PAGING);

                            if(pagingObject.has(ClubContract.PhotosConstants.NEXT)&&!pagingObject.isNull(ClubContract.PhotosConstants.NEXT)){
                                photoNextJSONReq(pagingObject.getString(ClubContract.PhotosConstants.NEXT),album_name,photo_count);
                            }
                        }
                    }


                }
                Log.v("photos_data len---",String.valueOf(photos_data.size()));

            } catch (Exception e) {
                Log.v("Excep fetchPhotosD---",e.toString());
                e.printStackTrace();
            }
        }
    }

    public void photoNextJSONReq(String nextResponseLink,String album_name, int photo_count){
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
            fetchNextPhotosData(builder.toString(),album_name,photo_count);
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

    public void fetchNextPhotosData(String nextPhotoString, String album_name, int photo_count){
        String caption;
        String created_time;
        String facebook_link;
        String place;
        String height;
        String width;
        String thumb_link;
        String image_link;
        String big_image_link;
        try {
            JSONObject nextObj = new JSONObject(nextPhotoString);
            JSONArray new_array = nextObj.getJSONArray("data");

            for(int k=0;k<new_array.length();k++) {

                caption = "";
                created_time = "";
                facebook_link = "";
                place = "";
                height = "";
                width = "";
                thumb_link = "";
                image_link = "";
                big_image_link = "";

                JSONObject curr_image_next = new_array.getJSONObject(k);

                if(curr_image_next.has(ClubContract.PhotosConstants.PHOTO_NAME)&&!curr_image_next.isNull(ClubContract.PhotosConstants.PHOTO_NAME)) {
                    caption = curr_image_next.getString(ClubContract.PhotosConstants.PHOTO_NAME);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.CREATED_TIME)&&!curr_image_next.isNull(ClubContract.PhotosConstants.CREATED_TIME)) {
                    created_time = curr_image_next.getString(ClubContract.PhotosConstants.CREATED_TIME);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.FACEBOOK_LINK)&&!curr_image_next.isNull(ClubContract.PhotosConstants.FACEBOOK_LINK)) {
                    facebook_link = curr_image_next.getString(ClubContract.PhotosConstants.FACEBOOK_LINK);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.PLACE)&&!curr_image_next.isNull(ClubContract.PhotosConstants.PLACE)) {
                    place = curr_image_next.getString(ClubContract.PhotosConstants.PLACE);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.HEIGHT)&&!curr_image_next.isNull(ClubContract.PhotosConstants.HEIGHT)) {
                    height = curr_image_next.getString(ClubContract.PhotosConstants.HEIGHT);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.WIDTH)&&!curr_image_next.isNull(ClubContract.PhotosConstants.WIDTH)) {
                    width = curr_image_next.getString(ClubContract.PhotosConstants.WIDTH);
                }
                if(curr_image_next.has(ClubContract.PhotosConstants.THUMB_PICTURE)&&!curr_image_next.isNull(ClubContract.PhotosConstants.THUMB_PICTURE)) {
                    thumb_link = curr_image_next.getString(ClubContract.PhotosConstants.THUMB_PICTURE);
                }

                if (curr_image_next.has(ClubContract.PhotosConstants.IMAGES) && !curr_image_next.isNull(ClubContract.PhotosConstants.IMAGES)) {
                    JSONArray curr_sources_array = curr_image_next.getJSONArray(ClubContract.PhotosConstants.IMAGES);
                    JSONObject curr_source = curr_sources_array.getJSONObject(curr_sources_array.length() - 1);

                    if (curr_source.has(ClubContract.PhotosConstants.SOURCE) && !curr_source.isNull(ClubContract.PhotosConstants.SOURCE)) {
                        image_link = curr_source.getString(ClubContract.PhotosConstants.SOURCE);
                    }

                    curr_source = curr_sources_array.getJSONObject(0);

                    if (curr_source.has(ClubContract.PhotosConstants.SOURCE) && !curr_source.isNull(ClubContract.PhotosConstants.SOURCE)) {
                        big_image_link = curr_source.getString(ClubContract.PhotosConstants.SOURCE);
                    }
                }
                if(!fetchAsyncP.isCancelled()) {
                    photos_data.add(new Photo_Serial(album_name, photo_count, caption, created_time, facebook_link, place, height, width, thumb_link, image_link, big_image_link));
                }
                else {
                    Log.v("fetchAsyncP---", "Cancelled");
                    /*if(photos_data!=null)
                    photos_data.clear();*/    /**     CLEARED SO THAT ANOTHER CLUB DOESN'T SHOW PHOTOS OF THIS CLUB   */
                }
//                TODO --> VERIFY CLEAR
                /*if(photos_data.size()>=FIRST_SCAN) {
//                    fetchAsyncP.cancel(true);
                    break;
                }*/
                Log.v("photos add---+",String.valueOf(photos_data.size()));

            }
            if(nextObj.has(ClubContract.PhotosConstants.PAGING)&&!nextObj.isNull(ClubContract.PhotosConstants.PAGING)){
                JSONObject pagingObject = nextObj.getJSONObject(ClubContract.PhotosConstants.PAGING);

                if(pagingObject.has(ClubContract.PhotosConstants.NEXT)&&!pagingObject.isNull(ClubContract.PhotosConstants.NEXT)){
                    photoNextJSONReq(pagingObject.getString(ClubContract.PhotosConstants.NEXT),album_name,photo_count);
                }
            }
        } catch (Exception e) {
            Log.v("Exception in fetchNext","----");
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(fetchAsyncP!=null) {
            if (fetchAsyncP.getStatus().toString().equals("RUNNING")) {
                outState.putBoolean("Incomplete", true);
                if(!photos_data.isEmpty()) {
                    Bundle photoBundle = new Bundle();
                    photoBundle.putSerializable("photos", photos_data);
                    outState.putBundle(ClubContract.PHOTO_BUNDLE, photoBundle);
                }
            }
            else if(fetchAsyncP.getStatus().toString().equals("FINISHED")) {
                Bundle photoBundle = new Bundle();
                photoBundle.putSerializable("photos",photos_data);
                outState.putBundle(ClubContract.PHOTO_BUNDLE,photoBundle);
                outState.putBoolean("Incomplete", false);
            }
        }
        else {
            Bundle photoBundle = new Bundle();
            photoBundle.putSerializable("photos",photos_data);
            outState.putBundle(ClubContract.PHOTO_BUNDLE,photoBundle);
            outState.putBoolean("Incomplete",false);
        }
    }

    public void onDataFetched(){
        if(data_len==0){
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
            no_data.setVisibility(VISIBLE);
            swipe_text.setVisibility(VISIBLE);
        }
        else{
            no_internet.setVisibility(GONE);
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            photo_recyc_view.setVisibility(VISIBLE);
            swipe_text.setVisibility(GONE);

            layoutManager = new GridLayoutManager(getContext(),3);
            photo_recyc_view.setHasFixedSize(true);
            photo_recyc_view.setLayoutManager(layoutManager);

            recycAdapter = new RecycPhotosAdap(photos_data,getActivity());
            photo_recyc_view.setAdapter(recycAdapter);
        }
    }

    public boolean isConnectedFunc(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        isConnected = ninfo != null && ninfo.isConnectedOrConnecting();

        if(!isConnected){
            pbar.setVisibility(GONE);
            no_data.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
            no_internet.setVisibility(VISIBLE);
            swipe_text.setVisibility(VISIBLE);
            return false;
        }
        else {
            no_internet.setVisibility(GONE);
            no_data.setVisibility(GONE);
            swipe_text.setVisibility(GONE);
            return true;
        }
    }

    public void setNextData(){
            recycAdapter.notifyDataSetChanged();
    }

}
