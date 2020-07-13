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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;

import java.util.Date;


public class SearchFragment extends Fragment implements Constants {

    SharedPreferences mSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_CITY)){
            EditText editText = (EditText) getActivity().findViewById(R.id.entryCityFragment);
            editText.setText(mSetting.getString(APP_PREFERENCES_CITY, " "));
        }

        ImageButton cityInfo = view.findViewById(R.id.searchCityFragment);
        cityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchCity = (EditText) getActivity().findViewById(R.id.entryCityFragment);
                String city = searchCity.getText().toString();
                SharedPreferences.Editor editor = mSetting.edit();
                infoCityTest();
                editor.putString(APP_PREFERENCES_CITY,city);
                editor.apply();
                Toast.makeText(getActivity(), city, Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
    }

    public void infoCityTest(){
        Date date = new Date();
        String DATE = date.toString();
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_TEMPERATURE,"Температура на улице 38°");
        editor.putString(APP_PREFERENCES_DATE,DATE);
        editor.putString(APP_PREFERENCES_UPDATE,DATE);
        editor.putString(APP_PREFERENCES_PRESSURE_INFO,"Давление 759.00 мм. ");
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO,"Скорость ветра 2 м.с ");
        editor.apply();
    }
}