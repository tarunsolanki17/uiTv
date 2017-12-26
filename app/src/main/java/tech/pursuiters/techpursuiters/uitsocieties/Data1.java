package tech.pursuiters.techpursuiters.uitsocieties;

/**
 * Created by Tarun on 17-Aug-17.
 */

/**
 ***********************  DATA MODEL FOR DIFFERENT CLUBS  ********************
 */
public class Data1 {

    int img_res_Id;
    String club_name;
    public String club_id;
    public int club_flag;

    public Data1(int res,String name,String id, int flag){
        img_res_Id = res;
        club_name = name;
        club_id = id;
        club_flag = flag;
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

    public int getClub_flag() {
        return club_flag;
    }
}
