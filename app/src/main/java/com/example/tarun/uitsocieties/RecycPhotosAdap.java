package com.example.tarun.uitsocieties;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.example.tarun.uitsocieties.R.id.photo_view;

/**
 * Created by Tarun on 18-Oct-17.
 */

public class RecycPhotosAdap extends RecyclerView.Adapter<RecycPhotosAdap.PhotoViewHolder> {

    ArrayList<PhotoParcel> photos_data;
    Context context;

    public RecycPhotosAdap(ArrayList<PhotoParcel> data,Context con) {
        photos_data = data;
        context = con;
    }

    @Override
    public RecycPhotosAdap.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.photo_recyc_layout, parent, false);
        RecycPhotosAdap.PhotoViewHolder viewHolder = new RecycPhotosAdap.PhotoViewHolder(photoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecycPhotosAdap.PhotoViewHolder holder, int position) {
        PhotoParcel curr_photo = photos_data.get(position);
        ImageView imageView = holder.photo_img_view;
        Glide.with(context)
                .load(curr_photo.getImage_link())
                .apply(new RequestOptions().placeholder(R.drawable.color_black))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return photos_data.size();
    }
    class PhotoViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView photo_img_view;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            photo_img_view = itemView.findViewById(R.id.photo_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                PhotoParcel curr_photo = photos_data.get(position);
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putParcelableArrayListExtra("photo_parcel",photos_data);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        }

    }
}
