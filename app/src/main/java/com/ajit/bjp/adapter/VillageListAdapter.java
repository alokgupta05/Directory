package com.ajit.bjp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.VillageEntry;
import com.ajit.bjp.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by O8428 on 7/13/2017.
 */

public class VillageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VillageEntry> villageEntryList;
    private OnClickPerson onClickPerson;
    private boolean mIsMultipleSharing;
    private List<VillageEntry> mSharingList;

    public interface OnClickPerson{
        void onPersonClicked(VillageEntry person);
        void onVillageDetailsShareClick(String details);
    }

    public void setOnClickPerson(OnClickPerson onClickPerson){
        this.onClickPerson = onClickPerson;
    }

    public VillageListAdapter(List<VillageEntry> personList) {
        this.villageEntryList = personList;
        if(villageEntryList == null) {
            villageEntryList = new ArrayList<>();
        }
        mSharingList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_village, parent, false);
        return new PersonHolderView(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final VillageEntry personInfo = villageEntryList.get(position);

        String srNo = Integer.toString(position+1).concat("/").concat(Integer.toString(villageEntryList.size()));

        PersonHolderView branchHolderView = (PersonHolderView) holder;

        branchHolderView.lblSrNo.setText(AppConstants.SR_NO.concat(srNo));
        branchHolderView.txtDetails.setText(personInfo.getGsx$details().get$t());
        branchHolderView.txtScheme.setText(personInfo.getGsx$scheme().get$t());
        branchHolderView.txtStatus.setText(personInfo.getGsx$status().get$t());
        branchHolderView.txtSanctionedAmount.setText(personInfo.getGsx$sanctionedamount().get$t());

        if(mIsMultipleSharing) {
            branchHolderView.checkbox.setVisibility(View.VISIBLE);
        } else {
            branchHolderView.checkbox.setChecked(false);
            branchHolderView.checkbox.setVisibility(View.GONE);
        }

        if(mSharingList.contains(personInfo)) {
            branchHolderView.checkbox.setChecked(true);
        } else {
            branchHolderView.checkbox.setChecked(false);
        }

        branchHolderView.checkbox.setOnClickListener( view -> {
            if(branchHolderView.checkbox.isChecked()) {
                mSharingList.add(personInfo);
            } else {
                mSharingList.remove(personInfo);
            }
        });

        branchHolderView.btnShare.setOnClickListener(
                (View v) -> {
                    if(onClickPerson != null && !mIsMultipleSharing) {
                        String content = "Details: "+ personInfo.getGsx$details().get$t() +
                                "\nScheme: "+ personInfo.getGsx$scheme().get$t() +
                                "\nStatus:" + personInfo.getGsx$status().get$t() +
                                "\nSanctioned Amount:" + personInfo.getGsx$sanctionedamount().get$t();
                        onClickPerson.onVillageDetailsShareClick(content);
                    }
                }
        );

        branchHolderView.itemView.setOnClickListener(
                (View v) -> {
                    if (onClickPerson != null) {
                        onClickPerson.onPersonClicked(personInfo);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return villageEntryList.size();
    }

    public void isMultipleSharing(boolean isMultipleSharing) {
        mIsMultipleSharing = isMultipleSharing;
        if(!isMultipleSharing) {
            mSharingList.clear();
        }
        notifyDataSetChanged();
    }

    public List<VillageEntry> getSharingList() {
        return mSharingList;
    }

    static class PersonHolderView extends RecyclerView.ViewHolder {
        TextView lblSrNo, txtDetails, txtScheme, txtStatus, txtSanctionedAmount;
        ImageButton btnShare;
        CheckBox checkbox;

        PersonHolderView(View view) {
            super(view);
            lblSrNo = view.findViewById(R.id.lblSrNo);
            txtDetails = view.findViewById(R.id.txtDetails);
            txtScheme = view.findViewById(R.id.txtScheme);
            txtStatus = view.findViewById(R.id.txtStatus);
            txtSanctionedAmount = view.findViewById(R.id.txtSanctionedAmount);

            btnShare = view.findViewById(R.id.btnShare);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }

}

