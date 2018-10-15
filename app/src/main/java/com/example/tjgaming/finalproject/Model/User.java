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
}