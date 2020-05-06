package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.VillageListAdapter;
import com.ajit.bjp.model.Village;
import com.ajit.bjp.model.VillageEntry;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppUtils;

import java.util.List;

import static com.ajit.bjp.activity.VillageActivity.VILLAGE_KEY;

public class SearchVillageListActivity extends AppCompatActivity implements VillageListAdapter.OnClickPerson {

    private RecyclerView listViewPersonName;
    private SearchView mSearchView;
    private VillageListAdapter personListAdapter;
    private Menu mMenu;
    private List<VillageEntry> villageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setData();
        listViewPersonName = findViewById(R.id.list_view_name);
        personListAdapter = new VillageListAdapter(villageList);
        personListAdapter.setOnClickPerson(this);
        listViewPersonName.setItemAnimator(new DefaultItemAnimator());
        listViewPersonName.setLayoutManager(new LinearLayoutManager(this));
        listViewPersonName.setAdapter(personListAdapter);
//        listViewPersonName.addItemDecoration(new DividerItemDecoration(listViewPersonName.getContext(), DividerItemDecoration.VERTICAL));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_karyakarta, menu);
        mMenu = menu;
        menu.findItem(R.id.menuSearch).setVisible(false);
        menu.findItem(R.id.menuShare).setVisible(false);
        menu.findItem(R.id.menuClose).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menuCheckBox:
                if(villageList != null) {
                    mMenu.findItem(R.id.menuSearch).setVisible(false);
                    mMenu.findItem(R.id.menuCheckBox).setVisible(false);
                    mMenu.findItem(R.id.menuShare).setVisible(true);
                    mMenu.findItem(R.id.menuClose).setVisible(true);

                    personListAdapter.isMultipleSharing(true);
                }
                return true;

            case R.id.menuShare:
                List<VillageEntry> sharingList = personListAdapter.getSharingList();
                if(!sharingList.isEmpty()) {
                    shareMultipleContacts(sharingList);

                    mMenu.findItem(R.id.menuSearch).setVisible(true);
                    mMenu.findItem(R.id.menuCheckBox).setVisible(true);
                    mMenu.findItem(R.id.menuShare).setVisible(false);
                    mMenu.findItem(R.id.menuClose).setVisible(false);

                    personListAdapter.isMultipleSharing(false);
                }
                return true;

            case R.id.menuClose:
                mMenu.findItem(R.id.menuSearch).setVisible(true);
                mMenu.findItem(R.id.menuCheckBox).setVisible(true);
                mMenu.findItem(R.id.menuShare).setVisible(false);
                mMenu.findItem(R.id.menuClose).setVisible(false);

                personListAdapter.isMultipleSharing(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData(){
        villageList = (List<VillageEntry>) AppCache.INSTANCE.getValueOfAppCache("VILLAGE_LIST");
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
    public void onPersonClicked(VillageEntry person) {
        Intent intentPerson = new Intent(this, VillageDetailActivity.class);
        intentPerson.putExtra(VILLAGE_KEY, getVillageFromEntry(person));
        startActivity(intentPerson);
    }

    @Override
    public void onVillageDetailsShareClick(String details) {
        AppUtils.shareContent(SearchVillageListActivity.this, details);
    }

    private void shareMultipleContacts(List<VillageEntry> sharingList) {
        String content = "";
        for (int i = 0; i < sharingList.size(); i++) {
            VillageEntry villageEntry = sharingList.get(i);
            content = content.concat(Integer.toString(i + 1)).concat(")\n")
                    .concat("Village Name: ").concat(villageEntry.getGsx$village().get$t()).concat("\n")
                    .concat("Details: ").concat(villageEntry.getGsx$details().get$t()).concat("\n")
                    .concat("Scheme: ").concat(villageEntry.getGsx$scheme().get$t()).concat("\n")
                    .concat("Status: ").concat(villageEntry.getGsx$status().get$t()).concat("\n")
                    .concat("Sanctioned Amount :").concat(villageEntry.getGsx$sanctionedamount().get$t()).concat("\n\n");
        }

        AppUtils.shareContent(this, content);
    }
}
