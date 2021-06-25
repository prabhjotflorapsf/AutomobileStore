package com.example.automobilestore.model;

public class VerticalCarData {

    String name;
    String price;
    Integer imageUrl;
    String condition;

    public VerticalCarData(String name, String price, Integer imageUrl, String condition) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.condition = condition;
    }



    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }
}
