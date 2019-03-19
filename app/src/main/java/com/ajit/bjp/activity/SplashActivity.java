package com.ajit.bjp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static com.ajit.bjp.activity.LoginActivity.LOGIN;
import static com.ajit.bjp.activity.LoginActivity.MY_PREFS_NAME;

public class SplashActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean loginStatus = prefs.getBoolean(LOGIN, false);

        if(loginStatus)
        {
            Intent homeIntent = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(homeIntent);
            finish();

        }else{
            Intent homeIntent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }
}
