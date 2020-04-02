package com.ajit.bjp.activity.karyakarta;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
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
                dialNumber(mobileNo)
        );

        mAdapter.getSMSNumber().subscribe( mobileNo ->
                openSMS(mobileNo)
        );

        mAdapter.getWhatsAppNumber().subscribe( whatsAppNo ->
                openWhatsApp(whatsAppNo)
        );

        mAdapter.getSelectedIndex().subscribe( index ->
                openKaryakartaDetailsActivity(index)
        );
    }

    private void openWhatsApp(String whatsAppNo) {
        String url = String.format(AppConstants.WHATSAPP_URL, whatsAppNo);
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo(AppConstants.WHATSAPP_PACKAGE, PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(KaryaKartaActivity.this, AppConstants.WHATSAPP_ERROR, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void dialNumber(String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(AppConstants.TELEPHONE_TAG.concat(mobileNo)));
        startActivity(intent);
    }

    private void openSMS(String mobileNo) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse(AppConstants.SMS_TAG.concat(mobileNo)));
        startActivity(sendIntent);
    }

    private void openKaryakartaDetailsActivity(int index) {

    }

}
