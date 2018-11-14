package com.example.tjgaming.finalproject.View.Home.Favorites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Database.DBWatcher;
import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.Model.UserReview;
import com.example.tjgaming.finalproject.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by TJ on 11/7/2018.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> implements DBWatcher {

    private Context mContext;
    private List<FavoriteShow> mList;
    private List<UserReview> mReviewList;
    private RecyclerView mRecyclerView;
    private Database database;
    private int seekBarStart;
    private String userReview;
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
    public void onFavoriteDeleted(List<FavoriteShow> list) {
        setData(list);
        favoriteDeletedToast();
    }

    @Override
    public void onRating(int rating) {
        seekBarStart = rating;
        displayRatingDialog(view);
    }

    @Override
    public void onReviewSaved(String review) {
//        TextView reviewTV = ((TextView) ((LinearLayout) getView().getParent().getParent().getParent()).getChildAt(1));
//        reviewTV.setText(review);
        reviewSavedToast();
    }

    @Override
    public void onReviewReceived(String review) {
        //Toast.makeText(mContext, review, Toast.LENGTH_SHORT).show();
        userReview = review;
        displayReviewDialog(view);
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorites_item, parent, false);

        final FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(view);
        return favoritesViewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(FavoritesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        holder.showNameTextView.setText(mList.get(position).getShow_name());

        holder.showReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Will bring user to reviews page to view and rate user reviews.
            }
        });

        holder.reviewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the text from the dialog. Save the review in DB.
                setView(v);
                database.getReview(((TextView) ((RelativeLayout) v
                        .getParent()
                        .getParent())
                        .getChildAt(0))
                        .getText()
                        .toString());
                //displayReviewDialog(v);
            }
        });

        holder.ratingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls a rating dialog with a seekbar for rating 0.0 to 10.0
                setView(v);
                database.getUserRating(((TextView) ((RelativeLayout) v
                        .getParent()
                        .getParent())
                        .getChildAt(0))
                        .getText()
                        .toString());
            }
        });

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDeleteDialog(v);
            }
        });
    }

    private void displayReviewDialog(View v){
        final AlertDialog.Builder review = new AlertDialog.Builder(mContext);
        final EditText editText = new EditText(mContext);
        final View view = v;
        final String showName = ((TextView) ((RelativeLayout) v
                .getParent()
                .getParent())
                .getChildAt(0))
                .getText()
                .toString();

        LinearLayout linearLayout = new LinearLayout(mContext);
        review.setTitle("Review");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(16,16,16,16);
        editText.setWidth(linearLayout.getWidth());
        editText.setTextSize(16);
        editText.setHint("Add a review!");
        if (userReview != null && !userReview.equals("Your review here....")){
            editText.append(userReview);//Allows cursor to be placed at the end of review
        }
        linearLayout.addView(editText);
        review.setView(linearLayout);
        review.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get the item that was clicked and save review in database.
                String review = editText.getText().toString();
                database.addReview(showName, review);
            }
        });
        review.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        review.show();
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

    private void ratingSavedToast() {
        Toast.makeText(mContext, "Rating Saved!", Toast.LENGTH_SHORT).show();
    }

    private void favoriteDeletedToast() {
        Toast.makeText(mContext, "Favorite Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void reviewSavedToast() {
        Toast.makeText(mContext, "Review Saved!", Toast.LENGTH_SHORT).show();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        TextView showNameTextView;
        TextView showReviewsTextView;
        ImageView ratingView;
        ImageView deleteView;
        ImageView reviewView;

        FavoritesViewHolder(View view){
            super(view);

            showNameTextView = view.findViewById(R.id.favorites_item_text_view);
            ratingView = view.findViewById(R.id.favorites_item_rating);
            deleteView = view.findViewById(R.id.favorites_item_delete);
            reviewView = view.findViewById(R.id.favorites_item_review);
            showReviewsTextView = view.findViewById(R.id.favorites_item_user_review);
        }
    }

    public void setData(List<FavoriteShow> list){
        mList = list;
        notifyDataSetChanged();
    }
}
