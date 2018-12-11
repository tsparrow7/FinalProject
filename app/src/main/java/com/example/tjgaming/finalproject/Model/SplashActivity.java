package com.example.tjgaming.finalproject.Model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.tjgaming.finalproject.View.Authentication.LoginActivity;

public class SplashActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);//This starts the activity for the splash screen so it acts like an animation.
    }
}
