package com.example.tarun.uitsocieties;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static com.example.tarun.uitsocieties.MainActivity.width;

/**
 * Created by Tarun on 16-Aug-17.
 */

/**********************  ARRAY ADAPTER FOR CLUB LOGOS GRID LAYOUT  ********************/

public class MyArrayAdap extends ArrayAdapter<Data1> {

    public static int CLUB_NO;
    Context con;
    ArrayList<Data1> club_data;


    public MyArrayAdap(Context context, @LayoutRes int resource, @NonNull ArrayList<Data1> data) {
        super(context, 0, data);
        con = context;
        club_data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.v("---Running---","getView");

        View gridItemView = convertView;
        if(gridItemView==null){
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout,parent,false);
        }

        final Data1 curr_item = getItem(position);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width/2,width/2);

        ImageView imgvar = (ImageView) gridItemView.findViewById(R.id.imgv);
        imgvar.setLayoutParams(layoutParams);
        imgvar.setImageResource(curr_item.getImg_res_Id());
//        imgvar.setBaseline(width/2);

        TextView tvar = (TextView) gridItemView.findViewById(R.id.tv);
        tvar.setPadding(0,width/2,0,0);
        tvar.setLayoutParams(new RelativeLayout.LayoutParams(width/2, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvar.setText(curr_item.getClub_name());


        gridItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(con,InClub.class);
                in.setFlags(position);
                Log.v("Position---",String.valueOf(position));
                in.putExtra("CLUB_ID",curr_item.getClub_id());

                con.startActivity(in);
            }
        });

        return gridItemView;








//        View gridItemView = convertView;

/*        ImageView builtimgview;
        if(convertView==null) {
            builtimgview = new ImageView(con);
            builtimgview.setLayoutParams(new GridView.LayoutParams(width/2,width/2));
            builtimgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            builtimgview.setBackgroundColor(Color.rgb(0,0,0));
//            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout,parent,false);
        }
        else{
            builtimgview = (ImageView) convertView;
        }

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.family_son);
        images.add(R.drawable.family_daughter);
        images.add(R.drawable.family_father);
        images.add(R.drawable.family_mother);
        images.add(R.drawable.family_grandfather);
        images.add(R.drawable.family_grandmother);

        Integer curr_item = getItem(position);

//        ImageView img = gridItemView.findViewById(R.id.imgv);
        builtimgview.setImageResource(curr_item);

        return builtimgview;
*/
    }
}
