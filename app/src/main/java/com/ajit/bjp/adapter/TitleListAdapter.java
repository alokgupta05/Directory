package com.ajit.bjp.adapter;

import android.content.Context;
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

public class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private List<String> stringList;
    private List<String> filterList;
    private OnClickDepartment onClickDepartment;
    private CustomFilter mFilter;

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnClickDepartment{
        void onDepartmentClicked(String dept);
    }

    public TitleListAdapter(Context context,List<String> stringList) {
        inflater = LayoutInflater.from(context);
        this.stringList= stringList;
        this.filterList = stringList;
        mFilter = new CustomFilter(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_title, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.serial_number.setText(filterList.get(position));
        holder.itemView.setOnClickListener(
                (View v) -> {
                    if (onClickDepartment != null) {
                        onClickDepartment.onDepartmentClicked(filterList.get(position));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return filterList!=null?filterList.size():0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView serial_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            serial_number = (TextView)itemView.findViewById(R.id.title);
        }
    }

    public void setOnClickDepartment(OnClickDepartment onClickDepartment) {
        this.onClickDepartment = onClickDepartment;
    }

    public class CustomFilter extends Filter {
        private TitleListAdapter mAdapter;

        private CustomFilter(TitleListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            filterList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            if(TextUtils.isEmpty(keyword)) {
                filterList = stringList;
            }else{
                for(String person: stringList){
                    if(person.toLowerCase().contains(keyword.toString().toLowerCase())){
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
            filterList = (ArrayList<String>) results.values;
        }


    }

}
