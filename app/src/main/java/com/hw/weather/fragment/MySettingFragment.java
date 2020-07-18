package com.hw.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;


public class MySettingFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_setting, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        navView.getMenu().findItem(R.id.navigation_setting).setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSetting();

        CheckBox pressure = view.findViewById(R.id.pressureFragmentSetting);
        pressure.setOnClickListener(view1 -> snackBar(view1, saved, save));

        CheckBox windSpeed = view.findViewById(R.id.windSpeedFragmentSetting);
        windSpeed.setOnClickListener(view12 -> snackBar(view12,saved,save));

        Button buttonSave = view.findViewById(R.id.saveSettingFragment);
        buttonSave.setOnClickListener(view13 -> {
            insertSetting();
            ((MainActivity) getActivity()).startFragment(1);
        });
    }

    public void snackBar(View view, String text1, String text2){
        Snackbar.make(view,text1, Snackbar.LENGTH_LONG).setAction(text2, (View.OnClickListener) view1 -> {
            insertSetting();
            ((MainActivity) getActivity()).startFragment(1);
        }).show();
    }

    public void insertSetting() {
        boolean checked1 = ((CheckBox) getActivity().findViewById(R.id.pressureFragmentSetting)).isChecked();
        boolean checked2 = ((CheckBox) getActivity().findViewById(R.id.windSpeedFragmentSetting)).isChecked();
        boolean checked3 = ((Switch) getActivity().findViewById(R.id.nightActivityFragment)).isChecked();
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putBoolean(APP_PREFERENCES_PRESSURE, checked1);
        editor.putBoolean(APP_PREFERENCES_WIND_SPEED, checked2);
        editor.putBoolean(APP_PREFERENCES_NIGHT, checked3);
        editor.apply();
    }

    public void getSetting() {
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_PRESSURE)) {
            CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.pressureFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.windSpeedFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true));
        }
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            Switch sw = (Switch) getActivity().findViewById(R.id.nightActivityFragment);
            sw.setChecked(mSetting.getBoolean(APP_PREFERENCES_NIGHT, true));
        }
    }
}