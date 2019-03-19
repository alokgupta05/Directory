package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.DepartmentListAdapter;
import com.ajit.bjp.adapter.VillageListAdapter;
import com.ajit.bjp.model.Entry;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.model.Village;
import com.ajit.bjp.model.VillageEntry;
import com.ajit.bjp.util.AppCache;

import java.util.List;

import static com.ajit.bjp.activity.SearchActivity.PERSON_KEY;
import static com.ajit.bjp.activity.VillageActivity.VILLAGE_KEY;

public class SearchPersonListActivity extends AppCompatActivity implements DepartmentListAdapter.OnClickPerson {

    private RecyclerView listViewPersonName;
    private SearchView mSearchView;
    private DepartmentListAdapter personListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewPersonName = findViewById(R.id.list_view_name);
         personListAdapter = new DepartmentListAdapter(setData(),this);
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

        String title = getIntent().getStringExtra("TYPE_LIST");
        getSupportActionBar().setTitle(title);


    }




    private List<Entry> setData(){
        List<Entry> villageList = (List<Entry>) AppCache.INSTANCE.getValueOfAppCache("PERSON_LIST");
        return villageList;
    }




    private Person getPersonFromEntry(Entry entry){

        Person person = new Person();
        person.setDeskNo(entry.getGsx$deskno().get$t());
        person.setFloor(entry.getGsx$floor().get$t());
        person.setHomeNo(entry.getGsx$homeno().get$t());
        person.setMobileNo(entry.getGsx$mobileno().get$t());
        person.setNamePerson(entry.getGsx$name().get$t());
        person.setOfficeNo(entry.getGsx$officeno().get$t());
        person.setEmail(entry.getGsx$email().get$t());
        person.setCharge(entry.getGsx$charge().get$t());
        person.setDept(entry.getGsx$department().get$t());
        person.setDesignation(entry.getGsx$designation().get$t());

        return person;

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
    public void onPersonClicked(Entry person) {
        Intent intentPerson = new Intent(this, PersonDetailActivity.class);
        intentPerson.putExtra(PERSON_KEY, getPersonFromEntry(person));
        startActivity(intentPerson);
    }
}
