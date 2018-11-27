package com.example.tjgaming.finalproject.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.Model.UserRating;
import com.example.tjgaming.finalproject.Model.UserReview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private CollectionReference mCollectionReference;
    private DBWatcher watcher = null;
    private List<FavoriteShow> mFavoritesList;
    private ArrayList<String> mList;
    private ArrayList<UserReview> mUserReviewList;


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

    public void getAllShowsForSearch() {
        mList = new ArrayList<>();

        mCollectionReference = FirebaseFirestore.getInstance()
                .collection("TV Shows");

        mCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                    try {
                        mList.add(tvMazeResult.getShow().getName());
                    } catch (NullPointerException e){
                        //do not add show to list if null exception occurs
                        Log.e(TAG,e.getMessage());
                    }
                }
                notifySearchResultsRetrieval(mList);
            }
        });
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

    public void getReview(final String showName) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Reviews")
                .document(showName)
                .collection(showName + "-reviews")
                .document(getUserLoggedIn().getUid());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        notifyReviewRetrieval(task.getResult().toObject(UserReview.class).getUser_review());
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                        //no review written send default value back to adapter
                        notifyReviewRetrieval("Your review here....");
                    }
                }
            }
        });
    }

    public void getReviewObject(final String showName) {
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Reviews")
                .document(showName)
                .collection(showName + "-reviews")
                .document(getUserLoggedIn().getUid());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        notifyReviewObjectRetrieval(task.getResult().toObject(UserReview.class));
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                        //no review written send default value back
                        notifyReviewObjectRetrieval(new UserReview("N/A","default","No review written..."));
                    }
                }
            }
        });
    }

    public void getListOfReviews(final String showName) {
        mUserReviewList = new ArrayList<>();

        mCollectionReference = FirebaseFirestore.getInstance()
                .collection("Reviews")
                .document(showName)
                .collection(showName + "-reviews");

        mCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    UserReview userReview = queryDocumentSnapshot.toObject(UserReview.class);

                    mUserReviewList.add(userReview);
                }
                notifyReviewListRetrieval(mUserReviewList);
            }
        });
    }

    public void addReview(final String showName, final String review) {
        //First get userName to set as author of review:
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("users")
                .document(getUserLoggedIn().getUid());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final String userName = (task.getResult().toObject(User.class)).getUsername();

                    //Then save the review in the database.
                    mDocumentReference = FirebaseFirestore.getInstance()
                            .collection("Reviews")
                            .document(showName)
                            .collection(showName + "-reviews")
                            .document(getUserLoggedIn().getUid());

                    Map<String, Object> userReview = new HashMap<>();
                    userReview.put("author", userName);
                    userReview.put("show_name", showName);
                    userReview.put("user_review", review);

                    mDocumentReference.set(userReview).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Notify the watcher that the review has been successfully added
                                Log.i(TAG,"review saved");
                                notifyReviewChange(review);
                            }
                        }
                    });
                }
            }
        });
    }

    public void setWatcher(DBWatcher watcher) {
        this.watcher = watcher;
    }

    private void notifyRatingChange(int rating) {
        if (watcher != null){
            watcher.onRating(rating);
        }
    }

    private void notifyFavoriteChange(List<FavoriteShow> list) {
        if (watcher != null){
            watcher.onFavoriteDeleted(list);
        }
    }

    private void notifyReviewChange(String review) {
        if (watcher != null){
            watcher.onReviewSaved(review);
        }
    }

    private void notifyReviewRetrieval(String review) {
        if (watcher != null){
            watcher.onReviewReceived(review);
        }
    }

    private void notifySearchResultsRetrieval(ArrayList<String> list) {
        if (watcher != null){
            watcher.onListReceived(list);
        }
    }

    private void notifyReviewObjectRetrieval(UserReview userReview) {
        if (watcher != null){
            watcher.onReviewObjectReceived(userReview);
        }
    }

    private void notifyReviewListRetrieval(ArrayList<UserReview> userReviewArrayList) {
        if (watcher != null) {
            watcher.onReviewListReceived(userReviewArrayList);
        }
    }
}

