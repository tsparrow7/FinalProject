package com.example.tjgaming.finalproject.View.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Database.DBWatcher;
import com.example.tjgaming.finalproject.Database.Database;
import com.example.tjgaming.finalproject.Utils.CustomStrings;
import com.example.tjgaming.finalproject.Model.FavoriteShow;
import com.example.tjgaming.finalproject.Model.TVMaze.TVMazeResult;
import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.Model.UserReview;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Authentication.LoginActivity;
import com.example.tjgaming.finalproject.View.Home.Favorites.FavoritesFragment;
import com.example.tjgaming.finalproject.Model.SearchModel;
import com.example.tjgaming.finalproject.View.Home.Forum.ForumFragment;
import com.example.tjgaming.finalproject.View.Home.MediaFeed.MediaTabbedFragment;
import com.example.tjgaming.finalproject.View.Home.MediaFeed.OnFragmentVisibleListener;
import com.example.tjgaming.finalproject.View.Home.TVShows.TVShowsFragment;
import com.example.tjgaming.finalproject.View.Home.Profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TVShowsFragment.OnListCreatedListener, DBWatcher, OnFragmentVisibleListener {

    private DocumentReference mDocRef;
    private FirebaseAuth mFirebaseAuth;
    private Database mDatabase;
    private User mUser;
    private TextView mUserName;
    private TextView mUserEmail;

    private String mFilterSelection;
    private String mSortSelection;
    private String mFilterField;
    private String mSortDirection;

    public static final String sTypeOfBundle = "TypeOfBundle";
    public static final String sLogin = "Login";
    public static final String sFilter = "Filter";
    public static final String sSearch = "Search";
    public static final String sNavDrawer = "Navigation Drawer";

    private String visibleFragment = null;

    private Bundle mBundle;
    private MediaTabbedFragment mMediaTabbedFragment;
    private FavoritesFragment favoritesFragment;

    RadioButton typeBtn;
    RadioButton genreBtn;
    RadioButton sortBtn;

    ArrayList<SearchModel> searchItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mUserName = headerView.findViewById(R.id.nav_header_user_name);
        mUserEmail = headerView.findViewById(R.id.nav_header_user_email);
        ImageView mProfileImage = headerView.findViewById(R.id.nav_header_profile_image);

        String uid = getIntent().getStringExtra("user_id");

        mDatabase = new Database(HomeActivity.this);
        mDatabase.setWatcher(this);
        //Get a Firebase Database Reference and get the data of the user logging in
        mDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.getCurrentUser();

        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mUser = task.getResult().toObject(User.class);
                    mUserEmail.setText(mUser.getEmail());
                    mUserName.setText(mUser.getUsername());
                    Log.d("HomeActivity", mUser.toString());
                }
            }
        });


        if (savedInstanceState == null) {
            mBundle = new Bundle();

            mMediaTabbedFragment = new MediaTabbedFragment();
            mBundle.putString(sTypeOfBundle,sLogin);
            mMediaTabbedFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mMediaTabbedFragment).commit();
            navigationView.setCheckedItem(R.id.nav_media_feed);

