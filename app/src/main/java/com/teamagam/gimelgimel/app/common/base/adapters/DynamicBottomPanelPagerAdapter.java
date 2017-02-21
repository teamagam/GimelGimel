package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;
import java.util.List;


public class DynamicBottomPanelPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mTitles;
    private final List<FragmentFactory> mFragmentFactories;
    private int mUnreadMessagesCount;

    public DynamicBottomPanelPagerAdapter(FragmentManager fm) {
        super(fm);
        mTitles = new LinkedList<>();
        mFragmentFactories = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentFactories.get(position).create();
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public String getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addPage(String title, FragmentFactory factory, int position) {
        mTitles.add(position, title);
        mFragmentFactories.add(position, factory);
        notifyDataSetChanged();
    }

    public void addPage(String title, FragmentFactory factory) {
        addPage(title, factory, getCount());
    }

    public void removePage(int position) {
        mTitles.remove(position);
        mFragmentFactories.remove(position);
        notifyDataSetChanged();
    }

    public void removePage(String title) {
        int i = mTitles.indexOf(title);
        removePage(i);
    }

    public void removePage(FragmentFactory fragmentFactory) {
        int i = mFragmentFactories.indexOf(fragmentFactory);
        removePage(i);
    }

    public void updateTitle(int position, String newTitle) {
        mTitles.set(position, newTitle);
        notifyDataSetChanged();
    }
    public void updateUnreadCount(int unreadMessagesCount) {
        mUnreadMessagesCount = unreadMessagesCount;
        notifyDataSetChanged();
    }

    public interface FragmentFactory {
        Fragment create();
    }
}
