package com.example.automobilestore.Admin.Model_adapter;

public class AdminUserData {
    private String Name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public String getName() {
        return Name;
    }

    public AdminUserData(String id,String name, String email, String phone) {
        this.id=id;
        Name = name;
        Email = email;
        Phone = phone;
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