//            favoritesFragment = new FavoritesFragment();
//            mBundle.putString(sTypeOfBundle, sLogin);
//            favoritesFragment.setArguments(mBundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    favoritesFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            refineSearchDialog();
        } else if (id==R.id.action_search) {
            mDatabase.getAllShowsForSearch();
        } else if (id==R.id.action_logout) {
            logoutDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refineSearchDialog() {

        switch (visibleFragment){
            case "Movies":
                movieRefine();
                break;
            case "TV Shows":
                tvShowRefine();
                break;
            case "Favorites":
                favoritesRefine();
                break;
        }

    }

    public void tvShowRefine() {
        //      TV SHOW REFINE AND FILTER
        final String[] filterTypeArr = {CustomStrings.SHOW_TYPE_DEFAULT,
                CustomStrings.REALITY, CustomStrings.ANIMATION, CustomStrings.NEWS,
                CustomStrings.DOCUMENTARY, CustomStrings.TALK_SHOW, CustomStrings.SCRIPTED,
                CustomStrings.GAME_SHOW};

        final String[] filterGenreArr = {CustomStrings.SHOW_GENRE_DEFAULT,
                CustomStrings.COMEDY, CustomStrings.DRAMA, CustomStrings.ROMANCE,
                CustomStrings.ADVENTURE, CustomStrings.NATURE, CustomStrings.FAMILY,
                CustomStrings.CRIME, CustomStrings.MYSTERY, CustomStrings.SUPERNATURAL,
                CustomStrings.HORROR, CustomStrings.HISTORY, CustomStrings.SCIENCE_FICTION,
                CustomStrings.FANTASY, CustomStrings.THRILLER};

        final String[] sortingArr = {CustomStrings.SHOW_SORT_DEFAULT,
                CustomStrings.SHOW_NAME,CustomStrings.SHOW_RATING_AVERAGE};

        AlertDialog.Builder refineBuilder = new AlertDialog.Builder(this);
        View refineDialogView = getLayoutInflater().inflate(R.layout.filter_dialog_spinner, null);
        refineBuilder.setTitle(getResources().getString(R.string.action_refine_tv_shows));

        typeBtn = refineDialogView.findViewById(R.id.filter_type_button);
        genreBtn = refineDialogView.findViewById(R.id.filter_genre_button);
        sortBtn = refineDialogView.findViewById(R.id.sort_button);

        final Spinner filterTypeSpinner = (Spinner) refineDialogView.findViewById(R.id.filter_type_spinner);
        filterTypeSpinner.setEnabled(false);
        ArrayAdapter<String> filterTypeAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, filterTypeArr);
        filterTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterTypeSpinner.setAdapter(filterTypeAdapter);
        refineBuilder.setView(refineDialogView);

        final Spinner filterGenreSpinner = (Spinner) refineDialogView.findViewById(R.id.filter_genre_spinner);
        filterGenreSpinner.setEnabled(false);
        final ArrayAdapter<String> filterGenreAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, filterGenreArr);
        filterGenreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterGenreSpinner.setAdapter(filterGenreAdapter);
        refineBuilder.setView(refineDialogView);

        final Spinner sortingSpinner = (Spinner) refineDialogView.findViewById(R.id.sort_spinner);
        sortingSpinner.setEnabled(false);
        ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, sortingArr);
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(sortingAdapter);
        refineBuilder.setView(refineDialogView);

        typeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genreBtn.setChecked(false);
                filterGenreSpinner.setEnabled(false);

                if (!filterTypeSpinner.isEnabled())
                    filterTypeSpinner.setEnabled(true);
            }
        });

        genreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeBtn.setChecked(false);
                filterTypeSpinner.setEnabled(false);

                if (!filterGenreSpinner.isEnabled())
                    filterGenreSpinner.setEnabled(true);
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortingSpinner.isEnabled())
                    sortingSpinner.setEnabled(true);
            }
        });

        refineBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mMediaTabbedFragment = new MediaTabbedFragment();
                mBundle = new Bundle();

                if (typeBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_TYPE;
                    mFilterSelection = filterTypeSpinner.getSelectedItem().toString();
                } else if (genreBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_GENRES;
                    mFilterSelection = filterGenreSpinner.getSelectedItem().toString();
                }
                if (sortBtn.isChecked()) {
                    mSortSelection = sortingSpinner.getSelectedItem().toString();
                    if (mSortSelection.equals(CustomStrings.SHOW_NAME)){
                        mSortDirection = "Ascending"; // If we are sorting by name then we want direction to be Ascending
                    } else if (mSortSelection.equals(CustomStrings.SHOW_RATING_AVERAGE)){
                        mSortDirection = "Descending";
                    }
                }

                mBundle.putString(sTypeOfBundle,sFilter);
                mBundle.putString("Field",mFilterField);
                mBundle.putString("Value",mFilterSelection);
                mBundle.putString("Order",mSortSelection);
                mBundle.putString("Direction",mSortDirection);

                mMediaTabbedFragment.setArguments(mBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mMediaTabbedFragment).commit();
            }
        });

        refineBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog refineDialog = refineBuilder.create();
        refineDialog.show();
