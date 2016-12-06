package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorsContainerFragment;


/**
 * Created by Admin on 06/12/2016.
 */

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new SensorsContainerFragment();
            case 1:
                return new MessagesContainerFragment();
            default:
                return null;

            //TODO: do we need arguments?
            //Bundle args = new Bundle();
            //args.putInt(S);
            // fragment.setArguments(args);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

   /* @Override
    public CharSequence getPageTitle(int position) {

    }*/
}
