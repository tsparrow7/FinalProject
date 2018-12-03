package com.example.tjgaming.finalproject.Model;

import java.util.List;

/**
 * Created by TJ on 10/24/2018.
 */
public class FavoriteShow {
    private String show_name;
    private List<String> days;
    private String times;
    private String network;
    private double rating;
    private String typeOfMedia;

    public FavoriteShow() {

    }

    public FavoriteShow(String show_name, List<String> days, String times, String network, double rating) {
        this.show_name = show_name;
        this.days = days;
        this.times = times;
        this.network = network;
        this.rating = rating;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTypeOfMedia() {
        return typeOfMedia;
    }

    public void setTypeOfMedia(String typeOfMedia) {
        this.typeOfMedia = typeOfMedia;
    }

    @Override
    public String toString() {
        return "FavoriteShow{" +
                "show_name='" + show_name + '\'' +
                ", days=" + days +
                ", times='" + times + '\'' +
                ", network='" + network + '\'' +
                ", rating=" + rating +
                '}';
    }
}
