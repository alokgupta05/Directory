package com.ajit.bjp.activity.corona;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ajit.bjp.R;
import com.ajit.bjp.model.CellEntry;
import com.ajit.bjp.model.Content;
import com.ajit.bjp.model.Gs$cell;
import com.ajit.bjp.model.corona.CoronaComplaint;
import com.ajit.bjp.model.corona.CoronaFeed;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoronaComplaintListActivity extends AppCompatActivity {

    private Disposable disposable;
    private APIService mAPIService;
    private ComplaintAdapter mAdapter;

    private ProgressBar prgCircular;


    public static final String COMPLAINT_HEADERS = "complaint_headers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_complaint_list);

        getSupportActionBar().setTitle("Corona Complaints");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAPIService = APIUtils.getAPIService();

        prgCircular = findViewById(R.id.progress_circular);
        RecyclerView listView = findViewById(R.id.listView);

        mAdapter = new ComplaintAdapter();
        listView.setAdapter(mAdapter);

        registerObservers();
        fetchCoronaComplaint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.corona_complaint_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menuRefresh:
                fetchCoronaComplaint();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchCoronaComplaint() {
        if(disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        prgCircular.setVisibility(View.VISIBLE);
        disposable = mAPIService.getCoronaComplaints("json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( exampleCorona -> {
                    prgCircular.setVisibility(View.GONE);
                    setAdapter(getCoronaComplaints(exampleCorona.getFeed()));

                }, throwable -> {
                    prgCircular.setVisibility(View.GONE);
                    Log.e(CoronaComplaintListActivity.class.getName(), "Unable to submit post to API." + throwable.getMessage());
                });
    }

    private void registerObservers() {
        mAdapter.getShareContent().subscribe( content ->
                AppUtils.shareContent(CoronaComplaintListActivity.this, content)
        );

        mAdapter.getWhatsAppNumber().subscribe( whatsAppNo ->
                AppUtils.openWhatsApp(CoronaComplaintListActivity.this, whatsAppNo)
        );

        mAdapter.getCallNumber().subscribe( mobileNo ->
                AppUtils.dialNumber(CoronaComplaintListActivity.this, mobileNo)
        );

        mAdapter.getSMSNumber().subscribe( mobileNo ->
                AppUtils.openSMS(CoronaComplaintListActivity.this, mobileNo)
        );
    }

    private List<CoronaComplaint> getCoronaComplaints(CoronaFeed feed) {
        List<CellEntry> entryList = feed.getEntry();
        List<CoronaComplaint> tempList = new ArrayList<>();
        List<String> complaintHeaders = new ArrayList<>();

        int startIndex = 0;

        for (int i = 0; i < entryList.size(); i++) {

            Gs$cell cell = entryList.get(i).getGs$cell();

            if(cell.getRow() > 1) {
                startIndex = i;
                break;

            } else {
                complaintHeaders.add(cell.get$t());
            }
        }

        int row = 2;
        int entries = complaintHeaders.size();

        while (entries < entryList.size()) {

            CoronaComplaint complaint = new CoronaComplaint();

            for (int i=startIndex; i<entryList.size(); i++) {

                CellEntry entry = entryList.get(i);
                Gs$cell cell = entry.getGs$cell();
                Content content = entry.getContent();

                if(cell.getRow() > row) {
                    startIndex = i;
                    break;

                } else {

                    String text = content.get$t();
                    entries += 1;

                    switch (cell.getCol()) {

                        case 1:
                            complaint.setTimeStamp(text);
                            try {
                                Date creationDate = new SimpleDateFormat("M/d/yyyy HH:mm:ss", Locale.ENGLISH).parse(text);
                                complaint.setCreationDate(creationDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 2:
                            complaint.setFullName(text);
                            break;

                        case 3:
                            complaint.setDob(text);
                            break;

                        case 4:
                            complaint.setVillage(text);
                            break;

                        case 5:
                            complaint.setGender(text);
                            break;

                        case 6:
                            complaint.setMobileNo(text);
                            break;

                        case 7:
                            complaint.setWorkGist(text);
                            break;

                        case 8:
                            complaint.setWhatsAppNo(text);
                            break;

                        case 9:
                            complaint.setComplaint(text);
                            break;

                        case 10:
                            complaint.setDepartment(text);
                            break;

                        case 11:
                            complaint.setStatus(text);
                            break;
                    }
                }
            }

            row += 1;
            tempList.add(complaint);

        }

        Collections.sort(tempList, (CoronaComplaint coronaComplaint, CoronaComplaint t1) ->
                coronaComplaint.getCreationDate().compareTo(t1.getCreationDate())
        );

        AppCache.INSTANCE.addToAppCache(COMPLAINT_HEADERS, complaintHeaders);

        List<CoronaComplaint> list = new ArrayList<>();

        for(int i=tempList.size()-1; i>=0; i--) {
            list.add(tempList.get(i));
        }

        return list;
    }

    private void setAdapter(List<CoronaComplaint> complaintList) {
        mAdapter.setComplaintList(complaintList);
    }

}
