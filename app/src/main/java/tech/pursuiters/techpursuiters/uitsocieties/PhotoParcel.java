package tech.pursuiters.techpursuiters.uitsocieties;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tarun on 15-Sep-17.
 */

public class PhotoParcel implements Parcelable {

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

    public PhotoParcel(String album_name, int photo_count, String caption, String created_time, String facebook_link, String place, String height, String width, String thumb_link, String image_link, String big_image_link) {
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

    protected PhotoParcel(Parcel input){
        album_name = input.readString();
        photo_count = input.readInt();
        caption = input.readString();
        created_time = input.readString();
        facebook_link = input.readString();
        place = input.readString();
        height = input.readString();
        width = input.readString();
        thumb_link = input.readString();
        image_link = input.readString();
        big_image_link = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(album_name);
        parcel.writeInt(photo_count);
        parcel.writeString(caption);
        parcel.writeString(created_time);
        parcel.writeString(facebook_link);
        parcel.writeString(place);
        parcel.writeString(height);
        parcel.writeString(width);
        parcel.writeString(thumb_link);
        parcel.writeString(image_link);
        parcel.writeString(big_image_link );
    }

    public static final Creator<PhotoParcel> CREATOR = new Creator<PhotoParcel>(){
        @Override
        public PhotoParcel createFromParcel(Parcel in) {
            return new PhotoParcel(in);
        }

        @Override
        public PhotoParcel[] newArray(int size) {
            return new PhotoParcel[size];
        }
    };

    public String getThumb_link() {
        return thumb_link;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getBig_image_link() {
        return big_image_link;
    }

    public String getCaption() {
        return caption;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public String getAlbum_name() {
        return album_name;
    }
}
