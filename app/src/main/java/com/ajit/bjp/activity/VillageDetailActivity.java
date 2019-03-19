package com.ajit.bjp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.model.Village;

import static com.ajit.bjp.activity.SearchActivity.PERSON_KEY;
import static com.ajit.bjp.activity.VillageActivity.VILLAGE_KEY;

public class VillageDetailActivity extends AppCompatActivity {

    private TextView txtVillageName;
    private TextView txtScheme;
    private TextView txtYear;
    private TextView txtDetails;
    private TextView txtSancAmt;
    private TextView txtDistance;
    private TextView txtStatus,txtRemarks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_village_detail);

        txtVillageName = findViewById(R.id.text_view_village_name);
        txtScheme = findViewById(R.id.text_view_scheme);
        txtYear = findViewById(R.id.text_view_year);
        txtDetails = findViewById(R.id.text_view_details);
        txtSancAmt = findViewById(R.id.text_view_sanctioned_amt);
        txtDistance = findViewById(R.id.text_view_distance);
        txtStatus = findViewById(R.id.text_view_status);
        txtRemarks =  findViewById(R.id.text_view_remarks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");
        setVillageDetails();

    }

    private void setVillageDetails(){

        if(getIntent() != null && getIntent().getParcelableExtra(VILLAGE_KEY)!=null){
            Village village = getIntent().getParcelableExtra(VILLAGE_KEY);
            txtVillageName.setText(village.villageName);
            txtScheme.setText(village.scheme);
            txtYear.setText(village.year);
            txtDetails.setText(village.details);
            txtSancAmt.setText(village.sanctionedAmt);
            txtDistance.setText(village.distances);
            txtStatus.setText(village.statusVillage);
            txtRemarks.setText(village.remarks);
        }
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
