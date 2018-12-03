package com.example.tjgaming.finalproject.View.Home.Favorites.Reviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tjgaming.finalproject.Database.DBWatcher;
import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.Favorite;
import com.example.tjgaming.finalproject.Model.UserReview;
import com.example.tjgaming.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements DBWatcher {

    TextView myUser;
    TextView allUsers;
    Database database;

    String myAuthor = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        myUser = findViewById(R.id.user_review_text_view);
        allUsers = findViewById(R.id.reviews_text_view);

        String showName = getIntent().getExtras().getString("showName");
        database = new Database(this);
        database.setWatcher(this);

        //TODO: add method to retrieve username from database and add to DBWatcher when it finishes call these two methods.

        database.getReviewObject(showName);
        database.getListOfReviews(showName);
    }

    @Override
    public void onFavoriteDeleted(List<Favorite> list) {

    }

    @Override
    public void onRating(int rating) {

    }

    @Override
    public void onReviewSaved(String review) {

    }

    @Override
    public void onReviewReceived(String review) {

    }

    @Override
    public void onListReceived(ArrayList<String> list) {

    }

    @Override
    public void onReviewObjectReceived(UserReview userReview) {

        myAuthor = userReview.getAuthor();

        myUser.setText(String.format("%s \nby: %s", userReview.getUser_review(), userReview.getAuthor()));

    }

    @Override
    public void onReviewListReceived(ArrayList<UserReview> list) {

        for (int i = 0; i < list.size(); i++) {

            if (!list.get(i).getAuthor().equals(myAuthor)) {
                allUsers.append(list.get(i).getUser_review() + " \nby: " + list.get(i).getAuthor() + "\n\n");
            }
        }
    }
}
