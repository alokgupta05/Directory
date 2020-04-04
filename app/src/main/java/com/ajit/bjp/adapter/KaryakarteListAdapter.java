package com.ajit.bjp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class KaryakarteListAdapter extends RecyclerView.Adapter {

    private List<KaryaKarta> mKaryaKartaList;
    private List<KaryaKarta> mFilteredList;
    private List<String> mHeaders;
    private NameFilter mFilter;

    private PublishSubject<String> whatsAppNumber = PublishSubject.create();
    private PublishSubject<String> callNumber = PublishSubject.create();
    private PublishSubject<String> smsNumber = PublishSubject.create();
    private PublishSubject<Integer> index = PublishSubject.create();

    public KaryakarteListAdapter(@NonNull List<KaryaKarta> karyaKartaList) {
        mKaryaKartaList = karyaKartaList;
        mFilteredList = karyaKartaList;
        mFilter = new NameFilter();
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

        String srNo = AppConstants.SR_NO.concat(Integer.toString(i+1));
        holder.lblSrNo.setText(srNo);
        holder.txtFullName.setText(karyaKarta.getFullName());
        holder.txtVillageName.setText(karyaKarta.getVillageName());
        holder.txtMobileNo.setText(karyaKarta.getMobileNo());
        holder.txtWhatsAppNo.setText(karyaKarta.getWhatsAppNo());

        holder.btnWhatsApp.setOnClickListener( view ->
            whatsAppNumber.onNext(karyaKarta.getWhatsAppNo())
        );

        holder.btnCall.setOnClickListener( view ->
            callNumber.onNext(karyaKarta.getMobileNo())
        );

        holder.btnSms.setOnClickListener( view ->
            smsNumber.onNext(karyaKarta.getMobileNo())
        );

        holder.itemView.setOnClickListener( view ->
            index.onNext(getSelectedIndex(karyaKarta))
        );
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public Filter getFilter() {
        return mFilter;
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

    public Observable<Integer> getSelectedIndex() {
        return index;
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
        ImageButton btnCall, btnSms, btnWhatsApp;

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
}
