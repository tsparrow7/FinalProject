package com.example.tjgaming.finalproject.Model.TVMaze;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/23/2018.
 */
public class TVMazeShowSchedule {
    @SerializedName("time")
    private String time;
    @SerializedName("days")
    private String[] days;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }
}
