package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.app.Fragment;
import android.graphics.drawable.Drawable;

/**
 * Data-object representing a drawer item
 */
public class DrawerListItem {

    /**
     * Title to display in the list
     */
    private String mTitle;

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

    public DrawerListItem(String title, Drawable icon, int containerViewResourceId,
                          Fragment fragment) {
        mTitle = title;
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

    public String getTitle() {
        return mTitle;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }
}