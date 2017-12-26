package tech.pursuiters.techpursuiters.uitsocieties.photos_fragment;

import java.io.Serializable;

/**
 * Created by Tarun on 17-Dec-17.
 */

public class Photo_Serial implements Serializable {

    private String album_name;
    private int photo_count;
    private String caption;
    private String created_time;
    private String facebook_link;
    private String place;
    private String height;
    private String width;
    private String thumb_link;
    private String image_link;
    private String big_image_link;

    public Photo_Serial(String album_name, int photo_count, String caption, String created_time, String facebook_link, String place, String height, String width, String thumb_link, String image_link, String big_image_link) {
        this.album_name = album_name;
        this.photo_count = photo_count;
        this.caption = caption;
        this.created_time = created_time;
        this.facebook_link = facebook_link;
        this.place = place;
        this.height = height;
        this.width = width;
        this.thumb_link = thumb_link;
        this.image_link = image_link;
        this.big_image_link = big_image_link;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public int getPhoto_count() {
        return photo_count;
    }

    public String getCaption() {
        return caption;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public String getPlace() {
        return place;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getThumb_link() {
        return thumb_link;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getBig_image_link() {
        return big_image_link;
    }
}
