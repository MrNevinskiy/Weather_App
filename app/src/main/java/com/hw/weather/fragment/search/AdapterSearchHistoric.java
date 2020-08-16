package com.hw.weather.fragment.search;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.weather.R;
import com.hw.weather.room.WeatherCity;
import com.hw.weather.room.WeatherSource;

import java.util.List;

public class AdapterSearchHistoric extends RecyclerView.Adapter<AdapterSearchHistoric.ViewHolder> {

    private WeatherSource weatherSource;
    private Fragment fragment;
    private long menuPosition;

    public AdapterSearchHistoric(WeatherSource weatherSource, Fragment fragment) {
        this.weatherSource = weatherSource;
        this.fragment = fragment;
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
        List<WeatherCity> weatherCities = weatherSource.getCities();
        WeatherCity weatherCity = weatherCities.get(position);
        holder.city.setText(weatherCity.city);
        holder.date.setText(weatherCity.date);
        holder.temp.setText(weatherCity.temp);

        holder.cardView.setOnClickListener(view -> {
            weatherSource.removeCity(weatherCity);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return weatherSource == null ? 0 : (int) weatherSource.getCountCity();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView city;
        private TextView date;
        private TextView temp;
        private View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            city = cardView.findViewById(R.id.history_city);
            date = cardView.findViewById(R.id.history_date);
            temp = cardView.findViewById(R.id.history_temperature);
        }
    }
}
