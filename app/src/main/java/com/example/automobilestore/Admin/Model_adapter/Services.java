package com.example.automobilestore.Admin.Model_adapter;

import android.net.Uri;

public class Services {
    String id;
    String Company,Address,url;
    private Uri image;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }


    public Services(String id, String company, String address, String url, Uri image) {
        this.id = id;
        Company = company;
        Address = address;
        this.url = url;
        this.image = image;
    }


}
