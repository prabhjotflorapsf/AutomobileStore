package com.example.automobilestore.model;

import android.net.Uri;

public class WishlistModel {
    String Docid;
    String price;
    String Model;
    String Transmission;String Year;
    String Address;
    String Condition;
    private Uri image;





    public WishlistModel(String docid, String price, String model, String transmission, String Year, String address, String condition, Uri image) {
        Docid = docid;
        this.price = price;
        Model = model;
        Transmission = transmission;
        Address = address;
        Condition = condition;
        this.image = image;
        this.Year=Year;
    }


    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
    public String getDocid() {
        return Docid;
    }

    public void setDocid(String docid) {
        Docid = docid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getTransmission() {
        return Transmission;
    }

    public void setTransmission(String transmission) {
        Transmission = transmission;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }


}
