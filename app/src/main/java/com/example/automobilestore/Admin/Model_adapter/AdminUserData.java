package com.example.automobilestore.Admin.Model_adapter;

public class AdminUserData {
    private String Name;

    public String getName() {
        return Name;
    }

    public AdminUserData(String name, String email, String phone) {
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
