package com.example.tjgaming.finalproject.Model;

import java.lang.reflect.Array;

/**
 * Created by TJ on 10/5/2018.
 */

public class User {
    private String email;
    private String gender;
    private String dateOfBirth;
    private String userName;
    private Array[] mediaSelection;


    public User(String email, String gender, String dateOfBirth, String userName, Array[] mediaSelection) {
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.userName = userName;
        this.mediaSelection = mediaSelection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Array[] getMediaSelection() {
        return mediaSelection;
    }

    public void setMediaSelection(Array[] mediaSelection) {
        this.mediaSelection = mediaSelection;
    }
}