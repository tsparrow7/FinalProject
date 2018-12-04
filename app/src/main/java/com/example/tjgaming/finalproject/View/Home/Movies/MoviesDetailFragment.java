package com.example.tjgaming.finalproject.View.Home.Movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Model.Favorite;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.Utils.CustomStrings;

import java.util.List;

public class MoviesDetailFragment extends Fragment implements View.OnClickListener{

    private Double mMovieRating;
    private String mMovieTitle;
    private String mMoviePosterUrl;
    private List<String> mGenresList;

    TextView mMovieTitleTextView;
    TextView mMovieRatingTextView;
    TextView mMoviePosterTextView;
    TextView mMovieGenresTextView;

    FloatingActionButton mFavorite;

    Database mDatabase;
    Favorite favorite;

    public MoviesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_detail, container, false);

        (mMovieTitleTextView = root.findViewById(R.id.movie_detail_title)).setText(mMovieTitle);
        (mMovieRatingTextView = root.findViewById(R.id.movie_detail_rating)).setText(String.valueOf(mMovieRating));
        (mMoviePosterTextView = root.findViewById(R.id.movie_detail_poster_url)).setText(mMoviePosterUrl);
        (mMovieGenresTextView = root.findViewById(R.id.movie_detail_genres)).setText(mGenresList.toString());

        (mFavorite = root.findViewById(R.id.movie_favorite_action_button)).setOnClickListener(this);

        return root;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        mMovieRating = args.getDouble("movie_rating");
        mMovieTitle = args.getString("title");
        mMoviePosterUrl = args.getString("poster_url");
        mGenresList = args.getStringArrayList("genres");
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),mMovieTitle + " added to favorites!",Toast.LENGTH_SHORT).show();

        favorite = new Favorite(mMovieTitle, mMovieRating);
        favorite.setTypeOfMedia(CustomStrings.MOVIES);
        mDatabase = new Database(getContext());
        mDatabase.addFavorite(favorite);
    }
}
