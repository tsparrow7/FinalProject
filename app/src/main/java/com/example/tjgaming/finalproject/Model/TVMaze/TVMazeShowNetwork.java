package com.example.tjgaming.finalproject.Model.TVMaze;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/23/2018.
 */
public class TVMazeShowNetwork {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
