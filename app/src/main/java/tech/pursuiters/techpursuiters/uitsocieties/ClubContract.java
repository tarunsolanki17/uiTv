package tech.pursuiters.techpursuiters.uitsocieties;

/**
 * Created by Tarun on 15-Sep-17.
 */

public class ClubContract  {

    public static final String uiTv_LINK = "https://play.google.com/store/apps/details?id=tech.pursuiters.techpursuiters.uitsocieties&hl=en";

    public static final String PHOTOS_SERIAL = "photos_serial";
    public static final String PHOTO_FILE = "Photo_File";
    public static final String PHOTO_BUNDLE = "photo_bundle";
    public static final String NOTIF_UPDATE = "Notif_Intent_Update";
    public static final String NOTIF_EVENT = "Notif_Intent_Event";
    public static final String PHOTO_DATA = "photo_data";

    public static final String AAVAHAN = "1706949069322058";
    public static final String ACM_RGPV = "1356583771115130";
    public static final String COHERENT = "493932347436578";
//    public static final String DISHANJALI = "1406845922941085";
    public static final String E_CELL = "1756876974557988";
    public static final String GREEN_ARMY = "339750026115267";
    public static final String HACKER_EARTH = "106829536670133";
    public static final String INSYNC = "581644598564495";//"270979900087022";
    public static final String I_SPEAK_AALAY = "542097419297226";
    public static final String MAHASANGRAM = "115089709174856";
    public static final String PHOENIX = "562423343851594";
    public static final String SHANKHNAAD = "1488226447936457";
    public static final String SRIJAN = "821884027935078";
    public static final String SUNDARBAN = "1014248005370111";
    public static final String TECHNOPHILIC = "738646856321850";
    public static final String TEDX_RGPV = "117643995501901";

    public static final String TECH_PURSUITERS_EMAIL = "techpursuiters@gmail.com";
    public static final String TECH_PURSUITERS_PASS = "souldopplegangers";

    public static final String[] CLUB_NAMES = {"AAVAHAN","ACM_RGPV","COHERENT","E_CELL","GREEN_ARMY","HACKER_EARTH","INSYNC","I_SPEAK_AALAY","MAHASANGRAM","PHOENIX","SHANKHNAAD","SRIJAN", "SUNDARBAN","TECHNOPHILIC","TEDX_RGPV"};
    public static final String[] CLUB_NAMES_VISIBLE = {"Aavahan","ACM-Student Chapter RGPV","Coherent","E-Cell","Green Army","Hacker Earth","Insync","I Speak Aalay ","Mahasangram","Phoenix","शंखनाद","Srijan","Sundarban","Technophilic","TEDx RGPV"};
    public static final String[] CLUB_IDS = {AAVAHAN,ACM_RGPV,COHERENT,E_CELL,GREEN_ARMY,HACKER_EARTH,INSYNC,I_SPEAK_AALAY,MAHASANGRAM,PHOENIX,SHANKHNAAD,SRIJAN,SUNDARBAN,TECHNOPHILIC,TEDX_RGPV};

    public static final String[] REFERRAL_CODES = {"H4E7sI","oOn1ev","wbdvG1","WU7UWy","UpkGC4","uj2AYW","ASamdb","eOWkZz","TIMMvk","rC7RAY","3cS3Rf","IPKeco","80L8Sx","UXJ4mw","5z7xR6","wFTGp4","Y4AYcA","CNE99B","zJdeyM","bIv0Va","mxVdFF","1n4CqS","VhIM7g","VW0ewX","0yaLUB","n3mlq1","00QvQW","PIgVdk","9am9Qr","qwGl2W","VB2TTp","8zyOkj","f7UQ1A","YQ147e","84W2xj","iATfe5","rl9AO9","IlNp1R", "azll3c","kivM1n","y0vwD7","gj7AQg","ofEiO8","XMh0Jg","VzK1uX","ic0lQH","avv4hp","54SUqu","itqSXv","LGY0De","UCIhm3","MQ410Y","rpe5x0","I2BK11","Rvty1J","Z8UU8S","ajEJbl","SJ6w9I","bylL47","YpSwkl","BQzcfL","ABS66H","u5S4rV","PiTV7I","TlLMjH","uC4AD6","nB86nY","ywvQlW","rCJ6fS","OJTNl7"};

    public class UserData{
        public static final String ID = "id";
        public static final String EMAIL = "email";
    }

    public class UpdatesConstants{
        public static final String ID = "id";
        public static final String CAPTION = "caption";      //  ACTUALLY AN EVENT NAME
        public static final String CREATED_TIME = "created_time";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";
        public static final String MESSAGE = "message";     //   POST CAPTION
        public static final String NAME = "name";           //   NAME OF EVENT
        public static final String PERMA_LINK = "permalink_url";
        public static final String PICTURE = "picture";
        public static final String FULL_PICTURE = "full_picture";
        public static final String PLACE = "place";
        public static final String LOCATION = "location";
        public static final String CITY = "city";
        public static final String SOURCE = "source";       //  SOURCE OF ANY FILE OR ATTACHED VIDEO
        public static final String STATUS_TYPE = "status_type";
        public static final String TYPE = "type";

        /**         CONSTANTS FOR TYPE OF STATUS                 */
        public static final String UP_STATUS = "status";
        public static final String UP_LINK = "link";
        public static final String UP_EVENT = "event";
        public static final String UP_PHOTO = "photo";
        public static final String UP_VIDEO = "video";

        public static final String STATUS_DATA = "status_data";
        public static final String UPDATE_PHOTO_URL = "cover_url";
        public static final String UPDATE_VIDEO_URL = "source_url";
        public static final String UPDATE_VIDEO = "update";



    }

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

    public class VideosConstants{
        public static final String DATA = "data";
        public static final String VIDEOS = "videos";
        public static final String ID = "id";
        public static final String CREATED_TIME = "created_time";
        public static final String DESCRIPTION = "description";
        public static final String LENGTH = "length";
        public static final String PLACE = "place";
        public static final String PICTURE = "picture";
        public static final String SOURCE = "source";
        public static final String THUMBNAILS = "thumbnails";
        public static final String IS_PREFERRED = "is_preferred";
        public static final String THUMBNAIL_URL = "uri";
    }
}
