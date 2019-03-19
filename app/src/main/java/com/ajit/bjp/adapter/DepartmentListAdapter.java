package com.ajit.bjp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajit.bjp.R;
import com.ajit.bjp.model.Content;
import com.ajit.bjp.model.Entry;
import com.ajit.bjp.model.VillageEntry;

import java.util.List;

/**
 * Created by O8428 on 7/13/2017.
 */

public class DepartmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Entry> villageEntryList;
    private OnClickPerson onClickPerson;
    private Context mContext;

    public interface OnClickPerson{
        void onPersonClicked(Entry person);
    }

    public void setOnClickPerson(OnClickPerson onClickPerson){
        this.onClickPerson = onClickPerson;
    }

    public DepartmentListAdapter(List<Entry> personList,Context context) {
        this.villageEntryList = personList;
        this.mContext = context;
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
        final Entry personInfo = villageEntryList.get(position);
        PersonHolderView branchHolderView = (PersonHolderView) holder;
        branchHolderView.itemView.setOnClickListener(
                (View v) -> {
                    if (onClickPerson != null) {
                        onClickPerson.onPersonClicked(personInfo);
                    }
                }
        );

        String charge ="";
        if(!personInfo.getGsx$charge().get$t().equalsIgnoreCase(mContext.getString(R.string.not_available))){
            charge =   personInfo.getGsx$charge().get$t();
        }
        branchHolderView.name.setText("Name: "+personInfo.getGsx$name().get$t());
        branchHolderView.department.setText("Designation: "+personInfo.getGsx$designation().get$t() +"\nCharge: "+ charge);

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

