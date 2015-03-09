package com.my.sumit.drawer.model;

public class NavigationDrawerItem {
    private String title;
    private int icon;

    public NavigationDrawerItem(){}

    public NavigationDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
