package com.my.sumit.models;

/**
 * Created by Ken on 3/11/2015.
 */
public class CurrentUser{
    private static String currentUserName;
    private static String currentUserImageURL;
    private static String currentUserAboutMe;
    private static String currentUserOccupation;
    private static String currentUserOrganization;

    public CurrentUser(String currentUserName, String currentUserAboutMe,
                       String currentUserImageURL, String currentUserOccupation, String currentUserOrganization){
        this.currentUserName = currentUserName;
        this.currentUserAboutMe = currentUserAboutMe;
        this.currentUserImageURL = currentUserImageURL;
        this.currentUserOccupation = currentUserOccupation;
        this.currentUserOrganization = currentUserOrganization;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public static String getCurrentUserImageURL() {
        return currentUserImageURL;
    }

    public void setCurrentUserImageURL(String currentUserImageURL) {
        this.currentUserImageURL = currentUserImageURL;
    }

    public static String getCurrentUserAboutMe() {
        return currentUserAboutMe;
    }

    public void setCurrentUserAboutMe(String currentUserAboutMe) {
        this.currentUserAboutMe = currentUserAboutMe;
    }

    public static String getCurrentUserOccupation() {
        return currentUserOccupation;
    }

    public static void setCurrentUserOccupation(String currentUserOccupation) {
        CurrentUser.currentUserOccupation = currentUserOccupation;
    }

    public static String getCurrentUserOrganization() {
        return currentUserOrganization;
    }

    public static void setCurrentUserOrganization(String currentUserOrganization) {
        CurrentUser.currentUserOrganization = currentUserOrganization;
    }
}
