package com.ajit.bjp.activity.karyakarta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.KaryakarteListAdapter;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;
import com.ajit.bjp.util.AppUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class KaryaKartaActivity extends AppCompatActivity {

    private Disposable disposable;
    private APIService mAPIService;

    private ProgressBar prgCircular;
    private RecyclerView listView;
    private KaryakarteListAdapter mAdapter;

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

        SearchView searchView = findViewById(R.id.search_view);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
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

        mAdapter.getSelectedIndex().subscribe( index ->
                openKaryakartaDetailsActivity(index)
        );
    }

    private void openKaryakartaDetailsActivity(int index) {
        Intent intent = new Intent(KaryaKartaActivity.this, KaryakartaDetailsActivity.class);
        intent.putExtra(KaryakartaDetailsActivity.SELECTED_INDEX, index);
        startActivity(intent);
    }

}
