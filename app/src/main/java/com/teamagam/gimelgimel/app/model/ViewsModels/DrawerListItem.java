package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.app.Fragment;
import android.graphics.drawable.Drawable;

/**
 * Data-object representing a drawer item
 */
public class DrawerListItem {

    /**
     * Name to display in the list
     */
    private String mName;

    /**
     * Icon to display in the list
     */
    private Drawable mIcon;

    /**
     * container view resource id
     */
    private int mContainerViewResourceId;

    /**
     * Fragment to assign to container view
     */
    private Fragment mFragment;

    public DrawerListItem(String name, Drawable icon, int containerViewResourceId,
                          Fragment fragment) {
        mName = name;
        mIcon = icon;
        mContainerViewResourceId = containerViewResourceId;
        mFragment = fragment;
    }

    public int getContainerViewResourceId() {
        return mContainerViewResourceId;
    }

    public void setContainerViewResourceId(int placeholderId) {
        mContainerViewResourceId = placeholderId;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setmFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }
}