package com.example.tjgaming.finalproject.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.Model.UserRating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TJ on 10/24/2018.
 */
public class Database {

    private final static String TAG = "Database.class";

    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private DocumentReference mDocumentReference;
    private DBWatcher watcher = null;
    private List<FavoriteShow> mFavoritesList;


    public Database(Context context) {
        mContext = context;
    }

    public void addToFavorites(FavoriteShow favoriteShow) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("favorites")
                .document(getUserLoggedIn().getUid())
                .collection(getUserLoggedIn().getUid() + "-favorites")
                .document(favoriteShow.getShow_name());

        Map<String, Object> favorite = new HashMap<>();
        favorite.put("show_name", favoriteShow.getShow_name());
        favorite.put("network", favoriteShow.getNetwork());
        favorite.put("days", favoriteShow.getDays());
        favorite.put("times", favoriteShow.getTimes());
        favorite.put("rating", favoriteShow.getRating());

        mDocumentReference.set(favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Document saved!");
                } else {
                    Log.d(TAG, "Document not saved", task.getException());
                }
            }
        });
    }

    public FirebaseUser getUserLoggedIn() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        return mFirebaseAuth.getCurrentUser();
    }

    public void addUserRating(String showName, float userRating) {
        //Round the user_rating to one decimal place
        BigDecimal bd = new BigDecimal(userRating);
        bd = bd.setScale(2,BigDecimal.ROUND_UP);
        userRating = bd.floatValue();

        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Ratings")
                .document(showName)
                .collection(showName + "-ratings")
                .document(getUserLoggedIn().getUid());

        Map<String, Object> rating = new HashMap<>();
        rating.put("show_name", showName);
        rating.put("user_id", getUserLoggedIn().getUid());
        rating.put("user_rating", userRating);

        mDocumentReference.set(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Rating saved!");
                } else {
                    Log.d(TAG, "Rating not saved", task.getException());
                }
            }
        });
    }

    public void getUserRating(String showName) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Ratings")
                .document(showName)
                .collection(showName + "-ratings")
                .document(getUserLoggedIn().getUid());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserRating rating = task.getResult().toObject(UserRating.class);
                    Double user_rating;
                    try {
                        user_rating = rating.getUser_rating() * 10;
                    } catch (NullPointerException e) {
                        user_rating = 0.0;
                    }
                    notifyRatingChange(user_rating.intValue());
//                    EventBus.getDefault().post(new RatingEvent(user_rating.intValue()));
//                    Log.i("onRating" + TAG,user_rating.intValue() + "");
                }
            }
        });
    }

    public void deleteFavorite(String showName, List<FavoriteShow> list) {
        mFavoritesList = list;

        for (int i = 0; i < mFavoritesList.size(); i++) {
            if (mFavoritesList.get(i).getShow_name().equals(showName)) {
                mFavoritesList.remove(i);
            }
        }

        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("favorites")
                .document(getUserLoggedIn().getUid())
                .collection(getUserLoggedIn().getUid() + "-favorites")
                .document(showName);
        mDocumentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Favorite deleted!");
                    notifyFavoriteChange(mFavoritesList);
                } else {
                    Log.d(TAG, "Favorite not deleted", task.getException());
                }
            }
        });
    }
    public void setWatcher(DBWatcher watcher) {
        this.watcher = watcher;
    }

    public void notifyRatingChange(int rating) {
        if (watcher != null){
            watcher.onRating(rating);
        }
    }

    private void notifyFavoriteChange(List<FavoriteShow> list) {
        if (watcher != null) {
            watcher.onFavorite(list);
        }
    }
}

