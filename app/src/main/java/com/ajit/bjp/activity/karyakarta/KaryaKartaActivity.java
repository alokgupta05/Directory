package com.ajit.bjp.activity.karyakarta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.activity.CustomBottomSheetDialog;
import com.ajit.bjp.adapter.KaryakarteListAdapter;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;
import com.ajit.bjp.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class KaryaKartaActivity extends AppCompatActivity {

    private Disposable disposable;
    private APIService mAPIService;

    private ProgressBar prgCircular;
    private RecyclerView listView;
    private SearchView searchView;
    private Menu mMenu;
    private KaryakarteListAdapter mAdapter;
    private List<String> mHeaders;
    private List<KaryaKarta> mKaryakartaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_name);

        getSupportActionBar().setTitle("Karyakarte");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAPIService = APIUtils.getAPIService();

        prgCircular = findViewById(R.id.progress_circular);
        listView = findViewById(R.id.list_view_name);
        listView.setLayoutManager(new LinearLayoutManager(this));

        getKaryaKartaList();

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >0) {
                    searchView.clearFocus();
                    mAdapter.getFilter().filter(query);
                    return true;
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String submitText) {
                mAdapter.getFilter().filter(submitText);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_karyakarta, menu);
        mMenu = menu;
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

            case R.id.menuSearch:
                if(mKaryakartaList != null)
                    openSearchOption();
                return true;

            case R.id.menuCheckBox:
                if(mKaryakartaList != null) {
                    mMenu.findItem(R.id.menuSearch).setVisible(false);
                    mMenu.findItem(R.id.menuCheckBox).setVisible(false);
                    mMenu.findItem(R.id.menuShare).setVisible(true);
                    mMenu.findItem(R.id.menuClose).setVisible(true);

                    mAdapter.isMultipleSharing(true);
                }
                return true;

            case R.id.menuShare:
                List<KaryaKarta> sharingList = mAdapter.getSharingList();
                if(!sharingList.isEmpty()) {
                    shareMultipleContacts(sharingList);

                    mMenu.findItem(R.id.menuSearch).setVisible(true);
                    mMenu.findItem(R.id.menuCheckBox).setVisible(true);
                    mMenu.findItem(R.id.menuShare).setVisible(false);
                    mMenu.findItem(R.id.menuClose).setVisible(false);

                    mAdapter.isMultipleSharing(false);
                }
                return true;

            case R.id.menuClose:
                mMenu.findItem(R.id.menuSearch).setVisible(true);
                mMenu.findItem(R.id.menuCheckBox).setVisible(true);
                mMenu.findItem(R.id.menuShare).setVisible(false);
                mMenu.findItem(R.id.menuClose).setVisible(false);

                mAdapter.isMultipleSharing(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getKaryaKartaList() {

        if(AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST) != null) {
            setDataToAdapter((List<KaryaKarta>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST));

        } else {
            if(disposable!=null  && !disposable.isDisposed()){
                disposable.dispose();
            }

            prgCircular.setVisibility(View.VISIBLE);
            disposable = mAPIService.getKaryakartaData("json").subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( exampleKarykarta -> {
                        setDataToAdapter(AppUtils.createKaryakartaList(exampleKarykarta.getFeed()));
                        prgCircular.setVisibility(View.GONE);

                    }, throwable ->
                    {
                        prgCircular.setVisibility(View.GONE);
                        Log.e(KaryaKartaActivity.class.getName(), "Unable to submit post to API." + throwable.getMessage());
                    });
        }

    }

    private void setDataToAdapter(List<KaryaKarta> list) {
        mHeaders = (List<String>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST_HEADERS);
        mKaryakartaList = list;
        mAdapter = new KaryakarteListAdapter(list);
        listView.setAdapter(mAdapter);
        registerObservers();
    }

    private void registerObservers() {
        mAdapter.getCallNumber().subscribe( mobileNo ->
                AppUtils.dialNumber(KaryaKartaActivity.this, mobileNo)
        );

        mAdapter.getSMSNumber().subscribe( mobileNo ->
                AppUtils.openSMS(KaryaKartaActivity.this, mobileNo)
        );

        mAdapter.getWhatsAppNumber().subscribe( whatsAppNo ->
                AppUtils.openWhatsApp(KaryaKartaActivity.this, whatsAppNo)
        );

        mAdapter.getShareContent().subscribe( content ->
                AppUtils.shareContent(KaryaKartaActivity.this, content)
        );

        mAdapter.getSelectedIndex().subscribe( index ->
                openKaryakartaDetailsActivity(index)
        );
    }

    private void openKaryakartaDetailsActivity(int index) {
        Intent intent = new Intent(KaryaKartaActivity.this, KaryakartaDetailsActivity.class);
        intent.putExtra(KaryakartaDetailsActivity.SELECTED_INDEX, index);
        startActivity(intent);
    }

    private void openSearchOption() {
        final CustomBottomSheetDialog bottomSheetDialogFragment = new CustomBottomSheetDialog();
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(this).inflate(R.layout.layout_karykarta_search, findViewById(R.id.bottomSheet), false);

        ((TextView)view.findViewById(R.id.lblVillage)).setText(mHeaders.get(3));
        ((TextView)view.findViewById(R.id.lblBloodGroup)).setText(mHeaders.get(5));
        ((TextView)view.findViewById(R.id.lblGramPanchayatWardNo)).setText(mHeaders.get(10));
        ((TextView)view.findViewById(R.id.lblVidhanSabhaWardNo)).setText(mHeaders.get(11));
        ((TextView)view.findViewById(R.id.lblJilaParishadGat)).setText(mHeaders.get(12));

        ArrayAdapter<String> villageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getAllVillages());
        AutoCompleteTextView txtVillage = view.findViewById(R.id.txtVillage);
        txtVillage.setAdapter(villageAdapter);

        SpinnerAdapter bloodGroupAdapter = new ArrayAdapter<>(this, R.layout.row_spinner, R.id.textview_spinner, getBloodGroups());
        AppCompatSpinner bloodGroupSelector = view.findViewById(R.id.bloodGroupSelector);
        bloodGroupSelector.setAdapter(bloodGroupAdapter);

        ArrayAdapter<String> gramPanchayatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getGramPanchayatWardNos());
        AutoCompleteTextView txtGramPanchayat = view.findViewById(R.id.txtGramPanchayat);
        txtGramPanchayat.setAdapter(gramPanchayatAdapter);

        ArrayAdapter<String> vidhanSabhaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getVidhanSabhaWardNos());
        AutoCompleteTextView txtVidhanSabha = view.findViewById(R.id.txtVidhanSabha);
        txtVidhanSabha.setAdapter(vidhanSabhaAdapter);

        SpinnerAdapter jilaParishadAdapter = new ArrayAdapter<>(this, R.layout.row_spinner, R.id.textview_spinner, getJilaParishadGats());
        AppCompatSpinner jilaParishadSelector = view.findViewById(R.id.jilaParishadSelector);
        jilaParishadSelector.setAdapter(jilaParishadAdapter);

        view.findViewById(R.id.btnSearch).setOnClickListener(view1 -> {
            if(TextUtils.isEmpty(txtVillage.getText().toString().trim()) ||
                bloodGroupSelector.getSelectedItemPosition() > 0 ||
                TextUtils.isEmpty(txtGramPanchayat.getText().toString().trim()) ||
                TextUtils.isEmpty(txtVidhanSabha.getText().toString().trim()) ||
                jilaParishadSelector.getSelectedItemPosition() > 0) {

                searchView.setQuery("", true);
                Map<String, String> filterMap = new HashMap<>();
                if(!TextUtils.isEmpty(txtVillage.getText().toString().trim())) {
                    filterMap.put(KaryakarteListAdapter.VILLAGE_FILTER, txtVillage.getText().toString().trim());
                }

                if(bloodGroupSelector.getSelectedItemPosition() > 0) {
                    filterMap.put(KaryakarteListAdapter.BLOOD_GROUP_FILTER, bloodGroupSelector.getSelectedItem().toString());
                }

                if(!TextUtils.isEmpty(txtGramPanchayat.getText().toString().trim())) {
                    filterMap.put(KaryakarteListAdapter.GRAM_PANCHAYAT_FILTER, txtGramPanchayat.getText().toString().trim());
                }

                if(!TextUtils.isEmpty(txtVidhanSabha.getText().toString().trim())) {
                    filterMap.put(KaryakarteListAdapter.VIDHAN_SABHA_FILTER, txtVidhanSabha.getText().toString().trim());
                }

                if(jilaParishadSelector.getSelectedItemPosition() > 0) {
                    filterMap.put(KaryakarteListAdapter.JILA_PARISHAD_FILTER, jilaParishadSelector.getSelectedItem().toString());
                }

                bottomSheetDialogFragment.collapseAndDismissFragment();
                mAdapter.searchKaryakarta(filterMap);
            }

        });

        bottomSheetDialogFragment.setCustomView(view);
        pushSelectFullScreenDialog(bottomSheetDialogFragment);
    }

    private List<String> getAllVillages() {
        Set<String> setVillages = new HashSet<>();

        for(KaryaKarta karyaKarta : mKaryakartaList) {
            setVillages.add(karyaKarta.getVillageName());
        }

        List<String> villages = new ArrayList<>();
        villages.add(AppConstants.SELECT);
        villages.addAll(setVillages);

        return villages;
    }

    private List<String> getBloodGroups() {
        Set<String> setBloodGroup = new HashSet<>();

        for (KaryaKarta karyaKarta : mKaryakartaList) {
            setBloodGroup.add(karyaKarta.getBloodGroup());
        }

        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add(AppConstants.SELECT);
        bloodGroups.addAll(setBloodGroup);

        return bloodGroups;
    }

    private List<String> getGramPanchayatWardNos() {
        Set<String> setGramPanchayat = new HashSet<>();

        for (KaryaKarta karyaKarta : mKaryakartaList) {
            setGramPanchayat.add(karyaKarta.getGramPanchayatWardNo());
        }

        List<String> gramPanchayats = new ArrayList<>();
        gramPanchayats.add(AppConstants.SELECT);
        gramPanchayats.addAll(setGramPanchayat);

        return gramPanchayats;
    }

    private List<String> getVidhanSabhaWardNos() {
        Set<String> setVidhanSabha = new HashSet<>();

        for (KaryaKarta karyaKarta : mKaryakartaList) {
            setVidhanSabha.add(karyaKarta.getVidhanSabhaWardNo());
        }

        List<String> vidhanSabhas = new ArrayList<>();
        vidhanSabhas.add(AppConstants.SELECT);
        vidhanSabhas.addAll(setVidhanSabha);

        return vidhanSabhas;
    }

    private List<String> getJilaParishadGats() {
        Set<String> setJilaParishad = new HashSet<>();

        for (KaryaKarta karyaKarta : mKaryakartaList) {
            setJilaParishad.add(karyaKarta.getJilaParishadGat());
        }

        List<String> jilaParishads = new ArrayList<>();
        jilaParishads.add(AppConstants.SELECT);
        jilaParishads.addAll(setJilaParishad);

        return jilaParishads;
    }

    private void pushSelectFullScreenDialog(CustomBottomSheetDialog bottomSheetDialogFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment previousInstance = getSupportFragmentManager().findFragmentByTag(bottomSheetDialogFragment.getFragmentTag());
        if (previousInstance != null) {
            fragmentTransaction.remove(previousInstance);
        }
        fragmentTransaction.add(bottomSheetDialogFragment, bottomSheetDialogFragment.getFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void shareMultipleContacts(List<KaryaKarta> sharingList) {
        String content = "";
        for(int i=0; i<sharingList.size(); i++) {
            KaryaKarta karyaKarta = sharingList.get(i);
            content = content.concat(Integer.toString(i+1)).concat(")\n")
                    .concat(mHeaders.get(1)).concat(" -> ").concat(karyaKarta.getFullName()).concat("\n")
                    .concat(mHeaders.get(6)).concat(" -> ").concat(karyaKarta.getMobileNo()).concat("\n")
                    .concat(mHeaders.get(3)).concat(" -> ").concat(karyaKarta.getVillageName()).concat("\n\n");
        }

        AppUtils.shareContent(this, content);
    }

}
