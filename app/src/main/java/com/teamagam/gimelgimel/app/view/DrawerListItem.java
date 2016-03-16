package com.teamagam.gimelgimel.app.view;

import android.graphics.drawable.Drawable;

/**
 * Created by CV on 3/13/2016.
 */
public class DrawerListItem {

    private String mTitle;
    private Drawable mIcon;

    public DrawerListItem(String title, Drawable icon) {
        this.mTitle = title;
        this.mIcon = icon;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }
}