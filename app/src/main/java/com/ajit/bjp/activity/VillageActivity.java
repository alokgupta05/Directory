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
import com.ajit.bjp.model.ExampleVillage;
import com.ajit.bjp.model.PersonFeed;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.model.Village;
import com.ajit.bjp.model.VillageEntry;
import com.ajit.bjp.model.VillageFeed;
import com.ajit.bjp.util.AppCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VillageActivity extends AppCompatActivity {
    public static final String VILLAGE_KEY = "VILLAGE";
    public static final String RESPONSE_DATA_VILLAGE = "response_data_village";
    public static final String PLEASE_SELECT = "Please select";
    private static final String ALL_VILLAGE = "All Village";

    private String TAG = VillageActivity.class.getSimpleName();
    private AppCompatSpinner mVillageSpinner, mSchemeSpinner, mStatusSpinner;
    private ExampleVillage exampleResponse;
    private Button btnSearch;
    private TextInputLayout inputLayoutVillage;
    private SearchView mSearchView;
    private TitleListAdapter titleListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_village);
        mVillageSpinner = findViewById(R.id.village_spinner);
        mSchemeSpinner = findViewById(R.id.scheme_spinner);
        mStatusSpinner = findViewById(R.id.status_spinner);

        inputLayoutVillage = findViewById(R.id.village_input_layout);

        btnSearch = findViewById(R.id.search_button);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 searchVillage();
            }
        });
        fetchData();
        getSupportActionBar().setTitle("Villages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void searchVillage() {
        VillageFeed villageFeed = exampleResponse.getVillageFeed();
        String villageSelected =  mVillageSpinner.getSelectedItem().toString();
        String schemeSelected = mSchemeSpinner.getSelectedItem().toString();
        String statusSelected = mStatusSpinner.getSelectedItem().toString();
        List<VillageEntry> schemeList = new ArrayList<>();
        boolean notFound = true;
        if (exampleResponse != null && villageSelected!=null && !villageSelected.equalsIgnoreCase(PLEASE_SELECT) && schemeSelected!=null) {
            for (VillageEntry entry : villageFeed.getEntry()) {
                boolean isVillage = entry.getGsx$village().get$t().equalsIgnoreCase(villageSelected);

                if(ALL_VILLAGE.equalsIgnoreCase(villageSelected))
                    isVillage = true;


                if (isVillage) {
                    notFound = false;
                    if (schemeSelected.equalsIgnoreCase(PLEASE_SELECT)) {
                        Intent intentPerson = new Intent(this, SearchVillageListActivity.class);
                        intentPerson.putExtra("TYPE_LIST", villageSelected);
                        AppCache.INSTANCE.addToAppCache("VILLAGE_LIST", getVillages(villageSelected));
                        startActivity(intentPerson);
                        break;
                    } else {
                        boolean isScheme = entry.getGsx$scheme().get$t().equalsIgnoreCase(schemeSelected);
                        if (isScheme) {
                            if(statusSelected.equalsIgnoreCase(PLEASE_SELECT))
                                schemeList.add(entry);
                            else{
                                boolean isStatus = entry.getGsx$status().get$t().equalsIgnoreCase(statusSelected);
                                if(isStatus)
                                    schemeList.add(entry);

                            }
                        }

                    }
                }
            }
        }else{
            inputLayoutVillage.setError("This field is mandatory");
        }
        if(notFound)
            Toast.makeText(this,"Not Found",Toast.LENGTH_SHORT).show();
        else{
            launchSchemeList(schemeList);
        }


    }

    private void launchVillageDetails(VillageEntry entry){
        Intent intentPerson = new Intent(this, VillageDetailActivity.class);
        intentPerson.putExtra(VILLAGE_KEY, getVillageFromEntry(entry));
        startActivity(intentPerson);
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


    private void launchDialogSheet(){


        VillageFeed villageFeed = exampleResponse.getVillageFeed();
        Set<String> setVillage = new HashSet<>();
        setVillage.add(ALL_VILLAGE);
        for (VillageEntry entry : villageFeed.getEntry()) {
            if (!TextUtils.isEmpty(entry.getGsx$village().get$t()))
                setVillage.add(entry.getGsx$village().get$t());

        }
        List<String> departmentList = new ArrayList<>();
        departmentList.addAll(setVillage);


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
                populateVillage(dept);
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

    private void launchSchemeList(List<VillageEntry> entryList ){

        if(entryList.size()>1) {
            Intent intentPerson = new Intent(this, SearchVillageListActivity.class);
            String string =  entryList.get(0).getGsx$village().get$t();

            if(mVillageSpinner.getSelectedItem().toString().equalsIgnoreCase(ALL_VILLAGE))
                string = ALL_VILLAGE;
            intentPerson.putExtra("TYPE_LIST", string);
            AppCache.INSTANCE.addToAppCache("VILLAGE_LIST", entryList);
            startActivity(intentPerson);
        }else if(entryList.size()==1) {
            launchVillageDetails(entryList.get(0));
        }
    }


    public List<VillageEntry> getVillages(String village){
        VillageFeed villageFeed = exampleResponse.getVillageFeed();
        List<VillageEntry> list = new ArrayList<>();
        for (VillageEntry entry : villageFeed.getEntry()) {

            boolean isVillage = entry.getGsx$village().get$t().equalsIgnoreCase(village);
            if(isVillage || village.equalsIgnoreCase(ALL_VILLAGE)){
               list.add(entry);
            }

        }
        return list;
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
        village.remarks = entry.getGsx$remarks().get$t();

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

    private void fetchData() {
        exampleResponse = (ExampleVillage) AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA_VILLAGE);
        populatSpinnerValues();


    }

    private void populatSpinnerValues() {
        if (exampleResponse != null) {
            populateVillage(PLEASE_SELECT);

            List<String> designationList = new ArrayList<>();
            designationList.add(PLEASE_SELECT);
            SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, designationList);
            mSchemeSpinner.setAdapter(spinnerAdapterCharges);

            List<String> statusList = new ArrayList<>();
            statusList.add(PLEASE_SELECT);
            SpinnerAdapter spinnerAdapterStatus = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, statusList);
            mStatusSpinner.setAdapter(spinnerAdapterStatus);

        }
    }

    private void populateVillage(String villageName) {

        List<String> stringList = new ArrayList<>();
        stringList.add(villageName);
        SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner,stringList );
        mVillageSpinner.setAdapter(spinnerAdapterCharges);

        mVillageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputLayoutVillage.setError(null);
                if(mVillageSpinner.getSelectedItem()!=null
                        && !TextUtils.isEmpty(mVillageSpinner.getSelectedItem().toString())
                        && (mVillageSpinner.getSelectedItem().toString().equalsIgnoreCase(ALL_VILLAGE) ||!mVillageSpinner.getSelectedItem().toString().equalsIgnoreCase(PLEASE_SELECT))) {
                    populateSchemes();
                    populateStatus();
                }/*else{
                    inputLayoutVillage.setError("This field is mandatory");
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mVillageSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    launchDialogSheet();
                }

                return true;
            }
        });
    }

    private void populateSchemes() {
        VillageFeed villageFeed = exampleResponse.getVillageFeed();
        Set<String> setScheme = new HashSet<>();
        String villageSelected = PLEASE_SELECT;

        if(mVillageSpinner.getSelectedItem()!=null)
            villageSelected =  mVillageSpinner.getSelectedItem().toString();


        if(villageSelected.equalsIgnoreCase(ALL_VILLAGE) || !villageSelected.equalsIgnoreCase(PLEASE_SELECT)){
            for (VillageEntry entry : villageFeed.getEntry()) {
                if (!TextUtils.isEmpty(entry.getGsx$scheme().get$t()) && (villageSelected.equalsIgnoreCase(ALL_VILLAGE) || entry.getGsx$village().get$t().equalsIgnoreCase(villageSelected)))
                    setScheme.add(entry.getGsx$scheme().get$t());

            }
        }

        List<String> stringList = new ArrayList<>();
        stringList.add(PLEASE_SELECT);
        stringList.addAll(setScheme);
        SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, stringList);
        mSchemeSpinner.setAdapter(spinnerAdapterCharges);

        mSchemeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputLayoutVillage.setError(null);
                if(mSchemeSpinner.getSelectedItem()!=null
                        && !TextUtils.isEmpty(mSchemeSpinner.getSelectedItem().toString())
                        && !mSchemeSpinner.getSelectedItem().toString().equalsIgnoreCase(PLEASE_SELECT)) {
                    populateStatus();
                }/*else{
                    inputLayoutVillage.setError("This field is mandatory");
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void populateStatus() {
        VillageFeed villageFeed = exampleResponse.getVillageFeed();
        Set<String> setStatus = new HashSet<>();



        String schemeSelected = "";
        String villageSelected = "";

        if(mSchemeSpinner.getSelectedItem()!=null)
            schemeSelected =  mSchemeSpinner.getSelectedItem().toString();


        if(mVillageSpinner.getSelectedItem()!=null)
            villageSelected =  mVillageSpinner.getSelectedItem().toString();

        if( !schemeSelected.equalsIgnoreCase(PLEASE_SELECT)
                 && ( villageSelected.equalsIgnoreCase(ALL_VILLAGE) || !villageSelected.equalsIgnoreCase(PLEASE_SELECT))){
            for (VillageEntry entry : villageFeed.getEntry()) {
                if (!TextUtils.isEmpty(entry.getGsx$status().get$t()) && entry.getGsx$scheme().get$t().equalsIgnoreCase(schemeSelected)
                         && (villageSelected.equalsIgnoreCase(ALL_VILLAGE) ||entry.getGsx$village().get$t().equalsIgnoreCase(villageSelected)))
                    setStatus.add(entry.getGsx$status().get$t());

            }
        }

        List<String> stringList = new ArrayList<>();
        stringList.add(PLEASE_SELECT);
        stringList.addAll(setStatus);
        SpinnerAdapter spinnerAdapterCharges = new ArrayAdapter<String>(this, R.layout.row_spinner, R.id.textview_spinner, stringList);
        mStatusSpinner.setAdapter(spinnerAdapterCharges);


    }



}