//      END TV SHOW REFINE AND FILTER
    }

    public void movieRefine() {
        //      START MOVIE REFINE AND FILTER
        final String[] filterMovieTypeArr = {CustomStrings.SHOW_TYPE_DEFAULT,
                CustomStrings.REALITY, CustomStrings.ANIMATION, CustomStrings.NEWS,
                CustomStrings.DOCUMENTARY, CustomStrings.TALK_SHOW, CustomStrings.SCRIPTED,
                CustomStrings.GAME_SHOW};

        final String[] filterMovieGenreArr = {CustomStrings.SHOW_GENRE_DEFAULT,
                CustomStrings.COMEDY, CustomStrings.DRAMA, CustomStrings.ROMANCE,
                CustomStrings.ADVENTURE, CustomStrings.NATURE, CustomStrings.FAMILY,
                CustomStrings.CRIME, CustomStrings.MYSTERY, CustomStrings.SUPERNATURAL,
                CustomStrings.HORROR, CustomStrings.HISTORY, CustomStrings.SCIENCE_FICTION,
                CustomStrings.FANTASY, CustomStrings.THRILLER};

        final String[] sortingMovieArr = {CustomStrings.SHOW_SORT_DEFAULT,
                CustomStrings.SHOW_NAME,CustomStrings.SHOW_RATING_AVERAGE};

        AlertDialog.Builder refineMovieBuilder = new AlertDialog.Builder(this);
        View refineMovieDialogView = getLayoutInflater().inflate(R.layout.filter_dialog_spinner, null);
        refineMovieBuilder.setTitle(getResources().getString(R.string.action_refine_movies));

        typeBtn = refineMovieDialogView.findViewById(R.id.filter_type_button);
        genreBtn = refineMovieDialogView.findViewById(R.id.filter_genre_button);
        sortBtn = refineMovieDialogView.findViewById(R.id.sort_button);

        final Spinner filterMovieTypeSpinner = (Spinner) refineMovieDialogView.findViewById(R.id.filter_type_spinner);
        filterMovieTypeSpinner.setEnabled(false);
        ArrayAdapter<String> filterMovieTypeAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, filterMovieTypeArr);
        filterMovieTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterMovieTypeSpinner.setAdapter(filterMovieTypeAdapter);
        refineMovieBuilder.setView(refineMovieDialogView);

        final Spinner filterMovieGenreSpinner = (Spinner) refineMovieDialogView.findViewById(R.id.filter_genre_spinner);
        filterMovieGenreSpinner.setEnabled(false);
        final ArrayAdapter<String> filterMovieGenreAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, filterMovieGenreArr);
        filterMovieGenreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterMovieGenreSpinner.setAdapter(filterMovieGenreAdapter);
        refineMovieBuilder.setView(refineMovieDialogView);

        final Spinner sortingMovieSpinner = (Spinner) refineMovieDialogView.findViewById(R.id.sort_spinner);
        sortingMovieSpinner.setEnabled(false);
        ArrayAdapter<String> sortingMovieAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, sortingMovieArr);
        sortingMovieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingMovieSpinner.setAdapter(sortingMovieAdapter);
        refineMovieBuilder.setView(refineMovieDialogView);

        typeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genreBtn.setChecked(false);
                filterMovieGenreSpinner.setEnabled(false);

                if (!filterMovieTypeSpinner.isEnabled())
                    filterMovieTypeSpinner.setEnabled(true);
            }
        });

        genreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeBtn.setChecked(false);
                filterMovieTypeSpinner.setEnabled(false);

                if (!filterMovieGenreSpinner.isEnabled())
                    filterMovieGenreSpinner.setEnabled(true);
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortingMovieSpinner.isEnabled())
                    sortingMovieSpinner.setEnabled(true);
            }
        });

        refineMovieBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mMediaTabbedFragment = new MediaTabbedFragment();
                mBundle = new Bundle();

                if (typeBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_TYPE;
                    mFilterSelection = filterMovieTypeSpinner.getSelectedItem().toString();
                } else if (genreBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_GENRES;
                    mFilterSelection = filterMovieGenreSpinner.getSelectedItem().toString();
                }
                if (sortBtn.isChecked()) {
                    mSortSelection = sortingMovieSpinner.getSelectedItem().toString();
                    if (mSortSelection.equals(CustomStrings.SHOW_NAME)){
                        mSortDirection = "Ascending"; // If we are sorting by name then we want direction to be Ascending
                    } else if (mSortSelection.equals(CustomStrings.SHOW_RATING_AVERAGE)){
                        mSortDirection = "Descending";
                    }
                }

                mBundle.putString(sTypeOfBundle,sFilter);
                mBundle.putString("Field",mFilterField);
                mBundle.putString("Value",mFilterSelection);
                mBundle.putString("Order",mSortSelection);
                mBundle.putString("Direction",mSortDirection);

                mMediaTabbedFragment.setArguments(mBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mMediaTabbedFragment).commit();
            }
        });

        refineMovieBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog refineMovieDialog = refineMovieBuilder.create();
        refineMovieDialog.show();
