package com.example.tjgaming.finalproject.View.Home.Movies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tjgaming.finalproject.Model.TheMovieDB.TMDBMovie;
import com.example.tjgaming.finalproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 11/28/2018.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MediaFeedViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<TMDBMovie> mList;
    private RecyclerView mRecyclerView;

    public MoviesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MediaFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_movies_item, parent, false);
        view.setOnClickListener(this);

        final MediaFeedViewHolder mediaFeedViewHolder = new MediaFeedViewHolder(view);
        return mediaFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(MediaFeedViewHolder holder, int position) {

        holder.movieNameTextView.setText(mList.get(position).getMovieTitle());

        try {
            Glide.with(mContext)
                    .load(mList.get(position).getMoviePosterUrl())
                    .into(holder.moviePosterView);
        } catch (NullPointerException e) {
            Log.e("MoviesAdapter","Caught null: " + e.getMessage());

            try {
                Glide.with(mContext)
                        .load(mList.get(position).getMoviePosterUrl())
                        .into(holder.moviePosterView);
            } catch (NullPointerException e1) {
                Log.e("MoviesAdapter","Caught null: " + e.getMessage());
                Glide.with(mContext)
                        .load(mContext.getDrawable(R.drawable.common_google_signin_btn_icon_dark_focused))
                        .into(holder.moviePosterView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onClick(View v) {

        int itemPosition = mRecyclerView.getChildAdapterPosition(v);

        AppCompatActivity activity = (AppCompatActivity) mContext;
        MoviesDetailFragment fragment = new MoviesDetailFragment();

        Bundle args = new Bundle();
        args.putDouble("movie_rating",mList.get(itemPosition).getMovieRating());
        args.putString("title",mList.get(itemPosition).getMovieTitle());
        args.putString("poster_url",mList.get(itemPosition).getMoviePosterUrl());
        args.putStringArrayList("genres", ((ArrayList<String>) mList.get(itemPosition).getGenreIds()));

        fragment.setArguments(args);

        activity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_up, 0,0, R.anim.slide_down)
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;

    }

    class MediaFeedViewHolder extends RecyclerView.ViewHolder {

        TextView movieNameTextView;
        ImageView moviePosterView;

        MediaFeedViewHolder(View itemView) {
            super(itemView);

            movieNameTextView = itemView.findViewById(R.id.movie_media_feed_item_text_view);
            moviePosterView = itemView.findViewById(R.id.movie_media_feed_item_image_view);
        }
    }

    public void setData(List<TMDBMovie> list) {
        mList = list;
        notifyDataSetChanged();
    }
}

