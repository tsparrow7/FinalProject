package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 12/3/2018.
 */
public class FavoriteAnalytics {

    private String title;
    private int timesFavorited;

    public FavoriteAnalytics() {
    }

    public FavoriteAnalytics(String title, int timesFavorited) {
        this.title = title;
        this.timesFavorited = timesFavorited;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimesFavorited() {
        return timesFavorited;
    }

    public void setTimesFavorited(int timesFavorited) {
        this.timesFavorited = timesFavorited;
    }
}
