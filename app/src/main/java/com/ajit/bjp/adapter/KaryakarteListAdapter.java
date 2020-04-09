package com.ajit.bjp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class KaryakarteListAdapter extends RecyclerView.Adapter {

    public static String VILLAGE_FILTER = "village_filter";
    public static String BLOOD_GROUP_FILTER = "blood_group_filter";
    public static String GRAM_PANCHAYAT_FILTER = "gram_panchayat_filter";
    public static String VIDHAN_SABHA_FILTER = "vidhan_sabha_filter";
    public static String JILA_PARISHAD_FILTER = "jila_parishad_filter";

    private List<KaryaKarta> mKaryaKartaList;
    private List<KaryaKarta> mFilteredList;
    private List<KaryaKarta> mSharingList;
    private List<String> mHeaders;
    private NameFilter mNameFilter;
    private SearchFilter mSearchFilter;
    private boolean mIsMultipleSharing;

    private PublishSubject<String> whatsAppNumber = PublishSubject.create();
    private PublishSubject<String> callNumber = PublishSubject.create();
    private PublishSubject<String> smsNumber = PublishSubject.create();
    private PublishSubject<String> shareContent = PublishSubject.create();
    private PublishSubject<Integer> index = PublishSubject.create();

    public KaryakarteListAdapter(@NonNull List<KaryaKarta> karyaKartaList) {
        mKaryaKartaList = karyaKartaList;
        mFilteredList = karyaKartaList;
        mNameFilter = new NameFilter();
        mSearchFilter = new SearchFilter();
        mSharingList = new ArrayList<>();

        mHeaders = (List<String>) AppCache.INSTANCE.getValueOfAppCache(AppConstants.KARYAKARTA_LIST_HEADERS);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_karyakarta, viewGroup, false);
        return new KaryakartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        KaryakartaViewHolder holder = (KaryakartaViewHolder) viewHolder;
        KaryaKarta karyaKarta = mFilteredList.get(i);

        String srNo = Integer.toString(i+1).concat("/").concat(Integer.toString(mFilteredList.size()));
        holder.lblSrNo.setText(AppConstants.SR_NO.concat(srNo));
        holder.txtFullName.setText(karyaKarta.getFullName());
        holder.txtVillageName.setText(karyaKarta.getVillageName());
        holder.txtMobileNo.setText(karyaKarta.getMobileNo());
        holder.txtWhatsAppNo.setText(karyaKarta.getWhatsAppNo());

        if(mIsMultipleSharing) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setChecked(false);
            holder.checkbox.setVisibility(View.GONE);
        }

        if(mSharingList.contains(karyaKarta)) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.btnWhatsApp.setOnClickListener( view ->
            whatsAppNumber.onNext(karyaKarta.getWhatsAppNo())
        );

        holder.btnCall.setOnClickListener( view ->
            callNumber.onNext(karyaKarta.getMobileNo())
        );

        holder.btnSms.setOnClickListener( view ->
            smsNumber.onNext(karyaKarta.getMobileNo())
        );

        holder.btnShare.setOnClickListener( view -> {
            if(!mIsMultipleSharing) {
                String content = mHeaders.get(1).concat(" -> ").concat(karyaKarta.getFullName()).concat("\n")
                        .concat(mHeaders.get(6)).concat(" -> ").concat(karyaKarta.getMobileNo()).concat("\n")
                        .concat(mHeaders.get(7)).concat(" -> ").concat(karyaKarta.getWhatsAppNo()).concat("\n")
                        .concat(mHeaders.get(3)).concat(" -> ").concat(karyaKarta.getVillageName());
                shareContent.onNext(content);
            }
        });

        holder.checkbox.setOnClickListener( view -> {
            if(holder.checkbox.isChecked()) {
                mSharingList.add(karyaKarta);
            } else {
                mSharingList.remove(karyaKarta);
            }
        });

        holder.itemView.setOnClickListener( view ->
            index.onNext(getSelectedIndex(karyaKarta))
        );
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public Filter getFilter() {
        return mNameFilter;
    }

    public void searchKaryakarta(@NonNull Map<String, String> searchMap) {
        mSearchFilter.searchFilter(searchMap);
        mSearchFilter.filter("");
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

    public Observable<String> getShareContent() {
        return shareContent;
    }

    public Observable<Integer> getSelectedIndex() {
        return index;
    }

    public void isMultipleSharing(boolean isMultipleSharing) {
        mIsMultipleSharing = isMultipleSharing;
        if(!isMultipleSharing) {
            mSharingList.clear();
        }
        notifyDataSetChanged();
    }

    public List<KaryaKarta> getSharingList() {
        return mSharingList;
    }

    private int getSelectedIndex(KaryaKarta karyaKarta) {
        int index = 0;
        for(int i=0; i<mKaryaKartaList.size(); i++) {
            if(mKaryaKartaList.get(i).getFullName().equals(karyaKarta.getFullName())) {
                index = i;
                break;
            }
        }

        return index;
    }

    private class KaryakartaViewHolder extends RecyclerView.ViewHolder {

        TextView lblSrNo, lblFullName, lblVillageName, lblMobileNo, lblWhatsAppNo;
        TextView txtFullName, txtVillageName, txtMobileNo, txtWhatsAppNo;
        ImageButton btnCall, btnSms, btnWhatsApp, btnShare;
        CheckBox checkbox;

        KaryakartaViewHolder(@NonNull View itemView) {
            super(itemView);

            lblSrNo = itemView.findViewById(R.id.lblSrNo);
            lblFullName = itemView.findViewById(R.id.lblFullName);
            lblVillageName = itemView.findViewById(R.id.lblVillageName);
            lblMobileNo = itemView.findViewById(R.id.lblMobileNo);
            lblWhatsAppNo = itemView.findViewById(R.id.lblWhatsAppNo);

            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtVillageName = itemView.findViewById(R.id.txtVillageName);
            txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtWhatsAppNo = itemView.findViewById(R.id.txtWhatsAppNo);

            btnCall = itemView.findViewById(R.id.btnCall);
            btnSms = itemView.findViewById(R.id.btnSms);
            btnWhatsApp = itemView.findViewById(R.id.btnWhatsApp);
            btnShare = itemView.findViewById(R.id.btnShare);

            checkbox = itemView.findViewById(R.id.checkbox);

            setHeaderTexts();
        }

        private void setHeaderTexts() {
            for (int i=0; i<mHeaders.size(); i++) {
                String header = mHeaders.get(i);
                switch (i) {

                    case 1:
                        lblFullName.setText(header);
                        break;

                    case 3:
                        lblVillageName.setText(header);
                        break;

                    case 6:
                        lblMobileNo.setText(header);
                        break;

                    case 7:
                        lblWhatsAppNo.setText(header);
                        break;
                }
            }
        }
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            final FilterResults results = new FilterResults();

            List<KaryaKarta> filteredList = new ArrayList<>();

            if(TextUtils.isEmpty(keyword)) {
                filteredList = mKaryaKartaList;

            } else {
                for(KaryaKarta person: mKaryaKartaList){
                    if(person.getFullName().toLowerCase().contains(keyword.toString().toLowerCase())){
                        filteredList.add(person);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //noinspection unchecked
            mFilteredList = (ArrayList<KaryaKarta>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    private class SearchFilter extends Filter {

        private Map<String, String> mSearchMap;

        SearchFilter() {
            mSearchMap = new HashMap<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults results = new FilterResults();
            List<KaryaKarta> filteredList = new ArrayList<>();

            if(mSearchMap.containsKey(VILLAGE_FILTER)) {
                String village = mSearchMap.get(VILLAGE_FILTER);
                for (KaryaKarta person : mKaryaKartaList) {
                    if(person.getVillageName().equals(village)) {
                        filteredList.add(person);
                    }
                }
            }

            if(mSearchMap.containsKey(BLOOD_GROUP_FILTER)) {
                String bloodGroup = mSearchMap.get(BLOOD_GROUP_FILTER);
                List<KaryaKarta> bloodGrpFilterList = new ArrayList<>();
                List<KaryaKarta> tempList = new ArrayList<>();

                if(filteredList.isEmpty()) {
                    tempList.addAll(mKaryaKartaList);
                } else {
                    tempList.addAll(filteredList);
                }

                for (KaryaKarta person : tempList) {
                    if (person.getBloodGroup().equals(bloodGroup)) {
                        bloodGrpFilterList.add(person);
                    }
                }

                filteredList.clear();
                filteredList.addAll(bloodGrpFilterList);
            }

            if(mSearchMap.containsKey(GRAM_PANCHAYAT_FILTER)) {
                String gramPanchayat = mSearchMap.get(GRAM_PANCHAYAT_FILTER);
                List<KaryaKarta> gramPanchayatFilterList = new ArrayList<>();
                List<KaryaKarta> tempList = new ArrayList<>();

                if(filteredList.isEmpty()) {
                    tempList.addAll(mKaryaKartaList);
                } else {
                    tempList.addAll(filteredList);
                }

                for (KaryaKarta person : tempList) {
                    if (person.getGramPanchayatWardNo().equals(gramPanchayat)) {
                        gramPanchayatFilterList.add(person);
                    }
                }

                filteredList.clear();
                filteredList.addAll(gramPanchayatFilterList);
            }

            if(mSearchMap.containsKey(VIDHAN_SABHA_FILTER)) {
                String vidhanSabha = mSearchMap.get(VIDHAN_SABHA_FILTER);
                List<KaryaKarta> vidhanSabhaFilterList = new ArrayList<>();
                List<KaryaKarta> tempList = new ArrayList<>();

                if(filteredList.isEmpty()) {
                    tempList.addAll(mKaryaKartaList);
                } else {
                    tempList.addAll(filteredList);
                }

                for (KaryaKarta person : tempList) {
                    if (person.getVidhanSabhaWardNo().equals(vidhanSabha)) {
                        vidhanSabhaFilterList.add(person);
                    }
                }

                filteredList.clear();
                filteredList.addAll(vidhanSabhaFilterList);
            }

            if(mSearchMap.containsKey(JILA_PARISHAD_FILTER)) {
                String jilaParishad = mSearchMap.get(JILA_PARISHAD_FILTER);
                List<KaryaKarta> jilaParishadFilterList = new ArrayList<>();
                List<KaryaKarta> tempList = new ArrayList<>();

                if(filteredList.isEmpty()) {
                    tempList.addAll(mKaryaKartaList);
                } else {
                    tempList.addAll(filteredList);
                }

                for (KaryaKarta person : tempList) {
                    if (person.getVidhanSabhaWardNo().equals(jilaParishad)) {
                        jilaParishadFilterList.add(person);
                    }
                }

                filteredList.clear();
                filteredList.addAll(jilaParishadFilterList);
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //noinspection unchecked
            mFilteredList = (ArrayList<KaryaKarta>) filterResults.values;
            notifyDataSetChanged();
        }

        void searchFilter(@NonNull Map<String, String> searchMap) {
            mSearchMap.clear();
            mSearchMap.putAll(searchMap);
        }
    }
}
