package com.ajit.bjp.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by O8428 on 7/13/2017.
 */

public class PersonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Person> personList;
    private List<Person> filterList;
    private CustomFilter mFilter;
    private OnClickPerson onClickPerson;
    private Context mContext;

    public interface OnClickPerson{
        void onPersonClicked(Person person);
    }

    public void setOnClickPerson(OnClickPerson onClickPerson){
        this.onClickPerson = onClickPerson;
    }

    public PersonListAdapter(List<Person> personList,Context context) {
        this.personList = personList;
        this.filterList = personList;
        mFilter = new CustomFilter(this);
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
        final Person personInfo = filterList.get(position);
        PersonHolderView branchHolderView = (PersonHolderView) holder;
        branchHolderView.itemView.setOnClickListener(
                (View v) -> {
                    if (onClickPerson != null) {
                        onClickPerson.onPersonClicked(personInfo);
                    }
                }
        );
        String charge ="";
        if(!personInfo.getCharge().equalsIgnoreCase(mContext.getString(R.string.not_available))){
            charge =   personInfo.getCharge() ;
        }
        branchHolderView.name.setText("Name: " +personInfo.getNamePerson());
        branchHolderView.department.setText("Dept: "+personInfo.getDept()  +"\nCharge: " + charge);

    }

    @Override
    public int getItemCount() {
        return filterList != null ? filterList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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

    public class CustomFilter extends Filter {
        private PersonListAdapter mAdapter;

        private CustomFilter(PersonListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            filterList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            if(TextUtils.isEmpty(keyword)) {
                filterList = personList;
            }else{
                for(Person person: personList){
                    if(person.getNamePerson().toLowerCase().contains(keyword.toString().toLowerCase())){
                        filterList.add(person);
                    }
                }
            }

            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
            //noinspection unchecked
            filterList = (ArrayList<Person>) results.values;
        }


    }

}

