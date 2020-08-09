package com.hw.weather.fragment.main.weatherForecastView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.weather.R;


public class WeatherList extends RecyclerView.Adapter<WeatherList.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.dateR);
        }

        public void setData(String date){
            getDate().setText(date);
        }

        public TextView getDate(){
            return date;
        }
    }

    private SourceList sourceList;

    public WeatherList(SourceList sourceList) {
        this.sourceList = sourceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherListInfo weatherListInfo = sourceList.getPosition(position);
        holder.setData(weatherListInfo.getData());
    }


    @Override
    public int getItemCount() {
        return sourceList.days();
    }
}
