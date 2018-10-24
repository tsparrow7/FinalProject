package com.example.tjgaming.finalproject.Model.TVMaze;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/15/2018.
 */
public class TVMazeResult {
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("name")
    private String name;
    @SerializedName("season")
    private int season;
    @SerializedName("number")
    private int number;
    @SerializedName("show")
    private TVMazeShow show;


    public TVMazeShow getShow() {
        return show;
    }

    public void setShow(TVMazeShow show) {
        this.show = show;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
