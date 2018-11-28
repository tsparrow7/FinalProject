package com.example.tjgaming.finalproject.View.Home.Movies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Model.TheMovieDB.TMDBMovie;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.Utils.GetMoviesTask;
import com.example.tjgaming.finalproject.Utils.NetworkChecker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    public MoviesFragment() {
    }

    //List for Storing movie objects
    private ArrayList<String> movieIdList = new ArrayList<>();
    private ArrayList<String> posterUrlList = new ArrayList<>();
    private ArrayList<TMDBMovie> tmdbMoviesList = new ArrayList<>();

    //data pulling from API to create movie objects
    private String movieId = null;
    private Double movieRating = null;
    private String movieTitle = null;
    private String poster = null;
    private List<Integer> genresList = new ArrayList<>();


    //JSON data for getting movies from the API
    final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    final String BASE_JSON_REQUEST = "api.themoviedb.org";
    final String JSON_REQUEST_PARAM = "3";
    final String MOVIE_JSON_REQUEST = "movie";
    final String API_KEY_PARAM = "api_key";
    final String POSTER_SIZE_PARAM = "w500";
    final String POPULAR_MOVIES_PARAM = "popular";

    private GridView mGridView;
    private String mKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = v.findViewById(R.id.grid_view);
        mKey = getString(R.string.api_key);

        getMovieDataFromApi();
        return v;
    }

    public void getMovieDataFromApi(){

        String movieData = "";

        try {
            if(NetworkChecker.isNetworkActive(getActivity())) {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority(BASE_JSON_REQUEST)
                        .appendPath(JSON_REQUEST_PARAM)
                        .appendPath(MOVIE_JSON_REQUEST)
                        .appendPath(POPULAR_MOVIES_PARAM)
                        .appendQueryParameter(API_KEY_PARAM, mKey);
                String myUrl = builder.build().toString();

                Log.i("MoviesFragment","URL to call: " + myUrl);


                GetMoviesTask movieTask = new GetMoviesTask();
                movieData = movieTask.execute(myUrl).get();
            }
            if (movieData != null) {

                JSONObject moviesObject = new JSONObject(movieData);
                JSONArray moviesArray = moviesObject.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {

                    JSONObject jsonObject = moviesArray.getJSONObject(i);

                    //Creating a movie to save in database after API call finishes
                    movieId = jsonObject.getString("id");
                    poster = String.format("%s%s%s", BASE_POSTER_URL, POSTER_SIZE_PARAM, jsonObject.getString("poster_path"));
                    movieTitle = jsonObject.getString("title");
                    movieRating = jsonObject.getDouble("vote_average");
                    //Genre ids list
                    JSONArray jsonArray = jsonObject.getJSONArray("genre_ids");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        genresList.add(jsonArray.getInt(j));
                    }

                    TMDBMovie movie = new TMDBMovie(movieId,poster,movieTitle,movieRating,genresList);

                    tmdbMoviesList.add(movie);

                    movieIdList.add(jsonObject.getString("id"));
                    String poster_path = (jsonObject.getString("poster_path"));
                    String poster_url = BASE_POSTER_URL+POSTER_SIZE_PARAM+poster_path;
                    posterUrlList.add(poster_url);
                }

                String[] postersArray = new String[posterUrlList.size()];
                postersArray = posterUrlList.toArray(postersArray);

                GridViewAdapter adapter = new GridViewAdapter(getContext(), getId(), postersArray);
                mGridView.setAdapter(adapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String movie = movieIdList.get(position);
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "Network currently not available", Toast.LENGTH_LONG)
                        .show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Custom Array Adapter
    public class GridViewAdapter extends ArrayAdapter {

        private Context context;
        private LayoutInflater inflater;
        private int id;
        private String[] imageURls;

        GridViewAdapter(Context context, int id, String[] imageUrls){

            super(context, R.layout.fragment_movies, imageUrls);

            this.context = context;
            this.id = id;
            this.imageURls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if(convertView == null){

                convertView = inflater.inflate(R.layout.fragment_movies_item, parent, false);
            }
            Picasso.with(context)
                    .load(imageURls[position])
                    .fit()
                    .into((ImageView) convertView);
            return convertView;
        }
    }
}
