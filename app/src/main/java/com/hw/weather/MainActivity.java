package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String instanceState;
        Date date = new Date();
        Singleton singleton = Singleton.getInstance(date.toString());
        if(savedInstanceState == null){
            instanceState = "Первый запуск";
        }else{
            instanceState = "Повторный запуск";
        }
        Toast.makeText(this, onCreate + instanceState + singleton.value, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate + instanceState + singleton.value);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, onStart, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, onResume, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onResume);
        date();
        setting();
        city();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, onPause, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onPause);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, onStop, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, onDestroy, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onDestroy);
    }

    public void searchOnClick(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        Toast.makeText(this, "searchOnClick", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "searchOnClick");
        startActivity(intent);
    }

    public void settingOnClick(View view) {
        Intent intent = new Intent(this, MySettingsActivity.class);
        Toast.makeText(this, "settingOnClick", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "settingOnClick");
        startActivity(intent);
    }

    public void date() {
        TextView textView = findViewById(R.id.date);
        Date date = new Date();
        textView.setText(date.toString());
    }

    public void setting() {
        boolean checked1 = getIntent().getBooleanExtra("WS",false);
        findViewById(R.id.pressure).setVisibility(checked1 ? View.GONE : View.VISIBLE);

        boolean checked2 = getIntent().getBooleanExtra("PS",false);
        findViewById(R.id.windSpeed).setVisibility(checked2 ? View.GONE : View.VISIBLE);
    }

    public void city() {
        try {
            String city = getIntent().getExtras().getString("CITY");
            TextView textView = findViewById(R.id.city);
            textView.setText(city);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}