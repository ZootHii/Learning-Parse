package com.zoothii.parseproject;


import android.net.Uri;

import java.util.Date;

class Post {
    private String username;
    private String date;
    private Uri imagePost;
    private Uri imageProfile;

    public Post(String username, String date, Uri imagePost, Uri imageProfile) {
        this.username = username;
        this.date = date;
        this.imagePost = imagePost;
        this.imageProfile = imageProfile;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getImagePost() {
        return imagePost;
    }

    public void setImagePost(Uri imagePost) {
        this.imagePost = imagePost;
    }

    public Uri getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(Uri imageProfile) {
        this.imageProfile = imageProfile;
    }

}
