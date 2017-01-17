package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorsContainerFragment;


public class BottomPanelPagerAdapter extends FragmentStatePagerAdapter {

    public static final int SENSORS_CONTAINER_POSITION = 0;
    public static final int MESSAGES_CONTAINER_POSITION = 1;
    private String[] mTitles;
    private int mNumOfTitles;
    private int mUnreadMessagesCount;

    public BottomPanelPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
        mNumOfTitles = titles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case SENSORS_CONTAINER_POSITION:
                return new SensorsContainerFragment();
            case MESSAGES_CONTAINER_POSITION:
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
        switch (position) {
            case SENSORS_CONTAINER_POSITION:
                return mTitles[position];
            case MESSAGES_CONTAINER_POSITION:
                return mTitles[position] + getMessagesCounterExtension();
            default:
                return null;
        }
    }

    @NonNull
    private String getMessagesCounterExtension() {
        if (mUnreadMessagesCount > 0) {
            return " (" + Integer.toString(mUnreadMessagesCount) + ")";
        } else {
            return "";
        }
    }

    public void updateUnreadCount(int unreadMessagesCount) {
        mUnreadMessagesCount = unreadMessagesCount;
    }
}
