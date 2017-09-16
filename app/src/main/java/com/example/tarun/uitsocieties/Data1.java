package com.example.tarun.uitsocieties;

/**
 * Created by Tarun on 17-Aug-17.
 */

/**
 ***********************  DATA MODEL FOR DIFFERENT CLUBS  ********************
 */
public class Data1 {

    int img_res_Id;
    String club_name;
    String club_id;

    public Data1(int res,String name,String id){
        img_res_Id = res;
        club_name = name;
        club_id = id;
    }

    public int getImg_res_Id() {
        return img_res_Id;
    }

    public String getClub_name() {
        return club_name;
    }

    public String getClub_id() {
        return club_id;
    }
}
