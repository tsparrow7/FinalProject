package com.example.tjgaming.finalproject.Model.TVMaze;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/24/2018.
 */
public class TVMazeShowRating {
    @SerializedName("average")
    private double average;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
