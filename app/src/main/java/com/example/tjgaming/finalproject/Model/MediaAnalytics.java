package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 12/4/2018.
 */
public class MediaAnalytics {

    private String title;
    private Double avgRating;
    private int timesRated;
    private int numOfMale;
    private int numOfFemale;
    private Double avgAge;

    public MediaAnalytics() {
    }

    public MediaAnalytics(String title, Double avgRating, int numOfMale, int numOfFemale, Double avgAge, int timesRated) {
        this.title = title;
        this.avgRating = avgRating;
        this.numOfMale = numOfMale;
        this.numOfFemale = numOfFemale;
        this.avgAge = avgAge;
        this.timesRated = timesRated;
    }

    public int getTimesRated() {
        return timesRated;
    }

    public void setTimesRated(int timesRated) {
        this.timesRated = timesRated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public int getNumOfMale() {
        return numOfMale;
    }

    public void setNumOfMale(int numOfMale) {
        this.numOfMale = numOfMale;
    }

    public int getNumOfFemale() {
        return numOfFemale;
    }

    public void setNumOfFemale(int numOfFemale) {
        this.numOfFemale = numOfFemale;
    }

    public Double getAvgAge() {
        return avgAge;
    }

    public void setAvgAge(Double avgAge) {
        this.avgAge = avgAge;
    }
}
