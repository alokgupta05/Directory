package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.TitleListAdapter;
import com.ajit.bjp.model.Entry;
import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.PersonFeed;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.util.AppCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ajit.bjp.activity.VillageActivity.PLEASE_SELECT;


public class SearchActivity extends AppCompatActivity {
    public static final String PERSON_KEY = "PERSON";
    public static final String RESPONSE_DATA = "response_data_person";

    private String TAG = SearchActivity.class.getSimpleName();
    private AppCompatSpinner mDepartmentSpinner, mDesignationSpinner;
    private TextInputLayout mDepartmentInputLayout,mChargeInputLayout,mDesignationInputLayout;
    private Example exampleResponse;
    private Button btnSearch;
    private SearchView mSearchView;
    private TitleListAdapter titleListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        mDepartmentSpinner = findViewById(R.id.department_spinner);
        mDesignationSpinner = findViewById(R.id.designation_spinner);
       // mChargeSpinner = findViewById(R.id.charge_spinner);
        btnSearch = findViewById(R.id.search_button);

        mDepartmentInputLayout = findViewById(R.id.input_layout_department);
        mDesignationInputLayout = findViewById(R.id.input_layout_designation);
        mChargeInputLayout = findViewById(R.id.input_layout_charge);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPerson();
            }
        });
        fetchData();
       // getSupportActionBar().setTitle("Search Directory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search By Department");

    }

    private void handleSearchTextEntry() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >0) {
                    mSearchView.clearFocus();
                    titleListAdapter.getFilter().filter(query);
                    return true;
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String submitText) {
                titleListAdapter.getFilter().filter(submitText);
                return true;
            }
        });
    }

    private void searchPerson() {
        PersonFeed personFeed = exampleResponse.getPersonFeed();
        String departmentSelected =  mDepartmentSpinner.getSelectedItem().toString();
        String designationSelected = mDesignationSpinner.getSelectedItem().toString();
       // String chargeSelected = mChargeSpinner.getSelectedItem().toString();
        boolean notFound = true;
        List<Entry> chargeList = new ArrayList<>();

        if (exampleResponse != null && !departmentSelected.equalsIgnoreCase(PLEASE_SELECT)) {
            for (Entry entry : personFeed.getEntry()) {

                boolean isDepartment = entry.getGsx$department().get$t().equalsIgnoreCase(departmentSelected);


                if(isDepartment)    {
                    notFound = false;
                    if( designationSelected.equalsIgnoreCase(PLEASE_SELECT)){
                        Intent intentPerson = new Intent(this, SearchPersonListActivity.class);
                         intentPerson.putExtra("TYPE_LIST",departmentSelected);
                        AppCache.INSTANCE.addToAppCache("PERSON_LIST",getPersonList(departmentSelected));
                        startActivity(intentPerson);
                        break;
                    }else {
                        boolean isDesignation = entry.getGsx$designation().get$t().equalsIgnoreCase(designationSelected);
                        if(isDesignation) {
                            chargeList.add(entry);
                        }
                    }
                }

            }
        }else{
            mDepartmentInputLayout.setError("This field is mandatory");
        }
        if(notFound)
            Toast.makeText(this,"Not Found",Toast.LENGTH_SHORT).show();
        else {
            launchChargeList(chargeList);
        }


    }

    private void launchChargeList(List<Entry> entryList){

        if(entryList.size()>1) {
            Intent intentPerson = new Intent(this, SearchPersonListActivity.class);
            intentPerson.putExtra("TYPE_LIST",entryList.get(0).getGsx$department().get$t());
            AppCache.INSTANCE.addToAppCache("PERSON_LIST", entryList);
            startActivity(intentPerson);
        }else if(entryList.size()==1) {
            launchPersonDetail(entryList.get(0));
        }
    }

    private void launchPersonDetail(Entry entry){
        Intent intentPerson = new Intent(this, PersonDetailActivity.class);
        intentPerson.putExtra(PERSON_KEY, getPersonFromEntry(entry));
        startActivity(intentPerson);
    }

    public List<Entry> getPersonList(String department){
        PersonFeed personFeed = exampleResponse.getPersonFeed();
        List<Entry> entryList = new ArrayList<>();
        for (Entry entry: personFeed.getEntry()){
            if(entry.getGsx$department().get$t().equalsIgnoreCase(department))
                entryList.add(entry);
        }
        return entryList;
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

    private void fetchData() {
        exampleResponse = (Example) AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA);
        populatSpinnerValues();


    }

    private void launchDialogSheet(){


        PersonFeed personFeed = exampleResponse.getPersonFeed();
        Set<String> setDepartment = new HashSet<>();
        for (Entry entry : personFeed.getEntry()) {
            if (!TextUtils.isEmpty(entry.getGsx$department().get$t()))
                setDepartment.add(entry.getGsx$department().get$t());

        }
        List<String> departmentList = new ArrayList<>();
        departmentList.addAll(setDepartment);


        Collections.sort(departmentList, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareTo(v2);
            }
        });
        CustomBottomSheetDialog customBottomSheetDialog = new CustomBottomSheetDialog();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_department_search,null);

        mSearchView = view.findViewById(R.id.search_view);
        // mSearchView.setOnClickListener(this);
        mSearchView.clearFocus();
        mSearchView.setFocusable(true);
        mSearchView.findViewById(R.id.search_close_btn)
                .setOnClickListener(
                        (View v) -> {
                            mSearchView.setQuery(null, false);

                        });


        RecyclerView recyclerView = view.findViewById(R.id.nationality_selection_list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        titleListAdapter = new TitleListAdapter(this,departmentList);
        titleListAdapter.setOnClickDepartment(new TitleListAdapter.OnClickDepartment() {
            @Override
            public void onDepartmentClicked(String dept) {
                populateDepartments(dept);
                customBottomSheetDialog.collapseAndDismissFragment();
            }
        });
        recyclerView.setAdapter(titleListAdapter);
        handleSearchTextEntry();

        customBottomSheetDialog.setCustomView(view);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment previousInstance = getSupportFragmentManager().findFragmentByTag(customBottomSheetDialog.getFragmentTag());
        if (previousInstance != null) {
            fragmentTransaction.remove(previousInstance);
        }
        fragmentTransaction.add(customBottomSheetDialog, customBottomSheetDialog.getFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void populatSpinnerValues() {
        if (exampleResponse != null) {
            populateDepartments(PLEASE_SELECT);

            List<String> designationList = new ArrayList<>();
            designationList.add(PLEASE_SELECT);
            SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, designationList);
            mDesignationSpinner.setAdapter(spinnerAdapterCharges);
        }
    }


    private void populateDesignation() {


        String departmentSelected = null;

        if(mDepartmentSpinner.getSelectedItem()!=null)
                    departmentSelected =  mDepartmentSpinner.getSelectedItem().toString();

        PersonFeed personFeed = exampleResponse.getPersonFeed();
        Set<String> setDesignation = new HashSet<>();
        for (Entry entry : personFeed.getEntry()) {
            if (!TextUtils.isEmpty(entry.getGsx$designation().get$t()))
                setDesignation.add(entry.getGsx$designation().get$t());

        }

        if(departmentSelected!=null && !departmentSelected.equalsIgnoreCase(PLEASE_SELECT)){
            setDesignation = new HashSet<>();
            for (Entry entry : personFeed.getEntry()) {
                if (!TextUtils.isEmpty(entry.getGsx$designation().get$t()) && entry.getGsx$department().get$t().equalsIgnoreCase(departmentSelected))
                    setDesignation.add(entry.getGsx$designation().get$t());

            }
        }
        List<String> designationList = new ArrayList<>();
        designationList.add(PLEASE_SELECT);
        designationList.addAll(setDesignation);

        SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, designationList);
        mDesignationSpinner.setAdapter(spinnerAdapterCharges);
    }

    private void populateDepartments(String text) {


        List<String> list= new ArrayList<>();
        list.add(text);
        SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, list);
        mDepartmentSpinner.setAdapter(spinnerAdapterCharges);

        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDepartmentInputLayout.setError(null);

                if(mDepartmentSpinner.getSelectedItem()!=null
                        && !TextUtils.isEmpty(mDepartmentSpinner.getSelectedItem().toString())
                        && !mDepartmentSpinner.getSelectedItem().toString().equalsIgnoreCase(PLEASE_SELECT))
                populateDesignation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDepartmentSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    launchDialogSheet();
                }

                return true;
            }
        });
    }



}
