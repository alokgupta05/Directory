package com.ajit.bjp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.NavigationMenuListAdapter;
import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.ExampleVillage;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.ajit.bjp.activity.SearchActivity.RESPONSE_DATA;
import static com.ajit.bjp.activity.VillageActivity.RESPONSE_DATA_VILLAGE;

public class HomeActivity extends AppCompatActivity {

    private Button btnVillages;
    private Button btnDirectory;
    private DrawerLayout drawerLayout;
    private APIService mAPIService;
    private ProgressBar prgCircular;
    private Disposable disposable, selectedMenuDisposable;
    private String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        btnVillages = findViewById(R.id.imgVillages);
        btnDirectory = findViewById(R.id.imgDirectory);
        prgCircular = findViewById(R.id.progress_circular);

        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationMenuListAdapter menuListAdapter = getMenuListAdapter();

        RecyclerView navViewList = findViewById(R.id.navViewList);
        navViewList.setAdapter(menuListAdapter);

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

        findViewById(R.id.btnToolbarMenu).setOnClickListener( view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        selectedMenuDisposable = menuListAdapter.getSelectedMenu().subscribe(
                (@NonNull Integer selectedMenu) -> handleSelectedMenu(selectedMenu)
        );

        mAPIService = APIUtils.getAPIService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Category");

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

    private NavigationMenuListAdapter getMenuListAdapter() {
        List<Integer> menuList = new ArrayList<>();
        menuList.add(R.string.directory);
        menuList.add(R.string.villages);
        menuList.add(R.string.complaint);
        menuList.add(R.string.karyakarta);
        menuList.add(R.string.birthdays);
        menuList.add(R.string.daily_program);
        menuList.add(R.string.self_task);

        return new NavigationMenuListAdapter(menuList);
    }

    private void handleSelectedMenu(int selectedMenu) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (selectedMenu) {

            case R.string.directory:
                if(AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA)!=null) {
                    launchDirectory();
                }else{
                    fetchDirectory();
                }
                break;

            case R.string.villages:
                if(AppCache.INSTANCE.getValueOfAppCache(RESPONSE_DATA_VILLAGE)!=null) {
                    launchVillages();
                }else{
                    fetchVillages();
                }
                break;

            case R.string.complaint:
                break;

            case R.string.karyakarta:
                break;

            case R.string.birthdays:
                break;

            case R.string.daily_program:
                break;

            case R.string.self_task:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
