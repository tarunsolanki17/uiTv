package com.example.tarun.uitsocieties.updates_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tarun.uitsocieties.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_EVENT;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_LINK;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_PHOTO;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_STATUS;
import static com.example.tarun.uitsocieties.ClubContract.UpdatesConstants.UP_VIDEO;
import static com.example.tarun.uitsocieties.InClub.viewpgr;
import static com.example.tarun.uitsocieties.R.id.viewpg;

/**
 * Created by Tarun on 13-Dec-17.
 */

public class UpdatesListAdapter extends ArrayAdapter<UpdateParcel> {

    private Context con;
    private ArrayList<UpdateParcel> updates_data;
    private TextView update_type, message;
    private ImageView image;

    public UpdatesListAdapter( Context context, ArrayList<UpdateParcel> data) {
        super(context, 0, data);
        con = context;
        updates_data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View updateListView = convertView;

        if(updateListView==null){
            updateListView = LayoutInflater.from(con).inflate(R.layout.update_list_item,parent,false);
        }

        final UpdateParcel curr_update = updates_data.get(position);

        update_type = updateListView.findViewById(R.id.update_type);
        message = updateListView.findViewById(R.id.message);
        image = updateListView.findViewById(R.id.update_image);

        update_type.setText(type_selector(curr_update.getType()));
        message.setText(curr_update.getMessage());

        Glide.with(con)
                .load(curr_update.getPicture())
                .apply(new RequestOptions().placeholder(R.drawable.color_gray_place))
                .into(image);

        updateListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr_update.getType().equals(UP_LINK)){
                    Intent link_intent = new Intent(Intent.ACTION_VIEW);
                    link_intent.setData(Uri.parse(curr_update.getLink()));
                    if(link_intent.resolveActivity(con.getPackageManager())!=null)
                        con.startActivity(link_intent);
                }
                if(curr_update.getType().equals(UP_EVENT))
                    viewpgr.setCurrentItem(2);
                if(curr_update.getType().equals(UP_PHOTO))
                    viewpgr.setCurrentItem(3);
                if(curr_update.getType().equals(UP_VIDEO))
                    viewpgr.setCurrentItem(4);
            }
        });

        return updateListView;
    }

    public String type_selector(String type){
        switch (type){
            case UP_STATUS: return "New Status";
            case UP_LINK: return "New Link Upload";
            case UP_EVENT: return "New Event";
            case UP_PHOTO: return "New Photo Upload";
            case UP_VIDEO: return "New Video Upload";
        }
        return null;
    }

}
