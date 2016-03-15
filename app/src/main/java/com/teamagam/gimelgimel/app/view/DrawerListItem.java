package com.teamagam.gimelgimel.app.view;

import android.graphics.drawable.Drawable;

/**
 * Created by CV on 3/13/2016.
 */
public class DrawerListItem {

    private String title;
    private Drawable icon;

    public DrawerListItem(){}

    public DrawerListItem(String title, Drawable icon){
        this.title = title;
        this.icon = icon;
    }

    public DrawerListItem(String title, Drawable icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public Drawable getIcon(){
        return this.icon;
    }


    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(Drawable icon){
        this.icon = icon;
    }

}