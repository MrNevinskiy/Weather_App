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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;
import com.hw.weather.fragment.search.SearchFragment;


public class MySettingFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;

    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainFragment mainFragment = new MainFragment();
                    ((MainActivity) getActivity()).startFragment(mainFragment);
                case R.id.navigation_setting:
                    MySettingFragment mySettingFragment = new MySettingFragment();
                    ((MainActivity) getActivity()).startFragment(mySettingFragment);
                case R.id.navigation_search:
                    SearchFragment searchFragment = new SearchFragment();
                    ((MainActivity) getActivity()).startFragment(searchFragment);
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_setting, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        navView.getMenu().findItem(R.id.navigation_setting).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSetting();

        MaterialCheckBox pressure = view.findViewById(R.id.pressureFragmentSetting);
        pressure.setOnClickListener(view1 -> snackBar(view1, saved, save));

        MaterialCheckBox windSpeed = view.findViewById(R.id.windSpeedFragmentSetting);
        windSpeed.setOnClickListener(view12 -> snackBar(view12,saved,save));

        MaterialButton buttonSave = view.findViewById(R.id.saveSettingFragment);
        buttonSave.setOnClickListener(view13 -> {
            insertSetting();
            MainFragment mainFragment = new MainFragment();
            ((MainActivity) getActivity()).startFragment(mainFragment);;
        });
    }

    public void snackBar(View view, String text1, String text2){
        Snackbar.make(view,text1, Snackbar.LENGTH_LONG).setAction(text2, (View.OnClickListener) view1 -> {
            insertSetting();
            MainFragment mainFragment = new MainFragment();
            ((MainActivity) getActivity()).startFragment(mainFragment);;
        }).show();
    }

    public void insertSetting() {
        boolean checked1 = ((MaterialCheckBox) getActivity().findViewById(R.id.pressureFragmentSetting)).isChecked();
        boolean checked2 = ((MaterialCheckBox) getActivity().findViewById(R.id.windSpeedFragmentSetting)).isChecked();
        boolean checked3 = ((SwitchMaterial) getActivity().findViewById(R.id.nightActivityFragment)).isChecked();
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
            MaterialCheckBox checkBox = (MaterialCheckBox) getActivity().findViewById(R.id.pressureFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            MaterialCheckBox checkBox = (MaterialCheckBox) getActivity().findViewById(R.id.windSpeedFragmentSetting);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true));
        }
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            SwitchMaterial sw = (SwitchMaterial) getActivity().findViewById(R.id.nightActivityFragment);
            sw.setChecked(mSetting.getBoolean(APP_PREFERENCES_NIGHT, true));
        }
    }
}