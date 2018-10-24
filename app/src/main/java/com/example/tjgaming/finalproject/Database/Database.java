package com.example.tjgaming.finalproject.Database;

import android.content.Context;

import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by TJ on 10/24/2018.
 */
public class Database {

    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private DocumentReference mDocumentReference;


    public Database(Context context) {
        mContext = context;
    }

    public void addToFavorites(FavoriteShow favoriteShow) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("favorites")
                .document(getUserLoggedIn().getUid());

    }


    public void login(String email, String password) {

    }

    public void logout() {

    }

    public FirebaseUser getUserLoggedIn() {
        return mFirebaseAuth.getCurrentUser();
    }

}
