package com.example.tjgaming.finalproject.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tjgaming.finalproject.Model.Favorite;
import com.example.tjgaming.finalproject.Model.FavoriteAnalytics;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.Model.MediaAnalytics;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.Model.TheMovieDB.TMDBMovie;
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
import java.util.Calendar;
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
    private List<Favorite> mFavoritesList;
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

    public void addFavorite(final Favorite favorite) {
        //When we add a favorite we want to add it to the users favorite list and
        //update the FavAnalytics table for the amount of times favorite'd.


        //ADD TO FAVORITES LIST
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Favorites")
                .document(getUserLoggedIn().getUid())
                .collection(getUserLoggedIn().getUid() + "-Favorites")
                .document(favorite.getTitle());

        Map<String, Object> favoriteItem = new HashMap<>();
        favoriteItem.put("title", favorite.getTitle());
        favoriteItem.put("rating", favorite.getRating());
        favoriteItem.put("type", favorite.getTypeOfMedia());

        mDocumentReference.set(favoriteItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Document saved!");
                } else {
                    Log.d(TAG, "Document not saved", task.getException());
                }
            }
        });

        //UPDATE FAVANALYTICS TABLE
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("FavAnalytics")
                .document(favorite.getTitle());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    FavoriteAnalytics analytics = task.getResult().toObject(FavoriteAnalytics.class);
                    int timesFavorited;
                    try {
                        timesFavorited = analytics.getTimesFavorited();
                    } catch (NullPointerException e) {
                        timesFavorited = 0;
                    }

                    if (timesFavorited == 0) {
                        Map<String, Object> favoriteAnalytics = new HashMap<>();
                        favoriteAnalytics.put("title", favorite.getTitle());
                        favoriteAnalytics.put("timesFavorited",1);
                        mDocumentReference.set(favoriteAnalytics).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Favorite Analytics saved!");
                                } else {
                                    Log.d(TAG, "Favorite Analytics not saved ", task.getException());
                                }
                            }
                        });
                    } else {
                        timesFavorited = timesFavorited + 1;
                        analytics.setTimesFavorited(timesFavorited);
                        mDocumentReference.set(analytics).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Favorite Analytics saved!");
                                } else {
                                    Log.d(TAG, "Favorite Analytics not saved ", task.getException());
                                }
                            }
                        });
                    }
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

    public void getAllMoviesForSearch() {
        mList = new ArrayList<>();

        mCollectionReference = FirebaseFirestore.getInstance()
                .collection("Movies");

        mCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                    try {
                        mList.add(tmdbMovie.getMovieTitle());
                    } catch (NullPointerException e){
                        //do not add show to list if null exception occurs
                        Log.e(TAG,e.getMessage());
                    }
                }
                notifySearchResultsRetrieval(mList);
            }
        });
    }

    public void addUserRating(final String showName, float userRating) {
        //Round the user_rating to one decimal place
        BigDecimal bd = new BigDecimal(userRating);
        bd = bd.setScale(2,BigDecimal.ROUND_UP);
        final float userRatingBd = bd.floatValue();

        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Ratings")
                .document(showName)
                .collection(showName + "-ratings")
                .document(getUserLoggedIn().getUid());

        Map<String, Object> rating = new HashMap<>();
        rating.put("show_name", showName);
        rating.put("user_id", getUserLoggedIn().getUid());
        rating.put("user_rating", userRatingBd);

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

        //getUserData
        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("users")
                .document(getUserLoggedIn().getUid());

        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);

                    //Get the age of the user
                    String str = user.getBirthdate();
                    String last4 = str == null || str.length() < 4 ?
                            str : str.substring(str.length() - 4);
                    int year = Integer.valueOf(last4);
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    final int age = currentYear - year;
                    //Get the gender of the current user
                    final String gender = user.getGender();

                    //Get the reference to Analytics
                    mDocumentReference = FirebaseFirestore.getInstance()
                            .collection("MediaAnalytics")
                            .document(showName);

                    mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                MediaAnalytics analytics = task.getResult().toObject(MediaAnalytics.class);
                                int female, male, ratings;
                                double avgRating, avgAge;

                                try {
                                    female = analytics.getNumOfFemale();
                                    Log.i(TAG, "Female: " + analytics.getNumOfFemale());
                                } catch (NullPointerException e) {
                                    female = 0;
                                }

                                try {
                                    male = analytics.getNumOfMale();
                                    Log.i(TAG, "Male: " + analytics.getNumOfMale());
                                } catch (NullPointerException e) {
                                    male = 0;
                                }

                                try {
                                    ratings = analytics.getTimesRated();
                                    Log.i(TAG, "Times Rated: " + analytics.getTimesRated());
                                } catch (NullPointerException e) {
                                    ratings = 0;
                                }

                                try {
                                    avgRating = analytics.getAvgRating();
                                    Log.i(TAG, "avgRating: " + analytics.getAvgRating());
                                } catch (NullPointerException e) {
                                    avgRating = 0;
                                }

                                try {
                                    avgAge = analytics.getAvgAge();
                                    Log.i(TAG, "avgAge: " + analytics.getAvgAge());
                                } catch (NullPointerException e) {
                                    avgAge = 0;
                                }

                                //If this is the first time this title has been rated
                                if (female == 0 && male == 0 && ratings == 0 && avgRating == 0 && avgAge == 0){
                                    Map<String, Object> mediaAnalytics = new HashMap<>();
                                    mediaAnalytics.put("title", showName);
                                    mediaAnalytics.put("avgRating",userRatingBd);
                                    if (gender.equals("Male")) {
                                        mediaAnalytics.put("numOfMale", 1);
                                        mediaAnalytics.put("numOfFemale", 0);
                                    } else if (gender.equals("Female")) {
                                        mediaAnalytics.put("numOfFemale", 1);
                                        mediaAnalytics.put("numOfMale", 0);
                                    } else {
                                        mediaAnalytics.put("numOfFemale", 0);
                                        mediaAnalytics.put("numOfMale", 0);
                                    }
                                    mediaAnalytics.put("avgAge", age);
                                    mediaAnalytics.put("timesRated", 1);
                                    mDocumentReference.set(mediaAnalytics).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Media Analytics saved!");
                                            } else {
                                                Log.d(TAG, "Media Analytics not saved ", task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Log.i(TAG,analytics.toString() + "In else");
                                    analytics.setTimesRated(ratings + 1);
                                    analytics.setAvgAge(((avgAge * (ratings-1)) + age)/ratings);
                                    analytics.setAvgRating(((avgRating * (ratings-1)) + userRatingBd)/ratings);
                                    if (gender.equals("Male")) {
                                        analytics.setNumOfMale(male + 1);
                                    } else if (gender.equals("Female")){
                                        analytics.setNumOfFemale(female + 1);
                                    }
                                    mDocumentReference.set(analytics).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Media Analytics updated!");
                                            } else {
                                                Log.d(TAG, "Media Analytics not updated ", task.getException());
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "Analytics not retrieved", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "User data not retrieved", task.getException());
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

    public void deleteFavorite(String title, List<Favorite> list) {
        mFavoritesList = list;

        for (int i = 0; i < mFavoritesList.size(); i++) {
            if (mFavoritesList.get(i).getTitle().equals(title)) {
                mFavoritesList.remove(i);
            }
        }

        mDocumentReference = FirebaseFirestore.getInstance()
                .collection("Favorites")
                .document(getUserLoggedIn().getUid())
                .collection(getUserLoggedIn().getUid() + "-Favorites")
                .document(title);
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

    private void notifyFavoriteChange(List<Favorite> list) {
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

