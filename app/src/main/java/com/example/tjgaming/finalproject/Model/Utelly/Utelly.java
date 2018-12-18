package com.example.tjgaming.finalproject.Model.Utelly;

import com.google.gson.annotations.SerializedName;

public class Utelly {
    @SerializedName("display_name")
    private String display_name;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("id")
    private String id;
    @SerializedName("icon")
    private String icon;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
