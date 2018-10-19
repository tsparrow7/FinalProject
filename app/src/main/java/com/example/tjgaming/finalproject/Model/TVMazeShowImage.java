package com.example.tjgaming.finalproject.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/19/2018.
 */
public class TVMazeShowImage {
    @SerializedName("medium")
    private String medium;
    @SerializedName("original")
    private String original;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
