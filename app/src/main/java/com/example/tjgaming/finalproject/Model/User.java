package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 10/5/2018.
 */

public class User {
    private String userName;
    private String email;


    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
