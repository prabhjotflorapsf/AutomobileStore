package com.example.automobilestore.Admin.Model_adapter;

import android.net.Uri;

public class AdminUserData {
    private String Name;
    private Uri image;
    private String id;
    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return Name;
    }

    public AdminUserData(String id,String name, String email, String phone,Uri image) {
        this.id=id;
        Name = name;
        Email = email;
        Phone = phone;
        this.image=image;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private String Email;
    private String Phone;

    public AdminUserData(){}
}
