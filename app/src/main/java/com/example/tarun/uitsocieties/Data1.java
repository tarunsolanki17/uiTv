package com.example.tarun.uitsocieties;

/**
 * Created by Tarun on 17-Aug-17.
 */

public class Data1 {
    int img_res_Id;
    String club_name;
    public Data1(int res,String name){
        img_res_Id = res;
        club_name = name;
    }

    public int getImg_res_Id() {
        return img_res_Id;
    }

    public String getClub_name() {
        return club_name;
    }
}
