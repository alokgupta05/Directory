package com.ajit.bjp.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class BirthdayListAdapter extends RecyclerView.Adapter {

    private HashMap<String, List<KaryaKarta>> mBirthdayMap;
    private List<String> mHeaders;

    private PublishSubject<String> whatsAppNumber = PublishSubject.create();
    private PublishSubject<String> callNumber = PublishSubject.create();
    private PublishSubject<String> smsNumber = PublishSubject.create();

    public BirthdayListAdapter(@NonNull HashMap<String, List<KaryaKarta>> birthdayMap) {
        mBirthdayMap = birthdayMap;
        mHeaders = (List<String>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST_HEADERS);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_birthday_header, viewGroup, false);
        return new BirthdayHeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        BirthdayHeaderViewHolder holder = (BirthdayHeaderViewHolder) viewHolder;
        if(position == 0) {
            holder.rowHeader.setText(AppConstants.TODAY_TAG);
            if(!mBirthdayMap.containsKey(AppConstants.TODAY_KEY)) {
                holder.rowHeader.setVisibility(View.GONE);
            } else {
                addTodayBirthdayViews(holder.birthdayRowContainer);
            }

        } else if(position == 1) {
            holder.rowHeader.setText(AppConstants.RECENTS_TAG);
            if(!mBirthdayMap.containsKey(AppConstants.RECENTS_KEY)) {
                holder.rowHeader.setVisibility(View.GONE);
            } else {
                getRecentBirthdays(holder.birthdayRowContainer);
            }

        } else if(position == 2) {
            holder.rowHeader.setText(AppConstants.UPCOMING_TAG);
            addUpcomingBirthdayViews(holder.birthdayRowContainer);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public Observable<String> getWhatsAppNumber() {
        return whatsAppNumber;
    }

    public Observable<String> getCallNumber() {
        return callNumber;
    }

    public Observable<String> getSMSNumber() {
        return smsNumber;
    }

    private void addTodayBirthdayViews(LinearLayout container) {
        if(container.getChildCount() == 0) {
            List<KaryaKarta> list = mBirthdayMap.get(AppConstants.TODAY_KEY);
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            for (KaryaKarta karyaKarta : list) {

                String date = new SimpleDateFormat("MMM d", Locale.ENGLISH).format(karyaKarta.getBirthday());
                String dateStr = String.format(AppConstants.TODAY_BIRTHDAY_TAG, date);
                View view = createKaryaKartaDetailView(inflater, container, karyaKarta, dateStr, true);

                container.addView(view);
            }
        }
    }

    private void getRecentBirthdays(LinearLayout container) {
        if(container.getChildCount() == 0) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            List<KaryaKarta> list = mBirthdayMap.get(AppConstants.RECENTS_KEY);
            Calendar today = Calendar.getInstance();

            for (KaryaKarta karyaKarta : list) {
                Date birthday = karyaKarta.getBirthday();
                Calendar dob = Calendar.getInstance();
                dob.setTime(birthday);
                int diff = today.get(Calendar.DATE) - dob.get(Calendar.DATE);
                String day = "";
                if(diff == 1) {
                    day = " day";
                } else {
                    day = " days";
                }

                day = Integer.toString(diff).concat(day);
                String date = new SimpleDateFormat("MMM d", Locale.ENGLISH).format(birthday);
                String dateStr = String.format(AppConstants.RECENT_BIRTHDAT_TAG, day, date);
                View view = createKaryaKartaDetailView(inflater, container, karyaKarta, dateStr, true);

                container.addView(view);
            }
        }
    }

    private void addUpcomingBirthdayViews(LinearLayout container) {
        if(container.getChildCount() == 0) {
            getTomorrowBirthdayViews(container);

            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            List<KaryaKarta> list = mBirthdayMap.get(AppConstants.UPCOMING_KEY);
            int month = -1;

            for (KaryaKarta karyaKarta: list) {
                Calendar dob = Calendar.getInstance();
                dob.setTime(karyaKarta.getBirthday());
                int birthMonth = dob.get(Calendar.MONTH);

                if(birthMonth != month) {
                    month = birthMonth;

                    Resources res = container.getResources();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,res.getDimensionPixelSize(R.dimen.dp_1));

                    View separator = new View(container.getContext());
                    separator.setBackgroundColor(res.getColor(android.R.color.darker_gray, null));
                    container.addView(separator, params);

                    View subHeaderView = inflater.inflate(R.layout.row_birthday_header, container, false);
                    subHeaderView.findViewById(R.id.rowHeader).setVisibility(View.GONE);
                    subHeaderView.findViewById(R.id.viewSeparator).setVisibility(View.INVISIBLE);
                    subHeaderView.findViewById(R.id.rowSubHeader).setVisibility(View.VISIBLE);

                    String laterStr = String.format(AppConstants.LATER_TAG, getMonth(birthMonth));
                    ((TextView)subHeaderView.findViewById(R.id.rowSubHeader)).setText(laterStr);
                    container.addView(subHeaderView);

                }

                String dateStr = new SimpleDateFormat("MMM d", Locale.ENGLISH).format(karyaKarta.getBirthday());
                View view = createKaryaKartaDetailView(inflater, container, karyaKarta, dateStr, false);

                container.addView(view);
            }

        }
    }

    private void getTomorrowBirthdayViews(LinearLayout container) {
        if(mBirthdayMap.containsKey(AppConstants.TOMORROW_KEY)) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());

            View subHeaderView = inflater.inflate(R.layout.row_birthday_header, container, false);
            subHeaderView.findViewById(R.id.rowHeader).setVisibility(View.GONE);
            subHeaderView.findViewById(R.id.viewSeparator).setVisibility(View.INVISIBLE);
            subHeaderView.findViewById(R.id.rowSubHeader).setVisibility(View.VISIBLE);

            ((TextView)subHeaderView.findViewById(R.id.rowSubHeader)).setText(AppConstants.TOMORROW_TAG);
            container.addView(subHeaderView);

            List<KaryaKarta> list = mBirthdayMap.get(AppConstants.TOMORROW_KEY);
            for (KaryaKarta karyaKarta : list) {
                String date = new SimpleDateFormat("MMM d", Locale.ENGLISH).format(karyaKarta.getBirthday());
                String dateStr = String.format(AppConstants.TOMORROW_BIRTHDAY_TAG, date);
                View view = createKaryaKartaDetailView(inflater, container, karyaKarta, dateStr, true);

                container.addView(view);
            }
        }
    }

    private View createKaryaKartaDetailView(LayoutInflater inflater, ViewGroup container, KaryaKarta karyaKarta, String dateStr, boolean isMobileNoToBeShown) {
        View view = inflater.inflate(R.layout.row_birthday_karyakarta, container, false);

        ((TextView)view.findViewById(R.id.lblFullName)).setText(mHeaders.get(1));
        ((TextView)view.findViewById(R.id.txtFullName)).setText(karyaKarta.getFullName());

        ((TextView)view.findViewById(R.id.lblVillageName)).setText(mHeaders.get(3));
        ((TextView)view.findViewById(R.id.txtVillageName)).setText(karyaKarta.getVillageName());

        if(isMobileNoToBeShown) {
            ((TextView)view.findViewById(R.id.lblMobileNo)).setText(mHeaders.get(6));
            ((TextView)view.findViewById(R.id.txtMobileNo)).setText(karyaKarta.getMobileNo());

            ((TextView)view.findViewById(R.id.lblWhatsAppNo)).setText(mHeaders.get(7));
            ((TextView)view.findViewById(R.id.txtWhatsAppNo)).setText(karyaKarta.getWhatsAppNo());

        } else {
            view.findViewById(R.id.mobileNoLayout).setVisibility(View.GONE);
            view.findViewById(R.id.whatsAppNoLayout).setVisibility(View.GONE);
        }

        ((TextView)view.findViewById(R.id.lblBirthday)).setText(dateStr);

        view.findViewById(R.id.btnCall).setOnClickListener( view1 ->
                callNumber.onNext(karyaKarta.getMobileNo())
        );

        view.findViewById(R.id.btnSms).setOnClickListener( view1 ->
                smsNumber.onNext(karyaKarta.getMobileNo())
        );

        view.findViewById(R.id.btnWhatsApp).setOnClickListener( view1 ->
                whatsAppNumber.onNext(karyaKarta.getWhatsAppNo())
        );

        return view;
    }

    private String getMonth(int month) {
        String monthStr = "";
        switch (month) {
            case Calendar.JANUARY:
                monthStr = "January";
                break;

            case Calendar.FEBRUARY:
                monthStr = "February";
                break;

            case Calendar.MARCH:
                monthStr = "March";
                break;

            case Calendar.APRIL:
                monthStr = "April";
                break;

            case Calendar.MAY:
                monthStr = "May";
                break;

            case Calendar.JUNE:
                monthStr = "June";
                break;

            case Calendar.JULY:
                monthStr = "July";
                break;

            case Calendar.AUGUST:
                monthStr = "August";
                break;

            case Calendar.SEPTEMBER:
                monthStr = "September";
                break;

            case Calendar.OCTOBER:
                monthStr = "October";
                break;

            case Calendar.NOVEMBER:
                monthStr = "November";
                break;

            case Calendar.DECEMBER:
                monthStr = "December";
                break;
        }

        return monthStr;
    }

    private class BirthdayHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView rowHeader;
        LinearLayout birthdayRowContainer;

        BirthdayHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            rowHeader = itemView.findViewById(R.id.rowHeader);
            birthdayRowContainer = itemView.findViewById(R.id.birthdayRowContainer);
        }
    }

}
