package com.example.tjgaming.finalproject.View.Home;

import android.app.ProgressDialog;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Api.ApiClient;
import com.example.tjgaming.finalproject.Api.ApiInterface;
import com.example.tjgaming.finalproject.CustomStrings;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TJ on 10/5/2018.
 */

public class MediaFeedFragment extends Fragment {

    RecyclerView mRecyclerView;
    MediaFeedAdapter mAdapter;
    List<TVMazeResult> mList = new ArrayList<>();
    ProgressDialog progressDialog;

    private String FIELD;
    private String VALUE;
    private String ORDERING;
    private Query.Direction DIRECTION;

    DocumentReference documentReference;
    CollectionReference mCollectionRef;
    FirebaseFirestore mFirestore;
    FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_feed, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            FIELD = bundle.getString("Field");
            VALUE = bundle.getString("Value");
            ORDERING = bundle.getString("Order");
            String STR_DIRECTION = bundle.getString("Direction");

            if (STR_DIRECTION.equals("Ascending")){
                DIRECTION = Query.Direction.ASCENDING;
            } else {
                DIRECTION = Query.Direction.DESCENDING;
            }
        }
        mRecyclerView = root.findViewById(R.id.media_feed_recycler);

        initAdapter();
        tvMazeRequest();
        return root;
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

                if (FIELD == null || VALUE == null || ORDERING == null || DIRECTION == null) {
                    mList.clear();
                    mFirestore = FirebaseFirestore.getInstance();
                    mCollectionRef = mFirestore.collection("TV Shows");
                    mCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                                TVMazeResult tvMazeResult = queryDocumentSnapshot.toObject(TVMazeResult.class);

                                mList.add(tvMazeResult);
                            }
                            stopProgress();
                            mAdapter.setData(mList);
                        }
                    });
                } else {

                    mList.clear();
                    mFirestore = FirebaseFirestore.getInstance();
                    mCollectionRef = mFirestore.collection("TV Shows");
                    mCollectionRef.whereEqualTo(FIELD, VALUE).orderBy(ORDERING, DIRECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                //TODO: Implement sorting for data based on genre/name/rating/type
                //TODO: pass values for ordering the data and the direction (ascending/descending)
                //TODO: pass values for filtering data by type, genre, rating, days, etc.
                /**
                 * For Example:
                 *
                 *                                    field                     direction
                 *  mCollectionRef.orderBy           ("show.rating.average",    Query.Direction.DESCENDING).get();
                 *
                 *
                 *                                    field                 value
                 *  mCollectionRef.whereArrayContains("show.genres",        "Crime")        .get();
                 *  mCollectionRef.whereArrayContains("show.network.name",  "TNT")          .get();
                 *  mCollectionRef.whereEqualTo      ("show.type",          "Animation")    .get();
                 */

//           // }
//            @Override
//            public void onFailure(Call<List<TVMazeResult>> call, Throwable t) {
//                Toast.makeText(getActivity(), "Failed Api call..." , Toast.LENGTH_SHORT).show();
//                Log.e("MediaFeedFragment", t.getLocalizedMessage());
//            }
//        });
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
}