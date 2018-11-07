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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.CustomStrings;
import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Authentication.LoginActivity;
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
            //TODO:Open up custom dialog to sort, filter, and order data in media feed fragment
            //TODO:Get the results of selections and add them to bundle and send them to fragment
            sortDialog();

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

    private void sortDialog() {
        final String[] sorting = {CustomStrings.SHOW_NAME,CustomStrings.SHOW_RATING_AVERAGE,CustomStrings.SHOW_SCHEDULE_TIME};
        AlertDialog.Builder sortBuilder = new AlertDialog.Builder(this);
        sortBuilder.setTitle("Sort");
        sortBuilder.setSingleChoiceItems(sorting, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSortSelection = sorting[which];
                    }
                });

        sortBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterDialog();
            }
        });

        sortBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog sortDialog = sortBuilder.create();
        sortDialog.show();

    }

    public void filterDialog() {
        final String[] filterArr = {CustomStrings.REALITY, CustomStrings.ANIMATION, CustomStrings.NEWS,
                                 CustomStrings.DOCUMENTARY, CustomStrings.TALK_SHOW, CustomStrings.SCRIPTED, CustomStrings.GAME_SHOW};

        AlertDialog.Builder filterBuilder = new AlertDialog.Builder(this);
        filterBuilder.setTitle("Filter");
        filterBuilder.setSingleChoiceItems(filterArr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFilterSelection = filterArr[which];
            }
        });

        filterBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

        filterBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog filterDialog = filterBuilder.create();
        filterDialog.show();

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
