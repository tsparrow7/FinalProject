package com.example.tjgaming.finalproject.View.Home.Favorites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Database.DBWatcher;
import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by TJ on 11/7/2018.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> implements DBWatcher {

    private Context mContext;
    private List<FavoriteShow> mList;
    private RecyclerView mRecyclerView;
    private Database database;
    private int seekBarStart;
    private View view;

    public FavoritesAdapter(Context context) {
        mContext = context;
        database = new Database(mContext);
        database.setWatcher(this);
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    @Override
    public void onFavorite(List<FavoriteShow> list) {
        setData(list);
        favoriteDeletedToast();
    }

    @Override
    public void onRating(int rating) {
        seekBarStart = rating;
        displayRatingDialog(getView());
    }

//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onRatingEvent(RatingEvent event) {
//        seekBarStart = event.rating;
//        displayRatingDialog(getView());
//        Log.i("onRatingEvent",getView().toString());
//    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorites_item, parent, false);

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

        final FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(view);
        return favoritesViewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(FavoritesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        holder.showNameTextView.setText(mList.get(position).getShow_name());

        holder.ratingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls a rating dialog with a seekbar for rating 0.0 to 10.0
                database.getUserRating(((TextView) ((RelativeLayout) v.getParent().getParent()).getChildAt(0)).getText().toString());
                setView(v);
                //displayRatingDialog(view);
            }
        });

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDeleteDialog(v);
            }
        });
    }

    private void displayDeleteDialog(final View v) {
        final View view = v;
        final String showName = ((TextView) ((RelativeLayout) v.getParent().getParent()).getChildAt(0)).getText().toString();
        final List<FavoriteShow> list = mList;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setMessage("Are you sure you want to delete this favorite?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.deleteFavorite(showName,list);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Delete");
        alert.show();
    }

    private void displayRatingDialog(View v) {
        final AlertDialog.Builder rating = new AlertDialog.Builder(mContext);
        final TextView textView = new TextView(mContext);
        final SeekBar seekBar = new SeekBar(mContext);
        final View view = v;

        final String showName = ((TextView) ((RelativeLayout) v.getParent().getParent()).getChildAt(0)).getText().toString();

        LinearLayout linearLayout = new LinearLayout(mContext);

        rating.setTitle("Rating");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(100,32,100,32);
        textView.setTextSize(32);
        textView.setPadding(8,8,8,8);
        seekBar.setProgress(seekBarStart);
        textView.setText(String.format(Locale.ENGLISH,"%.1f", (seekBarStart / 10.0)));

        linearLayout.addView(textView);
        linearLayout.addView(seekBar);

        rating.setView(linearLayout);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.format(Locale.ENGLISH,"%.1f", (progress / 10.0)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rating.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get the item that was clicked and save rating in database.
                float value = (seekBar.getProgress() / 10f);
                Database database = new Database(mContext);
                database.addUserRating(showName,value);
                dialog.dismiss();
                ratingSavedToast();
            }
        });
        rating.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        rating.show();
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

    public void ratingSavedToast() {
        Toast.makeText(mContext, "Rating Saved!", Toast.LENGTH_SHORT).show();
    }

    public void favoriteDeletedToast() {
        Toast.makeText(mContext, "Favorite Deleted!", Toast.LENGTH_SHORT).show();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        TextView showNameTextView;
        ImageView ratingView;
        ImageView deleteView;

        FavoritesViewHolder(View view){
            super(view);

            showNameTextView = view.findViewById(R.id.favorites_item_text_view);
            ratingView = view.findViewById(R.id.favorites_item_rating);
            deleteView = view.findViewById(R.id.favorites_item_delete);
        }
    }

    public void setData(List<FavoriteShow> list){
        mList = list;
        notifyDataSetChanged();
    }
}
