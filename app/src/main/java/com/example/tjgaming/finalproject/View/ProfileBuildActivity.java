package com.example.tjgaming.finalproject.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tjgaming.finalproject.R;

public class ProfileBuildActivity extends AppCompatActivity {

    private String mUid;
    private String mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_build);

        mUid = getIntent().getStringExtra("uid");
        mEmail = getIntent().getStringExtra("email");

    }
}
