package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.PersonListAdapter;
import com.ajit.bjp.adapter.VillageListAdapter;
import com.ajit.bjp.model.Entry;
import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.model.PersonFeed;
import com.ajit.bjp.model.Village;
import com.ajit.bjp.model.VillageEntry;
import com.ajit.bjp.util.AppCache;

import java.util.ArrayList;
import java.util.List;

import static com.ajit.bjp.activity.SearchActivity.PERSON_KEY;
import static com.ajit.bjp.activity.SearchActivity.RESPONSE_DATA;
import static com.ajit.bjp.activity.VillageActivity.VILLAGE_KEY;

public class SearchVillageListActivity extends AppCompatActivity implements VillageListAdapter.OnClickPerson {

    private RecyclerView listViewPersonName;
    private SearchView mSearchView;
    private VillageListAdapter personListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewPersonName = findViewById(R.id.list_view_name);
         personListAdapter = new VillageListAdapter(setData());
        personListAdapter.setOnClickPerson(this);
        listViewPersonName.setItemAnimator(new DefaultItemAnimator());
        listViewPersonName.setLayoutManager(new LinearLayoutManager(this));
        listViewPersonName.setAdapter(personListAdapter);
        listViewPersonName.addItemDecoration(new DividerItemDecoration(listViewPersonName.getContext(), DividerItemDecoration.VERTICAL));
        mSearchView = findViewById(R.id.search_view);
       // mSearchView.setOnClickListener(this);
        mSearchView.clearFocus();
        mSearchView.setFocusable(false);
        mSearchView.setVisibility(View.GONE);
        mSearchView.findViewById(R.id.search_close_btn)
                .setOnClickListener(
                        (View v) -> {
                            mSearchView.setQuery(null, false);

                        });
        String title = getIntent().getStringExtra("TYPE_LIST");
        getSupportActionBar().setTitle(title);


    }


    private List<VillageEntry> setData(){
        List<VillageEntry> villageList = (List<VillageEntry>) AppCache.INSTANCE.getValueOfAppCache("VILLAGE_LIST");
        return villageList;
    }




    private Village getVillageFromEntry(VillageEntry entry){

        Village village = new Village();
        village.villageName = entry.getGsx$village().get$t();
        village.scheme = entry.getGsx$scheme().get$t();
        village.details = entry.getGsx$details().get$t();
        village.sanctionedAmt = entry.getGsx$sanctionedamount().get$t();
        village.statusVillage = entry.getGsx$status().get$t();
        village.distances = entry.getGsx$distance().get$t();
        village.year = entry.getGsx$year().get$t();
        village.remarks =entry.getGsx$remarks().get$t();
        return village;

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

    @Override
    public void onPersonClicked(VillageEntry person) {
        Intent intentPerson = new Intent(this, VillageDetailActivity.class);
        intentPerson.putExtra(VILLAGE_KEY, getVillageFromEntry(person));
        startActivity(intentPerson);
    }
}
