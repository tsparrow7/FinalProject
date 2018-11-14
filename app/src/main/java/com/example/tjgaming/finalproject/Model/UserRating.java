package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 11/9/2018.
 */
public class UserRating {

    private String show_name;
    private String user_id;
    private Double user_rating;

    public UserRating() {

    }

    public UserRating(String show_name, String user_id, Double user_rating) {
        this.show_name = show_name;
        this.user_id = user_id;
        this.user_rating = user_rating;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(Double user_rating) {
        this.user_rating = user_rating;
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "show_name='" + show_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_rating='" + user_rating + '\'' +
                '}';
    }
}
