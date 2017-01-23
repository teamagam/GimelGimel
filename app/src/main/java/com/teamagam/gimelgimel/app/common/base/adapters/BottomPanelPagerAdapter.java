package com.teamagam.gimelgimel.app.common.base.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.sensor.view.SensorsContainerFragment;

import butterknife.BindArray;
import butterknife.ButterKnife;


public class BottomPanelPagerAdapter extends FragmentStatePagerAdapter {

    public static final int SENSORS_CONTAINER_POSITION = 0;
    public static final int MESSAGES_CONTAINER_POSITION = 1;

    private final Activity mActivity;
    @BindArray(R.array.bottom_panel_titles)
    String[] mTitles;
    private int mUnreadMessagesCount;

    public BottomPanelPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.mActivity = activity;
        ButterKnife.bind(this, mActivity);
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
        return mTitles.length;
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case SENSORS_CONTAINER_POSITION:
                return getSensorsContainerTitle();
            case MESSAGES_CONTAINER_POSITION:
                return getMessagesContainerTitle();
            default:
                return "";
        }
    }

    public static boolean isMessagesPage(int position) {
        return position == MESSAGES_CONTAINER_POSITION;
    }

    public static boolean isSensorsPage(int position) {
        return position == SENSORS_CONTAINER_POSITION;
    }

    private String getSensorsContainerTitle() {
        return mTitles[SENSORS_CONTAINER_POSITION];
    }

    private String getMessagesContainerTitle() {
        return mTitles[MESSAGES_CONTAINER_POSITION] + getMessagesCounterExtension();
    }

    private String getMessagesCounterExtension() {
        if (mUnreadMessagesCount > 0) {
            String string = mActivity.getString(R.string.bottom_panel_messages_counter,
                    mUnreadMessagesCount);
            return string;
        } else {
            return "";
        }
    }

    public void updateUnreadCount(int unreadMessagesCount) {
        mUnreadMessagesCount = unreadMessagesCount;
        notifyDataSetChanged();
    }
}
