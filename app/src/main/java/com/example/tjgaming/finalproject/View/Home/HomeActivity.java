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

import com.example.tjgaming.finalproject.Model.CustomStrings;
import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Authentication.LoginActivity;
import com.example.tjgaming.finalproject.View.Home.Favorites.FavoritesFragment;
import com.example.tjgaming.finalproject.View.Home.Forum.ForumFragment;
import com.example.tjgaming.finalproject.View.Home.MediaFeed.MediaFeedFragment;
import com.example.tjgaming.finalproject.View.Home.Profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DocumentReference mDocRef;
    private FirebaseAuth mFirebaseAuth;
    private User mUser;
    private TextView mUserName;
    private TextView mUserEmail;

    private String mFilterSelection;
    private String mSortSelection;
    private String mFilterField = CustomStrings.SHOW_TYPE;
    private String mSortDirection = "Descending";

    RadioButton typeBtn;
    RadioButton genreBtn;
    RadioButton sortBtn;


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

        //TODO: Get a Firebase Database Reference and get the data of the user logging in
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MediaFeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_media_feed);
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

//        } else if (id==R.id.action_search) {
//            new SimpleSearchDialogCompat<>(HomeActivity.this, "Search...", "What are you looking for...?",
//                    null, initData(), new SearchResultListener<Searchable>() {
//                @Override
//                public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
//                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
//                    baseSearchDialogCompat.dismiss();
//                }
//            }).show();
        } else if (id==R.id.action_logout) {
            logoutDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refineSearchDialog() {

        final String[] filterTypeArr = {CustomStrings.SHOW_TYPE_DEFAULT, CustomStrings.REALITY, CustomStrings.ANIMATION, CustomStrings.NEWS,
                CustomStrings.DOCUMENTARY, CustomStrings.TALK_SHOW, CustomStrings.SCRIPTED, CustomStrings.GAME_SHOW};

        final String[] filterGenreArr = {CustomStrings.SHOW_GENRE_DEFAULT, CustomStrings.COMEDY, CustomStrings.DRAMA, CustomStrings.ROMANCE,
                CustomStrings.ADVENTURE, CustomStrings.NATURE, CustomStrings.FAMILY, CustomStrings.CRIME, CustomStrings.MYSTERY, CustomStrings.SUPERNATURAL,
                CustomStrings.HORROR, CustomStrings.HISTORY, CustomStrings.SCIENCE_FICTION, CustomStrings.FANTASY, CustomStrings.THRILLER};

        final String[] sortingArr = {CustomStrings.SHOW_NAME,CustomStrings.SHOW_RATING_AVERAGE,CustomStrings.SHOW_SCHEDULE_TIME};

        AlertDialog.Builder refineBuilder = new AlertDialog.Builder(this);
        View refineDialogView = getLayoutInflater().inflate(R.layout.filter_dialog_spinner, null);
        refineBuilder.setTitle(getResources().getString(R.string.action_refine));

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
                MediaFeedFragment mediaFeedFragment = new MediaFeedFragment();
                Bundle bundle = new Bundle();

                bundle.putString("Field",mFilterField);
                bundle.putString("Value",mFilterSelection);
                bundle.putString("Order",mSortSelection);
                bundle.putString("Direction",mSortDirection);
                mediaFeedFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mediaFeedFragment).commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MediaFeedFragment()).commit();

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
}
