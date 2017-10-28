package com.example.tarun.uitsocieties.videos_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tarun.uitsocieties.PhotoParcel;
import com.example.tarun.uitsocieties.R;
import com.example.tarun.uitsocieties.RecycPhotosAdap;

import java.util.ArrayList;

import static com.example.tarun.uitsocieties.R.id.descp;

/**
 * Created by Tarun on 28-Oct-17.
 */

public class RecycVideosAdap extends RecyclerView.Adapter<RecycVideosAdap.VideoViewHolder> {

    ArrayList<VideoParcel> video_data;
    Context context;

    public RecycVideosAdap(ArrayList<VideoParcel> data, Context con) {
        video_data = data;
        context = con;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View videoView = inflater.inflate(R.layout.video_recyc_layout, parent, false);
        RecycVideosAdap.VideoViewHolder viewHolder = new RecycVideosAdap.VideoViewHolder(videoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoParcel curr_video = video_data.get(position);
//        holder.descp.setText(curr_video.getDescp());
        ImageView imageView = holder.thumb;
        Glide.with(context)
                .load(curr_video.getThumbnail_url())
                .apply(new RequestOptions().placeholder(R.drawable.color_gray_place))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return video_data.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        TextView descp;
        ImageView thumb;

        public VideoViewHolder(View itemView) {
            super(itemView);
//            descp = itemView.findViewById(R.id.descp_v);
            thumb = itemView.findViewById(R.id.thumb_v);
            //  TODO --> SET CORRECT HEIGHT
//            thumb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,350));
//            photo.setLayoutParams(new ViewGroup.MarginLayoutParams(2,2));
            thumb.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Intent video_intent = new Intent(context,VideosDetail.class);
                video_intent.putParcelableArrayListExtra("video_parcel",video_data);
                video_intent.putExtra("position",position);
                context.startActivity(video_intent);
            }
        }
    }
}
