package com.hw.weather.fragment.search;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    private MaterialTextView materialTextView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        materialTextView = itemView.findViewById(R.id.list_item);
    }

    public MaterialTextView getMaterialTextView(){
        return materialTextView;
    }
}
