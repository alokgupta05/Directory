package com.ajit.bjp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.karyakarta.KaryaKarta;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class KaryakarteListAdapter extends RecyclerView.Adapter {

    private List<KaryaKarta> mKaryaKartaList;
    private List<String> mHeaders;

    private PublishSubject<String> whatsAppNumber = PublishSubject.create();
    private PublishSubject<String> callNumber = PublishSubject.create();
    private PublishSubject<String> smsNumber = PublishSubject.create();
    private PublishSubject<Integer> index = PublishSubject.create();

    public KaryakarteListAdapter(@NonNull List<KaryaKarta> karyaKartaList) {
        mKaryaKartaList = karyaKartaList;
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
        KaryaKarta karyaKarta = mKaryaKartaList.get(i);

        String srNo = AppConstants.SR_NO.concat(Integer.toString(i+1));
        holder.lblSrNo.setText(srNo);
        holder.txtFullName.setText(karyaKarta.getFullName());
        holder.txtVillageName.setText(karyaKarta.getVillageName());
        holder.txtMobileNo.setText(karyaKarta.getMobileNo());
        holder.txtWhatsAppNo.setText(karyaKarta.getWhatsAppNo());

        holder.btnWhatsApp.setOnClickListener( view -> {
            String number = karyaKarta.getWhatsAppNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            whatsAppNumber.onNext(number);
        });

        holder.btnCall.setOnClickListener( view -> {
            String number = karyaKarta.getMobileNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            callNumber.onNext(number);
        });

        holder.btnSms.setOnClickListener( view -> {
            String number = karyaKarta.getMobileNo();
            if(!number.startsWith(AppConstants.INDIA_ISD_CODE)) {
                number = AppConstants.INDIA_ISD_CODE.concat(number);
            }
            smsNumber.onNext(number);
        });
    }

    @Override
    public int getItemCount() {
        return mKaryaKartaList.size();
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
}
