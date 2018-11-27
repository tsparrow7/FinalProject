package com.example.tjgaming.finalproject.Model;


import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchModel implements Searchable {
    private String mTitle;

    public SearchModel (String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}

