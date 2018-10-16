package com.example.tjgaming.finalproject.View.Home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjgaming.finalproject.Model.TVMazeResult;
import com.example.tjgaming.finalproject.R;

import java.util.List;

/**
 * Created by TJ on 10/15/2018.
 */
public class MediaFeedAdapter extends RecyclerView.Adapter<MediaFeedAdapter.MediaFeedViewHolder> {

    private Context mContext;
    private List<TVMazeResult> mList;

    public MediaFeedAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MediaFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.media_feed_item, parent, false);
        final MediaFeedViewHolder mediaFeedViewHolder = new MediaFeedViewHolder(view);

        return mediaFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(MediaFeedViewHolder holder, int position) {
        holder.testTextView.setText(mList.get(position).getShow().getName());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MediaFeedViewHolder extends RecyclerView.ViewHolder {

        TextView testTextView;

        MediaFeedViewHolder(View itemView) {
            super(itemView);

            testTextView = itemView.findViewById(R.id.media_feed_item_text_view);
        }
    }

    public void setData(List<TVMazeResult> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
