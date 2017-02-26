package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;
import java.util.List;


public class BottomPanelPagerAdapter extends FragmentStatePagerAdapter {

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

    public void addPage(int id, String title, FragmentFactory factory) {
        addToLists(id, title, factory);
        notifyDataSetChanged();
    }

    public void removePage(int id) {
        int position = mIds.indexOf(id);
        if (position == -1) {
            System.out.println("no such id ID: " + String.valueOf(id));
        } else {
            removeFromLists(position);
            notifyDataSetChanged();
        }
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

    public interface FragmentFactory {
        Fragment create();
    }
}
