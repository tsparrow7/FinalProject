package com.example.tjgaming.finalproject.View.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Api.ApiClient;
import com.example.tjgaming.finalproject.Api.ApiInterface;
import com.example.tjgaming.finalproject.Model.TVMazeResult;
import com.example.tjgaming.finalproject.R;

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
    ArrayList<TVMazeResult> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_feed, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.media_feed_recycler);

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
                mAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<TVMazeResult>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed Api call, u suck..", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
