package com.hw.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainFragment extends Fragment implements Constants {

    SharedPreferences mSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setting();
        city();
        ImageButton search = view.findViewById(R.id.searchOnClick);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).startFragment(3);
            }
        });
        ImageButton setting = view.findViewById(R.id.settingOnClick);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).startFragment(2);
            }
        });
        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = null;
                if(mSetting.contains(APP_PREFERENCES_CITY)) {
                    city = (mSetting.getString(APP_PREFERENCES_CITY, "  "));
                }
                String url = "https://www.ya.ru/" + city;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ImageButton about = view.findViewById(R.id.icon_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "http://www.freepik.com / Designed by Dimas_sugih / Freepik", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setting() {
        Date date = new Date();
        String DATE = date.toString();
        mSetting = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_PRESSURE)){
            getActivity().findViewById(R.id.pressureFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE,true)) ? View.GONE : View.VISIBLE);
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED)){
            getActivity().findViewById(R.id.windSpeedFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED,true)) ? View.GONE : View.VISIBLE);
        }
        if(mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.temperatureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE,"Температура на улице 36°"));
        }
        if(mSetting.contains(APP_PREFERENCES_DATE)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.dateFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE,DATE));
        }
        if(mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.pressureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO,"Давление 759.00 мм. "));
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            TextView textView = (TextView) getActivity().findViewById(R.id.windSpeedFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO,"Скорость ветра 2 м.с "));
        }
    }

    public void city() {
        if(mSetting.contains(APP_PREFERENCES_CITY)){
            TextView textView = (TextView)getActivity().findViewById(R.id.cityFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, ""));
        }
    }
}