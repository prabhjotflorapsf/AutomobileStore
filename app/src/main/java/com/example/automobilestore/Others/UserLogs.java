package com.example.automobilestore.Others;

public class UserLogs {
    private String email;
    private String name;
    private long phone;

    public UserLogs()
    { }

    public UserLogs(String email, String name, long phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getPhone() {
        return phone;
    }
}
