package com.example.tjgaming.finalproject.View.Registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.tjgaming.finalproject.R;

public class MediaSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    private CheckBox mMovieCheckBox;
    private CheckBox mTVCheckBox;
    private CheckBox mMusicCheckBox;
    private CheckBox mGamesCheckBox;
    private AppCompatButton mFinishButton;

    private boolean mMovies;
    private boolean mTv;
    private boolean mMusic;
    private boolean mGames;

    //From intent
    private String mUid;
    private String mEmail;
    private String mGender;
    private String mBirthDate;
    private String mNotifications;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_selection);

        initializeViews();
        getDataFromIntent();
        setFinishListener();
    }

    private void initializeViews() {

        mMovies = mTv = mMusic = mGames = false;

        ( mMovieCheckBox = findViewById(R.id.media_select_movie_checkbox) )
                .setOnClickListener(this);
        ( mTVCheckBox = findViewById(R.id.media_select_tv_checkbox) )
                .setOnClickListener(this);
        ( mMusicCheckBox = findViewById(R.id.media_select_music_checkbox) )
                .setOnClickListener(this);
        ( mGamesCheckBox = findViewById(R.id.media_select_games_checkbox) )
                .setOnClickListener(this);

        mFinishButton = findViewById(R.id.btn_media_select_finish);
    }

    private void getDataFromIntent() {
        mUid = getIntent().getStringExtra("uid");
        mEmail = getIntent().getStringExtra("email");
        mGender = getIntent().getStringExtra("gender");
        mBirthDate = getIntent().getStringExtra("dateOfBirth");
        mUserName = getIntent().getStringExtra("username");
        mNotifications = getIntent().getStringExtra("notifications");
    }

    private void setFinishListener() {
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovies == false && mTv == false && mMusic == false && mGames == false){
                    Toast.makeText(MediaSelectionActivity.this, "Please select at least one type of media.", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Create user object to store in database

                    //TODO: Start home activity intent
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.media_select_movie_checkbox:

                if(mMovies)
                    mMovies = false;
                else
                    mMovies = true;
                break;

            case R.id.media_select_tv_checkbox:

                if(mTv)
                    mTv = false;
                else
                    mTv = true;
                break;

            case R.id.media_select_music_checkbox:

                if(mMusic)
                    mMusic = false;
                else
                    mMusic = true;
                break;
            case R.id.media_select_games_checkbox:

                if(mGames)
                    mGames = false;
                else
                    mGames = true;
                break;
        }

    }
}