//      END MOVIE REFINE AND FILTER
    }

    private void favoritesRefine() {
        //      START FAVORITES REFINE AND FILTER
        final String[] sortFavoriteArr = {CustomStrings.NAME, CustomStrings.RATING};
        final String[] filterFavoriteArr = {CustomStrings.TV_SHOWS, CustomStrings.MOVIES};

        AlertDialog.Builder refineFavoriteBuilder = new AlertDialog.Builder(this);
        View refineFavoriteDialogView = getLayoutInflater().inflate(R.layout.filter_dialog_spinner, null);
        refineFavoriteBuilder.setTitle(getResources().getString(R.string.action_refine_favorites));

        typeBtn = refineFavoriteDialogView.findViewById(R.id.filter_type_button);
        sortBtn = refineFavoriteDialogView.findViewById(R.id.sort_button);
        genreBtn = refineFavoriteDialogView.findViewById(R.id.filter_genre_button);
        genreBtn.setVisibility(View.INVISIBLE);

        final Spinner filterFavoriteSpinner = (Spinner) refineFavoriteDialogView.findViewById(R.id.filter_type_spinner);
        filterFavoriteSpinner.setEnabled(false);

        ArrayAdapter<String> filterFavoriteAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, filterFavoriteArr);
        filterFavoriteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterFavoriteSpinner.setAdapter(filterFavoriteAdapter);
        refineFavoriteBuilder.setView(refineFavoriteDialogView);

        final Spinner filterFavGenreSpinner = (Spinner) refineFavoriteDialogView.findViewById(R.id.filter_genre_spinner);
        filterFavGenreSpinner.setVisibility(View.INVISIBLE);


        final Spinner sortingFavoriteSpinner = (Spinner) refineFavoriteDialogView.findViewById(R.id.sort_spinner);
        sortingFavoriteSpinner.setEnabled(false);

        ArrayAdapter<String> sortingFavoriteAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, sortFavoriteArr);
        sortingFavoriteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingFavoriteSpinner.setAdapter(sortingFavoriteAdapter);
        refineFavoriteBuilder.setView(refineFavoriteDialogView);

        typeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFavoriteSpinner.setEnabled(false);

                if (!filterFavoriteSpinner.isEnabled())
                    filterFavoriteSpinner.setEnabled(true);
            }
        });


        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortingFavoriteSpinner.isEnabled())
                    sortingFavoriteSpinner.setEnabled(true);
            }
        });

        refineFavoriteBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mMediaTabbedFragment = new MediaTabbedFragment();
                mBundle = new Bundle();

                if (typeBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_TYPE;
                    mFilterSelection = filterFavoriteSpinner.getSelectedItem().toString();
                } else if (genreBtn.isChecked()) {
                    mFilterField = CustomStrings.SHOW_GENRES;
                    mFilterSelection = filterFavoriteSpinner.getSelectedItem().toString();
                }
                if (sortBtn.isChecked()) {
                    mSortSelection = sortingFavoriteSpinner.getSelectedItem().toString();
                    if (mSortSelection.equals(CustomStrings.SHOW_NAME)){
                        mSortDirection = "Ascending"; // If we are sorting by name then we want direction to be Ascending
                    } else if (mSortSelection.equals(CustomStrings.SHOW_RATING_AVERAGE)){
                        mSortDirection = "Descending";
                    }
                }

                mBundle.putString(sTypeOfBundle,sFilter);
                mBundle.putString("Field",mFilterField);
                mBundle.putString("Value",mFilterSelection);
                mBundle.putString("Order",mSortSelection);
                mBundle.putString("Direction",mSortDirection);

                mMediaTabbedFragment.setArguments(mBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mMediaTabbedFragment).commit();
            }
        });

        refineFavoriteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog refineFavoriteDialog = refineFavoriteBuilder.create();
        refineFavoriteDialog.show();
