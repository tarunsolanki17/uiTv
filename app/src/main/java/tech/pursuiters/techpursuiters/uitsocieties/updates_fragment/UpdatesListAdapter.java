package tech.pursuiters.techpursuiters.uitsocieties.updates_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import tech.pursuiters.techpursuiters.uitsocieties.events_fragment.EventsDeatiledPhotoActivity;
import tech.pursuiters.techpursuiters.uitsocieties.videos_fragment.VideosDetail;

import java.util.ArrayList;

import tech.pursuiters.techpursuiters.uitsocieties.ClubContract;
import tech.pursuiters.techpursuiters.uitsocieties.InClub;

import static android.content.Intent.ACTION_VIEW;

/**
 * Created by Tarun on 13-Dec-17.
 */

public class UpdatesListAdapter extends ArrayAdapter<UpdateParcel> {

    private Context con;
    private ArrayList<UpdateParcel> updates_data;
    private TextView update_type, message;
    private ImageView image,facebook;

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
            updateListView = LayoutInflater.from(con).inflate(tech.pursuiters.techpursuiters.uitsocieties.R.layout.update_list_item,parent,false);
        }

        final UpdateParcel curr_update = updates_data.get(position);

        update_type = updateListView.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.update_type);
        message = updateListView.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.message);
        image = updateListView.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.update_image);
        facebook = updateListView.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.imageView);

        update_type.setText(type_selector(curr_update.getType()));
        message.setText(curr_update.getMessage());

        if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_EVENT))
            message.setText(curr_update.getCaption());

        if(curr_update.getPicture().isEmpty()){
            image.setImageResource(tech.pursuiters.techpursuiters.uitsocieties.R.drawable.no_preview);
        }
        else {
            Glide.with(con)
                    .load(curr_update.getPicture())
                    .apply(new RequestOptions().placeholder(tech.pursuiters.techpursuiters.uitsocieties.R.drawable.color_gray))
                    .into(image);
        }

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebook_intent = new Intent(ACTION_VIEW);
                facebook_intent.setData(Uri.parse(curr_update.getPermalink_url()));
                if((facebook_intent.resolveActivity(con.getPackageManager())!=null))
                    con.startActivity(facebook_intent);
            }
        });

        updateListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_STATUS)){
                    Intent statusAct = new Intent(con,StatusDetail.class);
                    statusAct.putExtra(ClubContract.UpdatesConstants.STATUS_DATA,curr_update);
                    con.startActivity(statusAct);
                }

                else if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_LINK)){
                    Intent link_intent = new Intent(ACTION_VIEW);
                    link_intent.setData(Uri.parse(curr_update.getLink()));
                    if(link_intent.resolveActivity(con.getPackageManager())!=null)
                        con.startActivity(link_intent);
                }

                else if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_EVENT)){
                    InClub.viewpgr.setCurrentItem(2);
                }

                else if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_PHOTO)){
                    Intent photo_view = new Intent(con, EventsDeatiledPhotoActivity.class);
                    photo_view.putExtra(ClubContract.UpdatesConstants.UPDATE_PHOTO_URL,curr_update.getFull_picture());
                    /*photo_view.putExtra(PHOTO_NAME,curr_update.getMessage());
                    photo_view.putExtra(CREATED_TIME,curr_update.getCreated_time());*/
                    con.startActivity(photo_view);
                }

                else if(curr_update.getType().equals(ClubContract.UpdatesConstants.UP_VIDEO)){
                    Intent update_video = new Intent(con, VideosDetail.class);
                    update_video.putExtra(ClubContract.UpdatesConstants.UPDATE_VIDEO_URL,curr_update.getSource());
                    update_video.putExtra(ClubContract.UpdatesConstants.UPDATE_VIDEO,true);
                    con.startActivity(update_video);
                }

            }
        });

        return updateListView;
    }

    public String type_selector(String type){
        switch (type){
            case ClubContract.UpdatesConstants.UP_STATUS: return "Status Update";
            case ClubContract.UpdatesConstants.UP_LINK: return "Link Upload";
            case ClubContract.UpdatesConstants.UP_EVENT: return "New Event";
            case ClubContract.UpdatesConstants.UP_PHOTO: return "Photo Upload";
            case ClubContract.UpdatesConstants.UP_VIDEO: return "Video Upload";
        }
        return null;
    }

}
