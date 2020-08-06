package com.hw.weather.fragment.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.R;

import java.util.List;

public class AdapterSearchHistoric extends RecyclerView.Adapter<ViewHolder> {

    private List<String> cityArr;
    private Activity activity;
    private int menuPosition;

    public AdapterSearchHistoric(List<String> cityArr, Activity activity) {
        this.cityArr = cityArr;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_r, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MaterialTextView materialTextView = holder.getMaterialTextView();
        materialTextView.setText(cityArr.get(position));

        materialTextView.setOnLongClickListener((view -> {
            menuPosition = position;
            return false;
        }));

        activity.registerForContextMenu(materialTextView);
    }

    @Override
    public int getItemCount() {
        return cityArr == null ? 0 : cityArr.size();
    }

    public void addItem(String element){
        cityArr.add(element);
        notifyItemInserted(cityArr.size()-1);
    }

    void updateItem(String element, int position){
        cityArr.set(position, element);
        notifyItemChanged(position);
    }

    void removeItem(int position){
        cityArr.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItems(){
        cityArr.clear();
        notifyDataSetChanged();
    }

    public int getMenuPosition() {
        return menuPosition;
    }

}
