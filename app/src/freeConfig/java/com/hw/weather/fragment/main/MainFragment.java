package com.hw.weather.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.Constants;
import com.hw.weather.R;
import com.hw.weather.fragment.main.weatherForecastView.SourceList;
import com.hw.weather.fragment.main.weatherForecastView.WeatherList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class MainFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SourceList weatherList = new SourceList(getResources());
        getCityList(weatherList.build(), view);
        checkCity(view);
        getPrefSetting();
    }


    private void checkCity(@NonNull View view) {
        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener((View view3) -> {
            mSetting = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            String city = (Objects.requireNonNull(mSetting.getString(APP_PREFERENCES_CITY, "City")));
            String url = "https://www.google.ru/search?newwindow=1&q=" + city;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    public void getCityList(SourceList sourceList, View view) {
        RecyclerView recyclerView = getView().findViewById(R.id.weatherListMain);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        WeatherList weatherList = new WeatherList(sourceList);
        recyclerView.setAdapter(weatherList);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void getPrefSetting() {
        mSetting = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            boolean theme = mSetting.getBoolean(APP_PREFERENCES_NIGHT, false);
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.cityFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, "City"));
        } if (mSetting.contains(APP_PREFERENCES_ICON)) {
            try {
                ImageView imageView = (ImageView) getView().findViewById(R.id.weatherIcon);
                File file = new File(mSetting.getString(APP_PREFERENCES_ICON, null), "icon.png");
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.w("File Not Found Exception",e);
                e.printStackTrace();
            }
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE)) {
            getView().findViewById(R.id.pressureFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            getView().findViewById(R.id.windSpeedFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temperatureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE_MIN)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temp_min);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE_MIN, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE_MAX)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temp_max);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE_MAX, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_DATE)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.dateFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE, "DATE"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.pressureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.windSpeedFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO, "null"));
        }
    }
}