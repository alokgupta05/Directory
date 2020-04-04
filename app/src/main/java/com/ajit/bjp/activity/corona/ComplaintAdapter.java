package com.ajit.bjp.activity.corona;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.corona.CoronaComplaint;
import com.ajit.bjp.util.AppCache;
import com.ajit.bjp.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ComplaintAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CoronaComplaint> mComplaintList = new ArrayList<>();
    private List<String> mHeaders;
    private PublishSubject<String> shareContent = PublishSubject.create();
    private PublishSubject<String> whatsAppNumber = PublishSubject.create();
    private PublishSubject<String> callNumber = PublishSubject.create();
    private PublishSubject<String> smsNumber = PublishSubject.create();

    ComplaintAdapter() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_complaint, viewGroup, false);
        return new CompalaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CompalaintViewHolder holder = (CompalaintViewHolder) viewHolder;

        CoronaComplaint complaint = mComplaintList.get(i);

        holder.lblSrNo.setText(AppConstants.SR_NO.concat(Integer.toString(i+1)));
        holder.txtFullName.setText(complaint.getFullName());
        holder.txtDob.setText(complaint.getDob());
        holder.txtVillage.setText(complaint.getVillage());
        holder.txtGender.setText(complaint.getGender());
        holder.txtMobileNo.setText(complaint.getMobileNo());
        holder.txtWhatsAppNo.setText(complaint.getWhatsAppNo());
        holder.txtWorkGist.setText(complaint.getWorkGist());
        holder.txtComplaint.setText(complaint.getComplaint());
        holder.txtDepartment.setText(complaint.getDepartment());
        holder.txtTimeStamp.setText(complaint.getTimeStamp());

        if(complaint.getStatus() != null) {
            holder.txtStatus.setText(complaint.getStatus());
            if(complaint.getStatus().trim().equalsIgnoreCase(AppConstants.PENDING_TAG)) {
                holder.txtStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, null));
            } else {
                holder.txtStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green, null));
            }
        } else {
            holder.txtStatus.setText(AppConstants.PENDING_TAG);
            holder.txtStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, null));
        }

        holder.btnShare.setOnClickListener(view -> {
            String content = mHeaders.get(1).concat(" -> ").concat(complaint.getFullName())
                    .concat("\n").concat(mHeaders.get(3)).concat(" -> ").concat(complaint.getVillage())
                    .concat("\n").concat(mHeaders.get(5)).concat(" -> ").concat(complaint.getMobileNo())
                    .concat("\n").concat(mHeaders.get(7)).concat(" -> ").concat(complaint.getWhatsAppNo())
                    .concat("\n").concat(mHeaders.get(8)).concat(" -> ").concat(complaint.getComplaint());

            shareContent.onNext(content);
        });

        holder.btnWhatsApp.setOnClickListener( view ->
            whatsAppNumber.onNext(complaint.getWhatsAppNo())
        );

        holder.btnCall.setOnClickListener( view ->
            callNumber.onNext(complaint.getMobileNo())
        );

        holder.btnSms.setOnClickListener( view ->
            smsNumber.onNext(complaint.getMobileNo())
        );
    }

    @Override
    public int getItemCount() {
        return mComplaintList.size();
    }

    void setComplaintList(@NonNull List<CoronaComplaint> complaintList) {
        if(mHeaders == null) {
            mHeaders = (List<String>) AppCache.INSTANCE.getValueOfAppCache(CoronaComplaintListActivity.COMPLAINT_HEADERS);
        }

        mComplaintList.clear();
        mComplaintList.addAll(complaintList);
        notifyDataSetChanged();
    }

    Observable<String> getShareContent() {
        return shareContent;
    }

    Observable<String> getWhatsAppNumber() {
        return whatsAppNumber;
    }

    Observable<String> getCallNumber() {
        return callNumber;
    }

    Observable<String> getSMSNumber() {
        return smsNumber;
    }

    private class CompalaintViewHolder extends RecyclerView.ViewHolder {

        TextView lblSrNo, lblFullName, lblDob, lblVillage, lblGender, lblMobileNo, lblWhatsAppNo, lblWorkGist, lblComplaint, lblDepartment, lblTimeStamp, lblStatus;
        TextView txtFullName, txtDob, txtVillage, txtGender, txtMobileNo, txtWhatsAppNo, txtWorkGist, txtComplaint, txtDepartment, txtTimeStamp, txtStatus;
        ImageButton btnShare, btnWhatsApp, btnCall, btnSms;

        CompalaintViewHolder(@NonNull View itemView) {
            super(itemView);

            lblSrNo = itemView.findViewById(R.id.lblSrNo);
            lblFullName = itemView.findViewById(R.id.lblFullName);
            lblDob = itemView.findViewById(R.id.lblDob);
            lblVillage = itemView.findViewById(R.id.lblVillage);
            lblGender = itemView.findViewById(R.id.lblGender);
            lblMobileNo = itemView.findViewById(R.id.lblMobileNo);
            lblWorkGist = itemView.findViewById(R.id.lblWorkGist);
            lblWhatsAppNo = itemView.findViewById(R.id.lblWhatsAppNo);
            lblComplaint = itemView.findViewById(R.id.lblComplaint);
            lblDepartment = itemView.findViewById(R.id.lblDepartment);
            lblTimeStamp = itemView.findViewById(R.id.lblTimeStamp);
            lblStatus = itemView.findViewById(R.id.lblStatus);

            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtDob = itemView.findViewById(R.id.txtDob);
            txtVillage = itemView.findViewById(R.id.txtVillage);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtWhatsAppNo = itemView.findViewById(R.id.txtWhatsAppNo);
            txtWorkGist = itemView.findViewById(R.id.txtWorkGist);
            txtComplaint = itemView.findViewById(R.id.txtComplaint);
            txtDepartment = itemView.findViewById(R.id.txtDepartment);
            txtTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
            txtStatus = itemView.findViewById(R.id.txtStatus);

            btnShare = itemView.findViewById(R.id.btnShare);
            btnWhatsApp = itemView.findViewById(R.id.btnWhatsApp);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnSms = itemView.findViewById(R.id.btnSms);

            setHeaderTexts();
        }

        private void setHeaderTexts() {
            for (int i=0; i<mHeaders.size(); i++) {
                String header = mHeaders.get(i);
                switch (i) {

                    case 0:
                        lblTimeStamp.setText(header);
                        break;

                    case 1:
                        lblFullName.setText(header);
                        break;

                    case 2:
                        lblDob.setText(header);
                        break;

                    case 3:
                        lblVillage.setText(header);
                        break;

                    case 4:
                        lblGender.setText(header);
                        break;

                    case 5:
                        lblMobileNo.setText(header);
                        break;

                    case 6:
                        lblWorkGist.setText(header);
                        break;

                    case 7:
                        lblWhatsAppNo.setText(header);
                        break;

                    case 8:
                        lblComplaint.setText(header);
                        break;

                    case 9:
                        lblDepartment.setText(header);
                        break;

                    case 10:
                        lblStatus.setText(header);
                        break;
                }
            }
        }
    }
}
