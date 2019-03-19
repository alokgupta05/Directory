package com.ajit.bjp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.ajit.bjp.R;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "pref_name";
    public static final String LOGIN = "LOGIN";
    private TextInputLayout mTextLayoutPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextLayoutPassword = findViewById(R.id.password_layout);
        btnSubmit = findViewById(R.id.submit_login);
        getSupportActionBar().setTitle("Login");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mTextLayoutPassword.getEditText().getText().toString().trim())){
                    mTextLayoutPassword.setError("Please enter the password");
                }else if(!mTextLayoutPassword.getEditText().getText().toString().trim().toString().equalsIgnoreCase("123456")){
                    mTextLayoutPassword.setError("Invalid Password");
                }else{
                    //Save successful login to preferences
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(LOGIN, true);
                    editor.apply();

                    Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        });
    }
}
