package com.example.tarun.uitsocieties;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.tarun.uitsocieties.MainActivity.width;
import static com.example.tarun.uitsocieties.R.id.photo_view;
import static java.security.AccessController.getContext;

/**
 * Created by Tarun on 06-Sep-17.
 */

public class MyRecycPhotoAdapter extends RecyclerView.Adapter<MyRecycPhotoAdapter.PhotoViewHolder> {

    int items;
    ArrayList<Integer> photo_collection;
    MyPhotoClickListener myPhotoClickListener;

    public MyRecycPhotoAdapter(ArrayList<Integer> photos_data, int count, MyPhotoClickListener listener) {
        photo_collection = photos_data;
        items = count;
        myPhotoClickListener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photo_recyc_layout,parent,false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.image_binder(photo_collection.get(position),position);
    }

    @Override
    public int getItemCount() {
        return items;
    }

    public interface MyPhotoClickListener{
        void onPhotoClick(int position);
    }
    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        WindowManager wm;
        int position;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(photo_view);
            wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
            itemView.setOnClickListener(this);
        }

        void image_binder(int photo_id,int pos){
            // TODO --> BIND THE IMAGE VIEW WITH THE DATA
            position = pos;
            // TODO --> CHANGE THE SIZE OF THE IMAGE SUCH THAT THERE'S NO NEED TO RESIZE THE IMAGE BY THE PRROCESSOR
            // TODO --> CHANGE THE LAYOUT FOR LANDSCAPE MODE
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width/3,width/3));
            imageView.setImageResource(photo_id);
        }

        @Override
        public void onClick(View view) {
            myPhotoClickListener.onPhotoClick(position);
        }
    }
}
