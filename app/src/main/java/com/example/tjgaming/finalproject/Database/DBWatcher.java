package com.example.tjgaming.finalproject.Database;

import com.example.tjgaming.finalproject.Model.Favorite;
import com.example.tjgaming.finalproject.Model.UserReview;

import java.util.ArrayList;
import java.util.List;

public interface DBWatcher {
    void onFavoriteDeleted(List<Favorite> list);

    void onRating(int rating);

    void onReviewSaved(String review);

    void onReviewReceived(String review);

    void onListReceived(ArrayList<String> list);

    void onReviewObjectReceived(UserReview userReview);

    void onReviewListReceived(ArrayList<UserReview> list);
}
