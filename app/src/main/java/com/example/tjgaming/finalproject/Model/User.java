package com.example.tjgaming.finalproject.Model;

import java.util.List;

/**
 * Created by TJ on 10/5/2018.
 */

public class User {
    private String email;
    private String gender;
    private String birthdate;
    private String username;
    private List<Boolean> mediaTypes;
    private String typeOfUser;


    public User() {

    }

    public User(String email, String gender, String birthdate, String username, List<Boolean> mediaTypes, String typeOfUser) {
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
        this.username = username;
        this.mediaTypes = mediaTypes;
        this.typeOfUser = typeOfUser;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<Boolean> getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(List<Boolean> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", username='" + username + '\'' +
                ", mediaTypes=" + mediaTypes +
                ", typeOfUser='" + typeOfUser + '\'' +
                '}';
    }
}