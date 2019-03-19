package com.ajit.bjp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.Person;
import com.ajit.bjp.model.VillageEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by O8428 on 7/13/2017.
 */

public class VillageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VillageEntry> villageEntryList;
    private OnClickPerson onClickPerson;

    public interface OnClickPerson{
        void onPersonClicked(VillageEntry person);
    }

    public void setOnClickPerson(OnClickPerson onClickPerson){
        this.onClickPerson = onClickPerson;
    }

    public VillageListAdapter(List<VillageEntry> personList) {
        this.villageEntryList = personList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_person_information, parent, false);
        return new PersonHolderView(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final VillageEntry personInfo = villageEntryList.get(position);
        PersonHolderView branchHolderView = (PersonHolderView) holder;
        branchHolderView.itemView.setOnClickListener(
                (View v) -> {
                    if (onClickPerson != null) {
                        onClickPerson.onPersonClicked(personInfo);
                    }
                }
        );
        branchHolderView.name.setText("Details: "+ personInfo.getGsx$details().get$t());
        branchHolderView.department.setText("Scheme: "+personInfo.getGsx$scheme().get$t() + "\nStatus: " + personInfo.getGsx$status().get$t());

    }

    @Override
    public int getItemCount() {
        return villageEntryList != null ? villageEntryList.size() : 0;
    }


    static class PersonHolderView extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView department;


        PersonHolderView(View view) {
            super(view);
            this.name = view.findViewById(R.id.text_name);
            this.department = view.findViewById(R.id.text_depart);

        }
    }

}

