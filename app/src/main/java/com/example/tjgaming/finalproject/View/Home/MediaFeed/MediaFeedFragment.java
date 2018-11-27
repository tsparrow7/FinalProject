package com.example.tjgaming.finalproject.View.Home.MediaFeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjgaming.finalproject.Model.CustomStrings;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 10/5/2018.
 */

public class MediaFeedFragment extends Fragment {

    RecyclerView mRecyclerView;
    MediaFeedAdapter mAdapter;
    List<TVMazeResult> mList = new ArrayList<>();
    ProgressDialog progressDialog;

    OnListCreatedListener mListener;

    private String FIELD;
    private String VALUE;
    private String ORDERING;
    private Query.Direction DIRECTION;
    private String searchedItem;

    DocumentReference documentReference;
    CollectionReference mCollectionRef;
    FirebaseFirestore mFirestore;
    FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_feed, container, false);

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
        } else {
            try {
                searchedItem = bundle.getString("searchedItem");
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        mRecyclerView = root.findViewById(R.id.media_feed_recycler);

        initAdapter();
        tvMazeRequest();
        return root;
    }

    private void sendDataBack() {
        mListener.onListCreated(mList);
    }

    private void initAdapter() {
        mAdapter = new MediaFeedAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

//        for (int i = 0; i < 10; i++) {
//            mList.add("Test " + i);
//        }
    }

    public void tvMazeRequest() {

        startProgress();

        //API call to fill the database with show data.
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//
//        Call<List<TVMazeResult>> call = apiInterface.getTvMazeResults();
//
//        call.enqueue(new Callback<List<TVMazeResult>>() {
//            @Override
//            public void onResponse(Call<List<TVMazeResult>> call, Response<List<TVMazeResult>> response) {
//                mList = response.body();
//
//                for (int i = 0; i < mList.size(); i++) {
//                    documentReference = FirebaseFirestore.getInstance()
//                            .collection("TV Shows")
//                            .document(mList.get(i).getShow().getName());
//
//                    documentReference.set(mList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("Saving media to db","TV Show saved");
//                            } else {
//                                Log.d("Saving media to db","TV Show not saved");
//                            }
//                        }
//                    });
//                }

                //Retrieve data from database and set to the data in the adapter.
                mList.clear();
                mFirestore = FirebaseFirestore.getInstance();
                mCollectionRef = mFirestore.collection("TV Shows");

                if (searchedItem != null) {
                    mCollectionRef
                            .whereEqualTo(CustomStrings.SHOW_NAME, searchedItem)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                        TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                        mList.add(tvMazeResult);
                                    }
                                    stopProgress();
                                    mAdapter.setData(mList);
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
                                                    TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                                    mList.add(tvMazeResult);
                                                }
                                                stopProgress();
                                                mAdapter.setData(mList);
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
                                                    TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                                    mList.add(tvMazeResult);
                                                }
                                                stopProgress();
                                                mAdapter.setData(mList);
                                            }
                                        });
                            }
                        } else if (FIELD.equals(CustomStrings.SHOW_TYPE) && VALUE != null) {

                            if (ORDERING != null) {
                                mCollectionRef
                                        .whereEqualTo(FIELD, VALUE)
                                        .orderBy(ORDERING, DIRECTION)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                                    mList.add(tvMazeResult);
                                                }
                                                stopProgress();
                                                mAdapter.setData(mList);
                                            }
                                        });
                            } else {
                                mCollectionRef
                                        .whereEqualTo(FIELD, VALUE)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                                    mList.add(tvMazeResult);
                                                }
                                                stopProgress();
                                                mAdapter.setData(mList);
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
                                            TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                            mList.add(tvMazeResult);
                                        }
                                        stopProgress();
                                        mAdapter.setData(mList);
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
                                            TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                            mList.add(tvMazeResult);
                                        }
                                        stopProgress();
                                        mAdapter.setData(mList);
                                    }
                                });
                    }
                }

            }
            //Api call on failure
//            @Override
//            public void onFailure(Call<List<TVMazeResult>> call, Throwable t) {
//                Toast.makeText(getActivity(), "Failed Api call..." , Toast.LENGTH_SHORT).show();
//                Log.e("MediaFeedFragment", t.getLocalizedMessage());
//            }
//        });
//  }

    private void startProgress() {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void stopProgress() {
        progressDialog.dismiss();

        sendDataBack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnListCreatedListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Must implement OnListCreatedListener");
        }
    }

    public interface OnListCreatedListener {
        void onListCreated(List<TVMazeResult> list);
    }
}