package com.ajit.bjp.activity.corona;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ajit.bjp.R;
import com.ajit.bjp.model.CellEntry;
import com.ajit.bjp.model.Content;
import com.ajit.bjp.model.Gs$cell;
import com.ajit.bjp.model.corona.CoronaComplaint;
import com.ajit.bjp.model.corona.CoronaFeed;
import com.ajit.bjp.network.APIService;
import com.ajit.bjp.network.APIUtils;
import com.ajit.bjp.util.AppCache;

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

    private ProgressBar prgCircular;
    private RecyclerView listView;

    private static final String CORONA_COMPLAINT_DATA = "corona_complaint_data";
    public static final String COMPLAINT_HEADERS = "complaint_headers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_complaint_list);

        getSupportActionBar().setTitle("Corona Complaints");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAPIService = APIUtils.getAPIService();

        prgCircular = findViewById(R.id.progress_circular);
        listView = findViewById(R.id.listView);

        getComplaintList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    private void getComplaintList() {
        if(AppCache.INSTANCE.getValueOfAppCache(CORONA_COMPLAINT_DATA) != null) {
            setAdapter((List<CoronaComplaint>) AppCache.INSTANCE.getValueOfAppCache(CORONA_COMPLAINT_DATA));

        } else {
            fetchCoronaComplaint();
        }
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

    private void setAdapter(List<CoronaComplaint> complaintList) {
        ComplaintAdapter adapter = new ComplaintAdapter(complaintList);
        listView.setAdapter(adapter);

        adapter.getShareContent().subscribe( content ->
            shareContent(content)
        );

        adapter.getWhatsAppNumber().subscribe( whatsAppNo ->
                openWhatsApp(whatsAppNo)
        );
    }

    private List<CoronaComplaint> getCoronaComplaints(CoronaFeed feed) {
        List<CellEntry> entryList = feed.getEntry();
        List<CoronaComplaint> list = new ArrayList<>();
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

        while (startIndex < (entryList.size() - feed.getGs$colCount().get$t())) {

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
                    switch (cell.getCol()) {

                        case 1:
                            complaint.setTimeStamp(text);
                            try {
                                Date creationDate = new SimpleDateFormat("m/d/yyyy HH:MM:SS", Locale.ENGLISH).parse(text);
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
                    }
                }
            }

            row++;
            list.add(complaint);

        }

        Collections.sort(list, (CoronaComplaint coronaComplaint, CoronaComplaint t1) ->
                coronaComplaint.getCreationDate().compareTo(t1.getCreationDate())
        );

        AppCache.INSTANCE.addToAppCache(COMPLAINT_HEADERS, complaintHeaders);
        AppCache.INSTANCE.addToAppCache(CORONA_COMPLAINT_DATA, list);

        return list;
    }

    private void shareContent(String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void openWhatsApp(String whatsAppNo) {
        String url = "https://api.whatsapp.com/send?phone=" + whatsAppNo;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(CoronaComplaintListActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
