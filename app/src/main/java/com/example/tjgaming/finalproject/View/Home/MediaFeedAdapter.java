package com.example.tjgaming.finalproject.View.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.R;

import java.util.List;

/**
 * Created by TJ on 10/15/2018.
 */
public class MediaFeedAdapter extends RecyclerView.Adapter<MediaFeedAdapter.MediaFeedViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<TVMazeResult> mList;
    private RecyclerView mRecyclerView;

    public MediaFeedAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MediaFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.media_feed_item, parent, false);
        view.setOnClickListener(this);

        final MediaFeedViewHolder mediaFeedViewHolder = new MediaFeedViewHolder(view);
        return mediaFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(MediaFeedViewHolder holder, int position) {

        holder.showNameTextView.setText(mList.get(position).getShow().getName());

        Glide.with(mContext)
                .load(mList.get(position).getShow().getImage().getOriginal())
                .into(holder.showImageView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildAdapterPosition(v);

        AppCompatActivity activity = (AppCompatActivity) mContext;
        MediaFeedDetailsFragment fragment = new MediaFeedDetailsFragment();

        Bundle args = new Bundle();
        args.putString("show_name",mList.get(itemPosition).getShow().getName());
        args.putString("episode_name",mList.get(itemPosition).getName());
        args.putInt("season",mList.get(itemPosition).getSeason());
        args.putInt("number",mList.get(itemPosition).getNumber());
        args.putString("time",mList.get(itemPosition).getShow().getSchedule().getTime());
        args.putStringArray("days",mList.get(itemPosition).getShow().getSchedule().getDays());
        args.putString("network",mList.get(itemPosition).getShow().getNetwork().getName());
        args.putDouble("rating",mList.get(itemPosition).getShow().getRating().getAverage());

        fragment.setArguments(args);

        activity.getSupportFragmentManager().beginTransaction()
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

        TextView showNameTextView;
        ImageView showImageView;

        MediaFeedViewHolder(View itemView) {
            super(itemView);

            showNameTextView = itemView.findViewById(R.id.media_feed_item_text_view);
            showImageView = itemView.findViewById(R.id.media_feed_item_image_view);
        }
    }

    public void setData(List<TVMazeResult> list) {
        mList = list;
        notifyDataSetChanged();
    }
}