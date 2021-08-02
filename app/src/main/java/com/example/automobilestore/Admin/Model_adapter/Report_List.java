package com.example.automobilestore.Admin.Model_adapter;

import android.net.Uri;

public class Report_List {
    String id;
    String User,Reason;
    private Uri image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }



    public Report_List(String id, String user, String reason, Uri image) {
        this.id = id;
        User = user;
        Reason = reason;
        this.image = image;
    }


}
