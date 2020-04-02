package com.ajit.bjp.activity.karyakarta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ajit.bjp.R;

public class KaryakartaBirthdayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyakarta_birthday);

        getSupportActionBar().setTitle("Birthdays");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
