package com.example.tarun.uitsocieties;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.example.tarun.uitsocieties.InClub.ft;
import static com.example.tarun.uitsocieties.InClub.login;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFrag extends Fragment implements MyRecycPhotoAdapter.MyPhotoClickListener{

    ArrayList<Integer> photos;
    TextView no_internet, no_data;
    RecyclerView photo_recyc_view;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public PhotosFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(login){
//
//        }
//        else{
//
//        }

        // TODO --> LOOK FOR INTERNET CONNECTION
        // TODO --> LOOK FOR DATA AVAILABLE

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        no_internet = (TextView) view.findViewById(R.id.no_internet);
        no_data = (TextView) view.findViewById(R.id.no_data);

        photo_recyc_view = (RecyclerView) view.findViewById(R.id.photo_recyc_view);

        photos = new ArrayList<>();
        photos.add(R.drawable.family_grandmother);
        photos.add(R.drawable.coherent);
        photos.add(R.drawable.d);
        photos.add(R.drawable.family_daughter);
        photos.add(R.drawable.insync);
        photos.add(R.drawable.b);
        photos.add(R.drawable.family_grandfather);
        photos.add(R.drawable.family_son);
        photos.add(R.drawable.c);
        photos.add(R.drawable.family_mother);
        photos.add(R.drawable.green_army);
        photos.add(R.drawable.family_father);
//        photos.add(R.drawable.a);
//        photos.add(R.drawable.d);
//        photos.clear();

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        boolean isConnected = ninfo != null && ninfo.isConnectedOrConnecting();

        if(!isConnected){
//            TODO --> IMAGE WITH TEXT IN NO INTERNET
//            TODO --> REFRESH BUTTON
            no_data.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
        }
        else if(photos.size()==0){
//            TODO --> IMAGE WITH TEXT IN NO DATA
            no_internet.setVisibility(GONE);
            photo_recyc_view.setVisibility(GONE);
        }
        else{
            no_data.setVisibility(GONE);
            no_internet.setVisibility(GONE);

            MyRecycPhotoAdapter photo_adap = new MyRecycPhotoAdapter(photos,photos.size(),this);

            // TODO --> IMPLEMENT STAGGERED GRID LAYOUT MANAGER
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);

            photo_recyc_view.setLayoutManager(layoutManager);
            photo_recyc_view.setHasFixedSize(true);
            photo_recyc_view.setAdapter(photo_adap);
        }

        return view;
    }

    @Override
    public void onPhotoClick(int pos) {
        Toast.makeText(getContext(),"Photo Tapped",Toast.LENGTH_SHORT).show();
        Intent in = new Intent(getContext(),DetailActivity.class);
        in.putIntegerArrayListExtra("Photos",photos);
        in.putExtra("position",pos);
        startActivity(in);
    }
    public void photosjsonRequest(){
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/581644598564495",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "albums{photo_count,photos{picture,images,name,album,created_time}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

//    public static void fragReplacer(){
//        login = AccessToken.getCurrentAccessToken()!=null;
//
//        if(login){
//            ft.replace(R.id.frame,new AboutFrag());
//            Log.v("Replacing----","Login with ABOUT");
//        }
//        if(!login){
//            ft.replace(R.id.frame,new LoginFrag2());
//            Log.v("Replacing----","About with LOGIN");
//        }
//    }
}
