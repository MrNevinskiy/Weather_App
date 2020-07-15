package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.hw.weather.fragment.MainFragment;
import com.hw.weather.fragment.MySettingFragment;
import com.hw.weather.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        ft.replace(R.id.test_replace, mainFragment);
        ft.commit();
    }

    public void startFragment(int item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("");
        if (item == 1) {
            MainFragment mainFragment = new MainFragment();
            ft.replace(R.id.test_replace, mainFragment);
        } else if (item == 2) {
            MySettingFragment mySettingFragment = new MySettingFragment();
            ft.replace(R.id.test_replace, mySettingFragment);
        } else if (item == 3) {
            SearchFragment searchFragment = new SearchFragment();
            ft.replace(R.id.test_replace, searchFragment);
        }
        ft.commit();
    }
}