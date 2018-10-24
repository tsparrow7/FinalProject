package com.example.tjgaming.finalproject.Model.TVMaze;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TJ on 10/15/2018.
 */
public class TVMazeShow {
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("language")
    private String language;
    @SerializedName("image")
    private TVMazeShowImage image;
    @SerializedName("network")
    private TVMazeShowNetwork network;
    @SerializedName("schedule")
    private TVMazeShowSchedule schedule;
    @SerializedName("rating")
    private TVMazeShowRating rating;

    public TVMazeShowImage getImage() {
        return image;
    }

    public void setImage(TVMazeShowImage image) {
        this.image = image;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public TVMazeShowNetwork getNetwork() {
        return network;
    }

    public void setNetwork(TVMazeShowNetwork network) {
        this.network = network;
    }

    public TVMazeShowSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(TVMazeShowSchedule schedule) {
        this.schedule = schedule;
    }

    public TVMazeShowRating getRating() {
        return rating;
    }

    public void setRating(TVMazeShowRating rating) {
        this.rating = rating;
    }
}
