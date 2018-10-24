package com.example.tjgaming.finalproject.Model;

/**
 * Created by TJ on 10/24/2018.
 */
public class FavoriteShow {
    private String mShowName;
    private String[] mDays;
    private String mTime;
    private String mNetwork;
    private double mRating;

    public FavoriteShow(String mShowName, String[] mDays, String mTime, String mNetwork, double mRating) {
        this.mShowName = mShowName;
        this.mDays = mDays;
        this.mTime = mTime;
        this.mNetwork = mNetwork;
        this.mRating = mRating;
    }

    public String getmShowName() {
        return mShowName;
    }

    public void setmShowName(String mShowName) {
        this.mShowName = mShowName;
    }

    public String[] getmDays() {
        return mDays;
    }

    public void setmDays(String[] mDays) {
        this.mDays = mDays;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmNetwork() {
        return mNetwork;
    }

    public void setmNetwork(String mNetwork) {
        this.mNetwork = mNetwork;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }
}
