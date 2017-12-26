package tech.pursuiters.techpursuiters.uitsocieties;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tech.pursuiters.techpursuiters.uitsocieties.about_fragment.AboutFrag;
import tech.pursuiters.techpursuiters.uitsocieties.updates_fragment.UpdatesFrag;
import tech.pursuiters.techpursuiters.uitsocieties.videos_fragment.VideosFrag;

/**
 * Created by Tarun on 17-Aug-17.
 */

/***********************    PAGER ADAPTER FOR MAIN ACTIVITY     **********************************/

public class MyPagerAdap extends FragmentStatePagerAdapter {

    public MyPagerAdap(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new AboutFrag();
        if(InClub.login){
            if(position==1)
                return new UpdatesFrag();
            else if(position==2)
                return new EventsFrag();
            else if(position==3)
                return new PhotosFrag();
            else if(position==4)
                return new VideosFrag();
            else return null;
        }
        else{
            if (position >= 1&&position<=4)
                return new LoginFrag();
            else
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "About";
        else if(position==1)
            return "Updates";
        else if(position==2)
            return "Events";
        else if(position==3)
            return "Photos";
        else if(position==4)
            return "Videos";
        else return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
