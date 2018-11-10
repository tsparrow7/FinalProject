package com.example.tjgaming.finalproject.Database;

import com.example.tjgaming.finalproject.Model.FavoriteShow;

import java.util.List;

public interface DBWatcher {
    void onFavorite(List<FavoriteShow> list);

    void onRating(int rating);
}
