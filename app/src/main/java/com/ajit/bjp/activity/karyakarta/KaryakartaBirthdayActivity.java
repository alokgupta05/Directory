package com.ajit.bjp.activity.karyakarta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ajit.bjp.R;
import com.ajit.bjp.adapter.BirthdayListAdapter;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;
import com.ajit.bjp.util.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class KaryakartaBirthdayActivity extends AppCompatActivity {

    private ProgressBar prgCircular;
    private RecyclerView birthdayList;

    private HashMap<String, List<KaryaKarta>> birthdayMap;
    private Disposable disposable;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyakarta_birthday);

        getSupportActionBar().setTitle("Birthdays");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAPIService = APIUtils.getAPIService();

        prgCircular = findViewById(R.id.progress_circular);
        birthdayList = findViewById(R.id.birthdayList);

        birthdayMap = new HashMap<>();

        getKaryaKartaList();

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
            createBirthdayMap((List<KaryaKarta>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST));

        } else {
            if(disposable!=null  && !disposable.isDisposed()){
                disposable.dispose();
            }

            prgCircular.setVisibility(View.VISIBLE);
            disposable = mAPIService.getKaryakartaData("json").subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( exampleKarykarta -> {
                        createBirthdayMap(AppUtils.createKaryakartaList(exampleKarykarta.getFeed()));
                        prgCircular.setVisibility(View.GONE);

                    }, throwable ->
                    {
                        prgCircular.setVisibility(View.GONE);
                        Log.e(KaryaKartaActivity.class.getName(), "Unable to submit post to API." + throwable.getMessage());
                    });
        }

    }

    private void createBirthdayMap(List<KaryaKarta> karyaKartaList) {
        Collections.sort(karyaKartaList, (KaryaKarta k1, KaryaKarta k2) ->
            k1.getBirthday().compareTo(k2.getBirthday())
        );
        Calendar today = Calendar.getInstance();

        List<KaryaKarta> todaysList = getTodaysBirthdayList(today, karyaKartaList);
        if(!todaysList.isEmpty()) {
            birthdayMap.put(AppConstants.TODAY_KEY, todaysList);
        }

        List<KaryaKarta> recents = getRecentBirthdayList(today, karyaKartaList);
        if(!recents.isEmpty()) {
            birthdayMap.put(AppConstants.RECENTS_KEY, recents);
        }

        List<KaryaKarta> tomorrow = getTomorrowBirthdays(karyaKartaList);
        if(!tomorrow.isEmpty()) {
            birthdayMap.put(AppConstants.TOMORROW_KEY, tomorrow);
        }

        List<KaryaKarta> upcoming = getUpcomingBirthdays(karyaKartaList);
        if(!upcoming.isEmpty()) {
            birthdayMap.put(AppConstants.UPCOMING_KEY, upcoming);
        }

        BirthdayListAdapter adapter = new BirthdayListAdapter(birthdayMap);
        birthdayList.setAdapter(adapter);

        adapter.getCallNumber().subscribe( mobileNo ->
                AppUtils.dialNumber(KaryakartaBirthdayActivity.this, mobileNo)
        );

        adapter.getSMSNumber().subscribe( mobileNo ->
                AppUtils.openSMS(KaryakartaBirthdayActivity.this, mobileNo)
        );

        adapter.getWhatsAppNumber().subscribe( whatsAppNo ->
                AppUtils.openWhatsApp(KaryakartaBirthdayActivity.this, whatsAppNo)
        );

    }

    private List<KaryaKarta> getTodaysBirthdayList(Calendar today, List<KaryaKarta> karyaKartaList) {
        List<KaryaKarta> list = new ArrayList<>();

        for (KaryaKarta karyaKarta : karyaKartaList) {
            if(karyaKarta.getBirthday() != null) {
                Calendar dob = Calendar.getInstance();
                dob.setTime(karyaKarta.getBirthday());

                if(today.get(Calendar.DATE) == dob.get(Calendar.DATE) && today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)) {
                    list.add(karyaKarta);
                }
            }
        }

        return list;
    }

    private List<KaryaKarta> getRecentBirthdayList(Calendar today, List<KaryaKarta> karyaKartaList) {

        Calendar birthday = Calendar.getInstance();
        birthday.setTime(karyaKartaList.get(0).getBirthday());

        Calendar todayObj = Calendar.getInstance();
        todayObj.set(Calendar.YEAR, birthday.get(Calendar.YEAR));

        List<KaryaKarta> tempList = new ArrayList<>();

        for (KaryaKarta karyaKarta : karyaKartaList) {

            if(karyaKarta.getBirthday() != null) {

                Calendar dob = Calendar.getInstance();
                dob.setTime(karyaKarta.getBirthday());

                long diff = todayObj.getTimeInMillis() - dob.getTimeInMillis();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                if(days == 0) {
                    break;
                } else if(days <= 2) {
                    tempList.add(karyaKarta);
                }
            }

        }

        List<KaryaKarta> list = new ArrayList<>();
        for(int i=tempList.size()-1; i>=0; i--) {
            list.add(tempList.get(i));
        }

        return list;

    }

    private List<KaryaKarta> getTomorrowBirthdays(List<KaryaKarta> karyaKartaList) {
        List<KaryaKarta> list = new ArrayList<>();

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);

        for (KaryaKarta karyaKarta : karyaKartaList) {

            if(karyaKarta.getBirthday() != null) {

                Calendar dob = Calendar.getInstance();
                dob.setTime(karyaKarta.getBirthday());

                if(tomorrow.get(Calendar.MONTH) == dob.get(Calendar.MONTH) &&
                        tomorrow.get(Calendar.DATE) == dob.get(Calendar.DATE)) {

                    list.add(karyaKarta);
                }
            }
        }

        return list;
    }

    private List<KaryaKarta> getUpcomingBirthdays(List<KaryaKarta> karyaKartaList) {

        List<KaryaKarta> list = new ArrayList<>();

        Calendar yearObj = Calendar.getInstance();
        yearObj.setTime(karyaKartaList.get(0).getBirthday());

        Calendar dayAfterTomorrow = Calendar.getInstance();
        dayAfterTomorrow.add(Calendar.DATE, 2);

        Calendar nextSevenDays = Calendar.getInstance();
        nextSevenDays.add(Calendar.DATE, 7);

        boolean isMonthSame = (dayAfterTomorrow.get(Calendar.MONTH) == nextSevenDays.get(Calendar.MONTH));

        for(KaryaKarta karyaKarta : karyaKartaList) {

            if(karyaKarta.getBirthday() != null) {

                Calendar dob = Calendar.getInstance();
                dob.setTime(karyaKarta.getBirthday());

                if(dob.get(Calendar.MONTH) == dayAfterTomorrow.get(Calendar.MONTH)) {
                    if(isMonthSame) {
                        if(dob.get(Calendar.DATE) >= dayAfterTomorrow.get(Calendar.DATE) &&
                                dob.get(Calendar.DATE) <= nextSevenDays.get(Calendar.DATE)) {
                            list.add(karyaKarta);

                        } else if(dob.get(Calendar.DATE) > nextSevenDays.get(Calendar.DATE)) {
                            break;
                        }
                    } else {
                        if(dob.get(Calendar.DATE) >= dayAfterTomorrow.get(Calendar.DATE)){
                            list.add(karyaKarta);
                        }
                    }

                } else if(dob.get(Calendar.MONTH) == nextSevenDays.get(Calendar.MONTH) && !isMonthSame) {
                    if(dob.get(Calendar.DATE) <= nextSevenDays.get(Calendar.DATE)) {
                        list.add(karyaKarta);
                    } else {
                        break;
                    }
                }

            }
        }

        return list;
    }

}
