package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.ajit.bjp.R;
import com.ajit.bjp.util.AppCache;


import static com.ajit.bjp.activity.SearchActivity.RESPONSE_DATA;

public class DirectoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_directory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Directory");
        findViewById(R.id.search_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA) != null) {
                    Intent intent = new Intent(DirectoryActivity.this, SearchByNameActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(DirectoryActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.search_department).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA) != null) {
                    Intent intent = new Intent(DirectoryActivity.this, SearchActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(DirectoryActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
