package com.example.tjgaming.finalproject.View.Home.MediaFeed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.R;

import java.util.List;

public class MediaFeedDetailsFragment extends Fragment implements View.OnClickListener {

    private String mShowName;
    private String mEpisodeName;
    private int mSeason;
    private int mEpisode;
    private String mTime;
    private List<String> mDays;
    private String mNetwork;
    private double mRating;

    TextView mShowTextView;
    TextView mEpisodeNameTextView;
    TextView mSeasonTextView;
    TextView mEpisodeTextView;
    TextView mTimeTextView;
    TextView mDaysTextView;
    TextView mNetworkTextView;
    TextView mRatingTextView;

    FloatingActionButton mFavorite;

    Database mDatabase;
    FavoriteShow mFavoriteShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_media_feed_details,container,false);

        (mShowTextView = root.findViewById(R.id.media_feed_details_show_name)).setText(mShowName);
        (mEpisodeNameTextView = root.findViewById(R.id.media_feed_details_episode_name)).setText(mEpisodeName);
        (mSeasonTextView = root.findViewById(R.id.media_feed_details_season)).setText(String.valueOf("Season: " +mSeason));
        (mEpisodeTextView = root.findViewById(R.id.media_feed_details_episode)).setText(String.valueOf("Episode: " +mEpisode));
        (mTimeTextView = root.findViewById(R.id.media_feed_details_times)).setText(" @ " + mTime);
        (mDaysTextView = root.findViewById(R.id.media_feed_details_days)).setText(mDays.toString());
        (mNetworkTextView = root.findViewById(R.id.media_feed_details_network)).setText(mNetwork);
        (mRatingTextView = root.findViewById(R.id.media_feed_details_rating)).setText(String.valueOf(mRating));

        (mFavorite = root.findViewById(R.id.details_favorite_action_button)).setOnClickListener(this);

        return root;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        mShowName = args.getString("show_name");
        mEpisodeName = args.getString("episode_name");
        mSeason = args.getInt("season");
        mEpisode = args.getInt("number");
        mTime = args.getString("time");
        mDays = args.getStringArrayList("days");
        mNetwork = args.getString("network");
        mRating = args.getDouble("rating");
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),mShowName + " added to favorites!",Toast.LENGTH_SHORT).show();

        mFavoriteShow = new FavoriteShow(mShowName,mDays,mTime,mNetwork,mRating);

        mDatabase = new Database(getContext());
        mDatabase.addToFavorites(mFavoriteShow);
    }
}
