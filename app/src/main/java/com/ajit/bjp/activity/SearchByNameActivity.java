package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.PersonListAdapter;
import com.ajit.bjp.model.Entry;
import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.PersonFeed;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.util.AppCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ajit.bjp.activity.SearchActivity.PERSON_KEY;
import static com.ajit.bjp.activity.SearchActivity.RESPONSE_DATA;

public class SearchByNameActivity extends AppCompatActivity implements PersonListAdapter.OnClickPerson {

    private RecyclerView listViewPersonName;
    private SearchView mSearchView;
    private PersonListAdapter personListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewPersonName = findViewById(R.id.list_view_name);
         personListAdapter = new PersonListAdapter(setData(),this);
        personListAdapter.setOnClickPerson(this);
        listViewPersonName.setItemAnimator(new DefaultItemAnimator());
        listViewPersonName.setLayoutManager(new LinearLayoutManager(this));
        listViewPersonName.setAdapter(personListAdapter);

        listViewPersonName.addItemDecoration(new DividerItemDecoration(listViewPersonName.getContext(), DividerItemDecoration.VERTICAL));
        mSearchView = findViewById(R.id.search_view);
        mSearchView.clearFocus();
        mSearchView.setFocusable(false);
        mSearchView.findViewById(R.id.search_close_btn)
                .setOnClickListener(
                        (View v) -> {
                            mSearchView.setQuery(null, false);

                        });
        getSupportActionBar().setTitle("Search By Name");

        handleSearchTextEntry();

    }

    private void handleSearchTextEntry() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >0) {
                    mSearchView.clearFocus();
                    personListAdapter.getFilter().filter(query);
                    return true;
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String submitText) {
                personListAdapter.getFilter().filter(submitText);
                return true;
            }
        });
    }

    private List<Person> setData(){
        Example exampleResponse = (Example) AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA);
        PersonFeed personFeed = exampleResponse.getPersonFeed();
        List<Person> personList = new ArrayList<>();
        if (exampleResponse != null) {
            for (Entry entry : personFeed.getEntry()) {
                if(!TextUtils.isEmpty(entry.getGsx$name().get$t()))
                    personList.add(getPersonFromEntry(entry));
            }


            Collections.sort(personList, new Comparator<Person>() {
                public int compare(Person v1, Person v2) {
                    return v1.getNamePerson().compareTo(v2.getNamePerson());
                }
            });
        }



        return personList;
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
    public void onPersonClicked(Person person) {
        Intent intentPerson = new Intent(this,PersonDetailActivity.class);
        intentPerson.putExtra(PERSON_KEY,person);
        startActivity(intentPerson);
    }
}
