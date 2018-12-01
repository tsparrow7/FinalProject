package com.example.tjgaming.finalproject.View.Home.Favorites;

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

import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.Utils.CustomStrings;
import com.example.tjgaming.finalproject.View.Home.MediaFeed.OnFragmentVisibleListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 10/5/2018.
 */

public class FavoritesFragment extends Fragment {

    RecyclerView mRecyclerView;
    FavoritesAdapter mAdapter;
    List<FavoriteShow> mList = new ArrayList<>();
    ProgressDialog progressDialog;

    DocumentReference mDocRef;
    CollectionReference mColRef;
    FirebaseFirestore mFirestore;
    Database mDatabase;

    OnFragmentVisibleListener mVisibilityListener;
    boolean isAttached;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        mRecyclerView = root.findViewById(R.id.favorites_recycler);

        initAdapter();
        getData();

        return root;
    }

    private void initAdapter() {
        mAdapter = new FavoritesAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        startProgress();

        mDatabase = new Database(getActivity());
        String userId = mDatabase.getUserLoggedIn().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mColRef = mFirestore.collection("favorites" + "/" + userId + "/" + userId + "-favorites");
        mColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    FavoriteShow favoriteShow = queryDocumentSnapshot.toObject(FavoriteShow.class);
                    mList.add(favoriteShow);
                }
                mAdapter.setData(mList);
                stopProgress();
            }
        });
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

        try {
            mVisibilityListener = (OnFragmentVisibleListener)context;
            isAttached = true;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentVisibleListener");
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        mVisibilityListener.fragmentVisible(true, CustomStrings.FAVORITES_FRAGMENT);
    }
}
