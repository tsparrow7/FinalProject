package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 12/3/2018.
 */
public class Favorite {

    private String title;
    private Double rating;
    private Double user_rating;
    private String typeOfMedia;

    public Favorite() {
    }

    public Favorite(String title, Double rating) {
        this.title = title;
        this.rating = rating;
    }

    public void setTypeOfMedia(String typeOfMedia) {
        this.typeOfMedia = typeOfMedia;
    }

    public String getTypeOfMedia() {
        return typeOfMedia;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setUser_rating(Double user_rating) {
        this.user_rating = user_rating;
    }

    public Double getUser_rating() {
        return user_rating;
    }
}
