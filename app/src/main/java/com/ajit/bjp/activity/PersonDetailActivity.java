package com.ajit.bjp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ajit.bjp.activity.SearchActivity.PERSON_KEY;

public class PersonDetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtMobileNo;
    private TextView txtOfficeNo;
    private TextView txtHomeNo;
    private TextView txtFloor;
    private TextView txtDeskNo;
    private TextView txtEmail;
    private TextView txtDept;
    private TextView txtDesignation;
    private TextView txtCharge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_person);

        txtName = findViewById(R.id.text_view_name);
        txtMobileNo = findViewById(R.id.text_view_mobile);
        txtOfficeNo = findViewById(R.id.text_view_office);
        txtHomeNo = findViewById(R.id.text_view_homeno);
        txtFloor = findViewById(R.id.text_view_floor);
        txtDeskNo = findViewById(R.id.text_view_desk_no);
        txtEmail = findViewById(R.id.text_view_email);
        txtDept = findViewById(R.id.text_view_department);
        txtDesignation = findViewById(R.id.text_view_designation);
        txtCharge = findViewById(R.id.text_view_charge);

        ImageView imgMobile = findViewById(R.id.img_view_mobile);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");
        setPersonDetails();

        imgMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                // Set the data for the intent as the phone number.
                smsIntent.setData(Uri.parse("smsto:"+txtMobileNo.getText().toString()));
                // Add the message (sms) with the key ("sms_body").
                // If package resolves (target app installed), send intent.
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Log.d("TAG", "Can't resolve app for ACTION_SENDTO Intent");
                }
            }
        });


    }


    private void setPersonDetails(){

        if(getIntent() != null && getIntent().getParcelableExtra(PERSON_KEY)!=null){
            Person person = getIntent().getParcelableExtra(PERSON_KEY);
            txtName.setText(person.getNamePerson());
            txtMobileNo.setText(person.getMobileNo());
            txtOfficeNo.setText(getStringNumber(person.getOfficeNo()));
            txtHomeNo.setText(getStringNumber(person.getHomeNo()));
            txtFloor.setText(person.getFloor());
            txtDeskNo.setText(getStringNumber(person.getDeskNo()));
            txtEmail.setText(person.getEmail());
            txtCharge.setText(person.getCharge());
            txtDept.setText(person.getDept());

            txtDesignation.setText(person.getDesignation());

            if(TextUtils.isEmpty(person.getMobileNo())){
                txtMobileNo.setText("");
                findViewById(R.id.img_view_mobile).setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(person.getOfficeNo())){
                txtOfficeNo.setText("");
            }
            if(TextUtils.isEmpty(person.getHomeNo())){
                txtHomeNo.setText("");
            }
            if(TextUtils.isEmpty(person.getDeskNo())){
                txtDeskNo.setText("");
            }
            if(TextUtils.isEmpty(person.getEmail())){
                txtEmail.setVisibility(View.GONE);
            }
        }
    }

    private String getStringNumber(String mobile){

        if(mobile!=null){
            mobile = mobile.replace("\n","");
            mobile = mobile.replace(" ","");
            List<String> numberList = Arrays.asList(mobile.split(";"));
            mobile="";
            for (String number :numberList) {
                mobile = mobile + number +"\n";

            }
            return mobile;
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