//      END MOVIE REFINE AND FILTER
    }

    private void logoutDialog() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Logout");
        alert.show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_media_feed) {
            mBundle = new Bundle();
            mMediaTabbedFragment = new MediaTabbedFragment();

            mBundle.putString(sTypeOfBundle,sNavDrawer);
            mMediaTabbedFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mMediaTabbedFragment).commit();

        } else if (id == R.id.nav_favorites) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FavoritesFragment()).commit();

        } else if (id == R.id.nav_forum) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ForumFragment()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onListCreated(List<TVMazeResult> list) {
//
//        if (list != null){
//            searchItem = new ArrayList<>();
//
//            for (int i = 0; i < list.size(); i++) {
//                try {
//                    searchItem.add(new SearchModel(list.get(i).getShow().getName()));
//                }catch (NullPointerException e){
//                    //Do not add item to list if showname is null
//                }
//            }
//        }
//    }

    @Override
    public void onFavoriteDeleted(List<FavoriteShow> list) {
        //not used
    }

    @Override
    public void onRating(int rating) {
        //not used
    }

    @Override
    public void onReviewSaved(String review) {
        //not used
    }

    @Override
    public void onReviewReceived(String review) {
        //not used
    }

    @Override
    public void onListReceived(ArrayList<String> list) {

        if (list != null){
            searchItem = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                searchItem.add(new SearchModel(list.get(i)));
            }
        }

        new SimpleSearchDialogCompat(HomeActivity.this, "Search...", "What are you looking for...?",
                null, searchItem, new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
//                TVShowsFragment TVShowsFragment = new TVShowsFragment();
//                Bundle bundle = new Bundle();
//
//                bundle.putString("searchedItem",searchable.toString());
//                TVShowsFragment.setArguments(bundle);

                mMediaTabbedFragment = new MediaTabbedFragment();
                mBundle = new Bundle();

                mBundle.putString(sTypeOfBundle,sSearch);
                mBundle.putString("searchedItem",searchable.toString());
                mMediaTabbedFragment.setArguments(mBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mMediaTabbedFragment).commit();
                baseSearchDialogCompat.dismiss();
            }
        }).show();
    }

    @Override
    public void onReviewObjectReceived(UserReview userReview) {
        //not used
    }

    @Override
    public void onReviewListReceived(ArrayList<UserReview> list) {
        //not used
    }

    @Override
    public void onListCreated(List<TVMazeResult> list) {
        //not used
    }

    @Override
    public void fragmentVisible(Boolean visible, String tag) {

        if (visible){
            visibleFragment = tag;
            Log.i("VisibleFragment", tag + " is visible");
        }
    }
}
