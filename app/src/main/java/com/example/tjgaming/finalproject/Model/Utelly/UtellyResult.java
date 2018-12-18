package com.example.tjgaming.finalproject.Model.Utelly;

import com.google.gson.annotations.SerializedName;

public class UtellyResult {
    @SerializedName("picture")
    private String picture;
    @SerializedName("name")
    private String name;
    @SerializedName("locations")
    private Utelly locations;



    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public Utelly getLocations(){
        return locations;
    }
    public void setLocations(Utelly locations) {
        this.locations = locations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
