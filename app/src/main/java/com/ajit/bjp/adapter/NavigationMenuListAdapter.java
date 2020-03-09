package com.ajit.bjp.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajit.bjp.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class NavigationMenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> mMenuList;
    private PublishSubject<Integer> selectedMenu;

    private NavigationMenuListAdapter(){}

    public NavigationMenuListAdapter(@NonNull List<Integer> menuList) {
        this.mMenuList = menuList;
        selectedMenu = PublishSubject.create();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_title, viewGroup, false);
        return new NavigationMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {
        NavigationMenuViewHolder holder = (NavigationMenuViewHolder) viewHolder;

        Resources resources = holder.itemView.getContext().getResources();
        holder.title.setText(resources.getString(mMenuList.get(postion)));

        holder.itemView.setOnClickListener(view -> {
            selectedMenu.onNext(mMenuList.get(postion));
        });
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public Observable<Integer> getSelectedMenu() {
        return selectedMenu;
    }

    class NavigationMenuViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        NavigationMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }


    }
}
