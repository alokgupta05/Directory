package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ajit.bjp.R;
import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.ExampleVillage;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.ajit.bjp.activity.SearchActivity.RESPONSE_DATA;
import static com.ajit.bjp.activity.VillageActivity.RESPONSE_DATA_VILLAGE;

public class HomeActivity extends AppCompatActivity {

    private Button btnVillages;
    private Button btnDirectory;
    private APIService mAPIService;
    private ProgressBar prgCircular;
    private Disposable disposable;
    private String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        btnVillages = findViewById(R.id.imgVillages);
        btnDirectory = findViewById(R.id.imgDirectory);
        prgCircular = findViewById(R.id.progress_circular);

        btnVillages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA_VILLAGE)!=null) {
                    launchVillages();
                }else{
                    fetchVillages();
                }
            }
        });

        btnDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA)!=null) {
                    launchDirectory();
                }else{
                    fetchDirectory();
                }
            }
        });
        mAPIService = APIUtils.getAPIService();
        getSupportActionBar().setTitle("Category");

    }

    private void launchDirectory(){
        Intent intentVillages = new Intent(HomeActivity.this, DirectoryActivity.class);
        startActivity(intentVillages);
    }

    private void launchVillages(){
        Intent intentDirectory = new Intent(HomeActivity.this, VillageActivity.class);
        startActivity(intentDirectory);
    }

    private void fetchDirectory() {


        if(disposable!=null  && !disposable.isDisposed()){
            disposable.dispose();
        }
        prgCircular.setVisibility(View.VISIBLE);
        disposable = mAPIService.getDirectoryData("json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Example>() {
                    @Override
                    public void accept(Example example) {
                        Log.i(TAG, "post submitted to API." + example);
                        prgCircular.setVisibility(View.GONE);
                        AppCache.INSTANCE.addToAppCache(RESPONSE_DATA, example);
                        launchDirectory();
                    }
                }, throwable ->
                {
                    findViewById(R.id.progress_circular).setVisibility(View.GONE);
                    Log.e(TAG, "Unable to submit post to API." + throwable.getMessage());
                });

    }


    private void fetchVillages() {
        if(disposable!=null  && !disposable.isDisposed()){
            disposable.dispose();
        }
        prgCircular.setVisibility(View.VISIBLE);
        disposable = mAPIService.getVillageData("json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ExampleVillage>() {
                    @Override
                    public void accept(ExampleVillage example) {
                        Log.i(TAG, "post submitted to API." + example);
                        prgCircular.setVisibility(View.GONE);
                        AppCache.INSTANCE.addToAppCache(RESPONSE_DATA_VILLAGE, example);
                        launchVillages();
                    }
                }, throwable ->
                {
                    prgCircular.setVisibility(View.GONE);
                    Log.e(TAG, "Unable to submit post to API." + throwable.getMessage());
                });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
