package com.hw.weather.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;


public class SearchFragment extends Fragment implements Constants {

    SharedPreferences mSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSearchSetting();

        ImageButton cityInfo = view.findViewById(R.id.searchCityFragment);
        cityInfo.setOnClickListener((View.OnClickListener) (View view1) -> {
            saveSearchSetting();
            ((MainActivity)getActivity()).onBackPressed();
        });
    }

    public void getSearchSetting() {
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
            searchCity.getEditText().setText(mSetting.getString(APP_PREFERENCES_CITY, " "));
        }
    }

    public void saveSearchSetting() {
        TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
        String city = searchCity.getEditText().getText().toString();
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_CITY, city);
        editor.putString(APP_PREFERENCES_TEMPERATURE,"Температура 36°");
        editor.putString(APP_PREFERENCES_DATE,"Сегодня");
        editor.putString(APP_PREFERENCES_UPDATE,"Сегодня");
        editor.putString(APP_PREFERENCES_PRESSURE_INFO,"Давление 759.00 мм.");
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO,"Скорость ветра 2 м.с");
        editor.apply();
    }
}