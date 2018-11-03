package com.example.tjgaming.finalproject.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TJ on 10/24/2018.
 */
public class Database {

    private final static String TAG = "Database.class";

    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private DocumentReference mDocumentReference;


    public Database(Context context) {
        mContext = context;
    }

    public void addToFavorites(FavoriteShow favoriteShow) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("favorites")
                .document(getUserLoggedIn().getUid())
                .collection(getUserLoggedIn().getUid() + "-favorites")
                .document(favoriteShow.getmShowName());

        Map<String, Object> favorite = new HashMap<>();
        favorite.put("show_name",favoriteShow.getmShowName());
        favorite.put("network",favoriteShow.getmNetwork());
        favorite.put("days",favoriteShow.getmDays());
        favorite.put("times",favoriteShow.getmTime());
        favorite.put("rating",favoriteShow.getmRating());

        mDocumentReference.set(favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Document saved!");
                } else {
                    Log.d(TAG,"Document not saved", task.getException());
                }
            }
        });

    }


    public void login(String email, String password) {

    }

    public void logout() {

    }

    public FirebaseUser getUserLoggedIn() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        return mFirebaseAuth.getCurrentUser();
    }

    public void addListToDB() {

    }

}
