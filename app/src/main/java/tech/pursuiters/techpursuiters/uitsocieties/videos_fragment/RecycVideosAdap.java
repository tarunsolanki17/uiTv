package tech.pursuiters.techpursuiters.uitsocieties.videos_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by Tarun on 28-Oct-17.
 */

public class RecycVideosAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<VideoParcel> video_data;
    Context context;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;

    public RecycVideosAdap(ArrayList<VideoParcel> data, Context con) {
        video_data = data;
        context = con;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View videoView = inflater.inflate(tech.pursuiters.techpursuiters.uitsocieties.R.layout.video_recyc_layout, parent, false);
            return new RecycVideosAdap.VideoViewHolder(videoView);
        }
        else if(viewType == TYPE_FOOTER){
            View footer = LayoutInflater.from(context).inflate(tech.pursuiters.techpursuiters.uitsocieties.R.layout.footer, parent, false);
            return new RecycVideosAdap.FooterViewHolder(footer);
        }
        else
            throw new RuntimeException("No view with defined types was found");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VideoViewHolder) {

            VideoParcel curr_video = video_data.get(position);
            ImageView imageView = ((VideoViewHolder) holder).thumb;
            Glide.with(context)
                    .load(curr_video.getThumbnail_url())
                    .apply(new RequestOptions().placeholder(tech.pursuiters.techpursuiters.uitsocieties.R.drawable.color_gray_place))
                    .into(imageView);
        }
        else if(holder instanceof FooterViewHolder){
            //  DO NOTHING
        }
    }

    @Override
    public int getItemCount() {
        return (video_data.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==video_data.size())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    private class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        TextView descp;
        ImageView thumb;

        public VideoViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(tech.pursuiters.techpursuiters.uitsocieties.R.id.thumb_v);
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

    private class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
