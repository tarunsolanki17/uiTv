package com.example.tarun.uitsocieties;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tarun.uitsocieties.photos_fragment.Photo_Serial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.example.tarun.uitsocieties.ClubContract.PHOTO_FILE;
import static com.example.tarun.uitsocieties.MainActivity.width;
import static com.example.tarun.uitsocieties.R.id.photo_view;

/**
 * Created by Tarun on 18-Oct-17.
 */

public class RecycPhotosAdap extends RecyclerView.Adapter<RecycPhotosAdap.PhotoViewHolder> {

    ArrayList<Photo_Serial> photos_data;
    Context context;

    public RecycPhotosAdap(ArrayList<Photo_Serial> data,Context con) {
        photos_data = data;
        context = con;
    }

    @Override
    public RecycPhotosAdap.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.photo_recyc_layout, parent, false);
        RecycPhotosAdap.PhotoViewHolder viewHolder = new RecycPhotosAdap.PhotoViewHolder(photoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecycPhotosAdap.PhotoViewHolder holder, int position) {
        Photo_Serial curr_photo = photos_data.get(position);
        ImageView imageView = holder.photo_img_view;
        imageView.getLayoutParams().height = width/3;
        Glide.with(context)
                .load(curr_photo.getImage_link())
                .apply(new RequestOptions().placeholder(R.drawable.color_gray_place))
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

            File photoFile = new File(context.getFilesDir(),PHOTO_FILE);
            photoFile.delete();
            if (!photoFile.exists()) {
                try {
                    photoFile.createNewFile();
                    Log.v("File created---","Now");
                    FileOutputStream fos = context.openFileOutput(PHOTO_FILE, context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(photos_data);
                    oos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.v("File not created---","Not");
            }

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        }
    }
}
