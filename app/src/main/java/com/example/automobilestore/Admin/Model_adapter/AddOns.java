package com.example.automobilestore.Admin.Model_adapter;

import android.net.Uri;

public class AddOns {
    String id;
    String Comapny,Address,url;
    private Uri image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComapny() {
        return Comapny;
    }

    public void setComapny(String comapny) {
        Comapny = comapny;
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



    public AddOns(String id, String comapny, String address, String url, Uri image) {
        this.id = id;
        Comapny = comapny;
        Address = address;
        this.url = url;
        this.image = image;
    }


}
