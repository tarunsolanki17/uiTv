package com.example.tarun.uitsocieties;

import java.util.ArrayList;

import static android.graphics.Color.GREEN;

/**
 * Created by Tarun on 15-Sep-17.
 */

public class ClubContract  {
    public static final String COHERENT = "493932347436578";
    public static final String E_CELL = "1756876974557988";
    public static final String GREEEN_ARMY = "339750026115267";
    public static final String INSYNC = "270979900087022";//"581644598564495";
    public static final String PHOENIX = "562423343851594";
    public static final String SUNDARBAN = "1014248005370111";

    public static final String[] CLUB_NAMES = {"COHERENT","E_CELL","GREEN_ARMY","INSYNC","PHOENIX","SUNDARBAN"};
    public static final String[] CLUB_IDS = {COHERENT,E_CELL,GREEEN_ARMY,INSYNC,PHOENIX,SUNDARBAN};

    public class EventsConstants{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String PLACE = "place";
        public static final String LOCATION = "location";
        public static final String CITY = "city";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String DESCRIPTION = "description";
        public static final String COVER = "cover";
        public static final String SOURCE_URL = "source";

    }
    public class PhotosConstants{
        public static final String ALBUMS = "albums";
        public static final String ALBUM_NAME = "name";
        public static final String PHOTO_COUNT = "photo_count";
        public static final String PHOTOS = "photos";
        public static final String PHOTO_NAME = "name";
        public static final String CREATED_TIME = "created_time";
        public static final String FACEBOOK_LINK = "link";
        public static final String PLACE = "place";
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String THUMB_PICTURE = "picture";
        public static final String IMAGES = "images";
        public static final String SOURCE = "source";
        public static final String PAGING = "paging";
        public static final String NEXT = "next";

    }
}
