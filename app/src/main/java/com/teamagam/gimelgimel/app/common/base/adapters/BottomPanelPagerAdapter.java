package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorsContainerFragment;


public class BottomPanelPagerAdapter extends FragmentStatePagerAdapter {
    
    private String[] mTitles;
    private int mNumOfTitles;
    
    public BottomPanelPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
        mNumOfTitles = titles.length;
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
        }
    }

    @Override
    public int getCount() {
        return mNumOfTitles;
    }

    @Override
    public String getPageTitle(int position) {
        return mTitles[position];
    }
}
