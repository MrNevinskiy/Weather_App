package com.hw.weather.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;
import com.hw.weather.fragment.recyclerView.SourceList;
import com.hw.weather.fragment.recyclerView.WeatherList;

import java.util.Objects;

public class MainFragment extends Fragment implements Constants {

    SharedPreferences mSetting;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                ((MainActivity) getActivity()).startFragment(1);
                return true;
            case R.id.navigation_setting:
                ((MainActivity) getActivity()).startFragment(2);
                return true;
            case R.id.navigation_search:
                ((MainActivity) getActivity()).startFragment(3);
                return true;

        }
        return false;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view_home);
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SourceList weatherList = new SourceList(getResources());
        getCityList(weatherList.build(), view);

        getPrefSetting();

        ImageButton search = view.findViewById(R.id.searchOnClick);
        search.setOnClickListener((View view1) -> ((MainActivity) getActivity()).startFragment(3));

        ImageButton setting = view.findViewById(R.id.settingOnClick);
        setting.setOnClickListener((View view2) -> ((MainActivity) getActivity()).startFragment(2));

        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener((View view3) -> {
            mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
            String city = (Objects.requireNonNull(mSetting.getString(APP_PREFERENCES_CITY, "City")));
            String url = "https://www.google.ru/search?newwindow=1&q=" + city;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        ImageButton about = view.findViewById(R.id.icon_about);
        about.setOnClickListener((View view4) -> {
            Toast.makeText(getActivity(), "http://www.freepik.com / Designed by Dimas_sugih / Freepik", Toast.LENGTH_LONG).show();
        });
    }

    public void getCityList(SourceList sourceList, View view) {
        RecyclerView recyclerView = getActivity().findViewById(R.id.weatherListMain);
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
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            boolean theme = mSetting.getBoolean(APP_PREFERENCES_NIGHT, false);
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //getActivity().setTheme(R.style.AppTheme);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //getActivity().setTheme(R.style.AppThemeDark);
            }
        }
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.cityFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, "City"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE)) {
            getActivity().findViewById(R.id.pressureFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            getActivity().findViewById(R.id.windSpeedFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.temperatureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE, "Температура 22°"));
        }
        if (mSetting.contains(APP_PREFERENCES_DATE)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.dateFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE, "DATE"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.pressureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO, "Давление 739.00 мм."));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.windSpeedFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO, "Скорость ветра 5 м.с"));
        }
    }
}