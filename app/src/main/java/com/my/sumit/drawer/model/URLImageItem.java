package com.my.sumit.drawer.model;

/**
 * Created by Ken on 3/15/2015.
 */
public class URLImageItem {
    private String friendName;
    private String imageURL;

    public URLImageItem(){}

    public URLImageItem(String friendName, String imageURL){
        this.friendName = friendName;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
