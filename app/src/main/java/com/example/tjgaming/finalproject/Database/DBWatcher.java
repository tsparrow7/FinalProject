package com.example.tjgaming.finalproject.Database;

import com.example.tjgaming.finalproject.Model.FavoriteShow;

import java.util.ArrayList;
import java.util.List;

public interface DBWatcher {
    void onFavoriteDeleted(List<FavoriteShow> list);

    void onRating(int rating);

    void onReviewSaved(String review);

    void onReviewReceived(String review);

    void onListReceived(ArrayList<String> list);
}
