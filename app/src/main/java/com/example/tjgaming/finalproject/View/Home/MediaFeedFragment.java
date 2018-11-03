package com.example.tjgaming.finalproject.View.Home;

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
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    DocumentReference documentReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_feed, container, false);

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

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<TVMazeResult>> call = apiInterface.getTvMazeResults();

        call.enqueue(new Callback<List<TVMazeResult>>() {
            @Override
            public void onResponse(Call<List<TVMazeResult>> call, Response<List<TVMazeResult>> response) {
                mList = response.body();

                for (int i = 0; i < mList.size(); i++) {
                    documentReference = FirebaseFirestore.getInstance()
                            .collection("TV Shows")
                            .document(mList.get(i).getShow().getName());

                    documentReference.set(mList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Saving media to db","TV Show saved");
                            } else {
                                Log.d("Saving media to db","TV Show not saved");
                            }
                        }
                    });
                }

                //TODO: Retrieve data from database and set to the data in the adapter.
                //TODO: Implement sorting for data based on genre/name/rating/type

                mAdapter.setData(response.body());
            }
            @Override
            public void onFailure(Call<List<TVMazeResult>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed Api call..." , Toast.LENGTH_SHORT).show();
                Log.e("MediaFeedFragment", t.getLocalizedMessage());
            }
        });
    }
}