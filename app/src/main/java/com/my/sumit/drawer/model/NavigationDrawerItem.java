package com.my.sumit.drawer.model;

import android.graphics.drawable.Drawable;

public class NavigationDrawerItem {
    private String title;
    private Drawable icon;

    public NavigationDrawerItem(){}

    public NavigationDrawerItem(String title, Drawable icon){
        this.title = title;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
