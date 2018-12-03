package com.example.tjgaming.finalproject.View.Home.Movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Model.TheMovieDB.TMDBMovie;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.Utils.CustomStrings;
import com.example.tjgaming.finalproject.Utils.GetMoviesTask;
import com.example.tjgaming.finalproject.Utils.NetworkChecker;
import com.example.tjgaming.finalproject.View.Home.MediaFeed.OnFragmentVisibleListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    public MoviesFragment() {
    }

    OnFragmentVisibleListener mListener;
    boolean isAttached;

    //List for Storing movie objects
    private ArrayList<String> movieIdList = new ArrayList<>();
    private ArrayList<String> posterUrlList = new ArrayList<>();
    private ArrayList<TMDBMovie> tmdbMoviesList = new ArrayList<>();

    //data pulling from API to create movie objects
    private String movieId = null;
    private Double movieRating = null;
    private String movieTitle = null;
    private String poster = null;
    private List<Integer> genresList = new ArrayList<>();

    RecyclerView mRecyclerView;
    MoviesAdapter mMovieAdapter;
    DocumentReference documentReference;
    CollectionReference mCollectionRef;
    FirebaseFirestore mFirestore;
    ProgressDialog progressDialog;

    //For sorting data from dB
    private String FIELD;
    private String VALUE;
    private String ORDERING;
    private Query.Direction DIRECTION;
    private String searchedItem;


    //JSON data for getting movies from the API
    final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    final String BASE_JSON_REQUEST = "api.themoviedb.org";
    final String JSON_REQUEST_PARAM = "3";
    final String MOVIE_JSON_REQUEST = "movie";
    final String API_KEY_PARAM = "api_key";
    final String POSTER_SIZE_PARAM = "w500";
    final String POPULAR_MOVIES_PARAM = "popular";

    private GridView mGridView;
    private String mKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        mKey = getString(R.string.api_key);
        Bundle bundle = getArguments();
        if (bundle != null && !bundle.containsKey("searchedItem")) {
            FIELD = bundle.getString("Field");
            VALUE = bundle.getString("Value");
            ORDERING = bundle.getString("Order");
            String STR_DIRECTION = bundle.getString("Direction");

            if (STR_DIRECTION != null) {
                if (STR_DIRECTION.equals("Ascending")) {
                    DIRECTION = Query.Direction.ASCENDING;
                } else {
                    DIRECTION = Query.Direction.DESCENDING;
                }
            }

            Log.i("MoviesFragment","Field: " + FIELD + "Value: " + VALUE + "Ordering: " + ORDERING);
        } else {
            try {
                searchedItem = bundle.getString("searchedItem");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        mRecyclerView = v.findViewById(R.id._movie_media_feed_recycler);

        initAdapter();
        getMovieDataFromApi();
        return v;
    }

    private void initAdapter() {
        mMovieAdapter = new MoviesAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mMovieAdapter);

    }


    public void getMovieDataFromApi(){
        startProgress();
        String movieData = "";

        try {
            if(NetworkChecker.isNetworkActive(getActivity())) {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority(BASE_JSON_REQUEST)
                        .appendPath(JSON_REQUEST_PARAM)
                        .appendPath(MOVIE_JSON_REQUEST)
                        .appendPath(POPULAR_MOVIES_PARAM)
                        .appendQueryParameter(API_KEY_PARAM, mKey);
                String myUrl = builder.build().toString();

                Log.i("MoviesFragment","URL to call: " + myUrl);


                GetMoviesTask movieTask = new GetMoviesTask();
                movieData = movieTask.execute(myUrl).get();
            }
            if (movieData != null) {

                JSONObject moviesObject = new JSONObject(movieData);
                JSONArray moviesArray = moviesObject.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {

                    JSONObject jsonObject = moviesArray.getJSONObject(i);

                    //Creating a movie to save in database after API call finishes
                    movieId = jsonObject.getString("id");
                    poster = String.format("%s%s%s", BASE_POSTER_URL, POSTER_SIZE_PARAM, jsonObject.getString("poster_path"));
                    movieTitle = jsonObject.getString("title");
                    movieRating = jsonObject.getDouble("vote_average");
                    //Genre ids list
                    JSONArray jsonArray = jsonObject.getJSONArray("genre_ids");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        genresList.add(jsonArray.getInt(j));
                    }

                    TMDBMovie movie = new TMDBMovie(movieId, poster, movieTitle, movieRating, genresList);

                    tmdbMoviesList.add(movie);

                    movieIdList.add(jsonObject.getString("id"));
                    String poster_path = (jsonObject.getString("poster_path"));
                    String poster_url = BASE_POSTER_URL + POSTER_SIZE_PARAM + poster_path;
                    posterUrlList.add(poster_url);
                }

                for (int i = 0; i < tmdbMoviesList.size(); i++) {
                    documentReference = FirebaseFirestore.getInstance()
                            .collection("Movies")
                            .document(tmdbMoviesList.get(i).getMovieTitle());

                    documentReference.set(tmdbMoviesList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Saving media to db", "Movie saved");
                            } else {
                                Log.d("Saving media to db", "Movie not saved");
                            }
                        }
                    });

                }
                tmdbMoviesList.clear();
                mFirestore = FirebaseFirestore.getInstance();
                mCollectionRef = mFirestore.collection("Movies");

                if (searchedItem != null) {
                    mCollectionRef
                            .whereEqualTo(CustomStrings.MOVIE_TITLE, searchedItem)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                        TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                                        tmdbMoviesList.add(tmdbMovie);
                                    }
                                    stopProgress();
                                    mMovieAdapter.setData(tmdbMoviesList);
                                }
                            });
                } else {
                    if (FIELD != null) {
                        if (FIELD.equals(CustomStrings.SHOW_GENRES) && VALUE != null) {

                            if (ORDERING != null) {
                                mCollectionRef
                                        .whereArrayContains(FIELD, VALUE)
                                        .orderBy(ORDERING, DIRECTION)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                                                    tmdbMoviesList.add(tmdbMovie);
                                                }
                                                stopProgress();
                                                mMovieAdapter.setData(tmdbMoviesList);
                                            }
                                        });
                            } else {
                                mCollectionRef
                                        .whereArrayContains(FIELD, VALUE)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                                                    tmdbMoviesList.add(tmdbMovie);
                                                }
                                                stopProgress();
                                                mMovieAdapter.setData(tmdbMoviesList);
                                            }
                                        });
                            }
                        }
                    }
                    //only sorting / ordering
                    else if (ORDERING != null) {
                        mCollectionRef
                                .orderBy(ORDERING, DIRECTION)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                            TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                                            tmdbMoviesList.add(tmdbMovie);
                                        }
                                        stopProgress();
                                        mMovieAdapter.setData(tmdbMoviesList);
                                    }
                                });
                    }
                    //default call, no filtering or sorting. Called when fragment first attached
                    else {
                        mCollectionRef
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                            TMDBMovie tmdbMovie = queryDocumentSnapshot.toObject(TMDBMovie.class);

                                            tmdbMoviesList.add(tmdbMovie);
                                        }
                                        stopProgress();
                                        mMovieAdapter.setData(tmdbMoviesList);
                                    }
                                });
                    }
                }
            }
            else{
                Toast.makeText(getActivity(), "Network currently not available", Toast.LENGTH_LONG)
                        .show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startProgress() {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void stopProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (OnFragmentVisibleListener) getActivity();
            isAttached = true; //flag for whether this fragment is attached to pager
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentVisibleListener");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isAttached && isVisibleToUser) {
            mListener.fragmentVisible(true, CustomStrings.MOVIE_FRAGMENT);
        }
    }
}
