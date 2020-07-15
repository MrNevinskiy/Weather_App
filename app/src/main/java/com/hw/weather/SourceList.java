package com.hw.weather;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class SourceList {

    private List<WeatherListInfo> info;
    private Resources resources;

    public SourceList(Resources resources) {
        info = new ArrayList<>(3);
        this.resources = resources;
    }

    public SourceList build() {
        String[] dataArray = resources.getStringArray(R.array.date);
        for (int i = 0; i < dataArray.length; i++) {
            info.add(new WeatherListInfo(dataArray[i]));
        }
        return this;
    }

    public WeatherListInfo getPosition(int position) {
        return info.get(position);
    }

    public int days() {
        return info.size();
    }

}
