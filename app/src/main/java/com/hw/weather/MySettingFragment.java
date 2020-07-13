package com.hw.weather;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;


public class MySettingFragment extends Fragment implements Constants {

    SharedPreferences mSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetting = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_PRESSURE)){
            CheckBox checkBox = (CheckBox)getActivity().findViewById(R.id.pressureFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true));
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED)){
            CheckBox checkBox = (CheckBox)getActivity().findViewById(R.id.windSpeedFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true));
        }
        if(mSetting.contains(APP_PREFERENCES_NIGHT)){
            Switch sw = (Switch)getActivity().findViewById(R.id.nightActivityFragment);
            sw.setChecked(mSetting.getBoolean(APP_PREFERENCES_NIGHT, true));
        }
        Button save = view.findViewById(R.id.saveSettingFragment);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked1 = ((CheckBox)getActivity().findViewById(R.id.pressureFragmentSetting)).isChecked();
                boolean checked2 = ((CheckBox)getActivity().findViewById(R.id.windSpeedFragmentSetting)).isChecked();
                boolean checked3 = ((Switch)getActivity().findViewById(R.id.nightActivityFragment)).isChecked();
                SharedPreferences.Editor editor = mSetting.edit();
                editor.putBoolean(APP_PREFERENCES_PRESSURE, checked1);
                editor.putBoolean(APP_PREFERENCES_WIND_SPEED, checked2);
                editor.putBoolean(APP_PREFERENCES_NIGHT,checked3);
                editor.apply();
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
    }
}