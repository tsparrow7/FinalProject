package com.example.tjgaming.finalproject.Model;

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
    private String season;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
