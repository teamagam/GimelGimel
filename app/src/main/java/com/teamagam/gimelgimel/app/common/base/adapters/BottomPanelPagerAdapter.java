package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

import java.util.LinkedList;
import java.util.List;


public class BottomPanelPagerAdapter extends FragmentStatePagerAdapter {

    private final AppLogger sLogger = AppLoggerFactory.create(this.getClass());

    private final List<Integer> mIds;
    private final List<String> mTitles;
    private final List<FragmentFactory> mFragmentFactories;

    public BottomPanelPagerAdapter(FragmentManager fm) {
        super(fm);
        mIds = new LinkedList<>();
        mTitles = new LinkedList<>();
        mFragmentFactories = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentFactories.get(position).create();
    }

    @Override
    public int getCount() {
        return mIds.size();
    }

    @Override
    public String getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addPage(int id, String title, FragmentFactory factory) {
        addToLists(id, title, factory);
        notifyDataSetChanged();
    }

    public void removePage(int id) {
        int position = mIds.indexOf(id);
        if (position == -1) {
            sLogger.w("No such id ID: " + String.valueOf(id));
        } else {
            removeFromLists(position);
            notifyDataSetChanged();
        }
    }

    public void updatePage(int id, String title, FragmentFactory factory) {
        int position = mIds.indexOf(id);
        setLists(position, title, factory);
        notifyDataSetChanged();
    }

    public int getPosition(int id) {
        return mIds.indexOf(id);
    }

    public int getId(int position) {
        return mIds.get(position);
    }

    public void updateTitle(int position, String newTitle) {
        mTitles.set(position, newTitle);
        notifyDataSetChanged();
    }

    private void addToLists(int id, String title, FragmentFactory factory) {
        mIds.add(id);
        mTitles.add(title);
        mFragmentFactories.add(factory);
    }

    private void removeFromLists(int position) {
        mIds.remove(position);
        mTitles.remove(position);
        mFragmentFactories.remove(position);
    }

    private void setLists(int position, String title, FragmentFactory factory) {
        mTitles.set(position, title);
        mFragmentFactories.set(position, factory);
    }

    public interface FragmentFactory {
        Fragment create();
    }
}
