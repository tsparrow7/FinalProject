package com.example.tjgaming.finalproject.View.Home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.R;

import java.util.List;

/**
 * Created by TJ on 11/7/2018.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private Context mContext;
    private List<FavoriteShow> mList;
    private RecyclerView mRecyclerView;

    public FavoritesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorites_item, parent, false);

        final FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(view);
        return favoritesViewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        holder.showNameTextView.setText(mList.get(position).getShow_name());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        TextView showNameTextView;

        FavoritesViewHolder(View view){
            super(view);

            showNameTextView = view.findViewById(R.id.favorites_item_text_view);
        }
    }

    public void setData(List<FavoriteShow> list){
        mList = list;
        notifyDataSetChanged();
    }
}
