package com.example.tjgaming.finalproject.View.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.tjgaming.finalproject.View.Home.FavoritesFragment;
import com.example.tjgaming.finalproject.View.Home.ForumFragment;
import com.example.tjgaming.finalproject.View.Home.MediaFeedFragment;
import com.example.tjgaming.finalproject.View.Home.ProfileFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DocumentReference mDocRef;
    private FirebaseAuth mFirebaseAuth;
    private User mUser;
    private TextView mUserName;
    private TextView mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mUserName = (TextView)headerView.findViewById(R.id.nav_header_user_name);
        mUserEmail = (TextView)headerView.findViewById(R.id.nav_header_user_email);
        ImageView mProfileImage = (ImageView)headerView.findViewById(R.id.nav_header_profile_image);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
