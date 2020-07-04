package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toast.makeText(this, onCreate, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate);
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

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void searchCity(View view) {
        EditText searchCity = (EditText)findViewById(R.id.entryCity);
        String city = searchCity.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CITY", city);
        Toast.makeText(this, "Город: " + city, Toast.LENGTH_LONG).show();
        Log.d(String.valueOf(this), onStart);
        startActivity(intent);

        //*****************************************************************//
        // игрался с Singleton
//        EditText searchCity = (EditText)findViewById(R.id.entryCity);
//        String city = searchCity.getText().toString();
//        Singleton singleton = Singleton.getInstance(city);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("CITY", singleton.value);
//        Toast.makeText(this, "Город: " + singleton.value, Toast.LENGTH_LONG).show();
//        Log.d(String.valueOf(this), onStart);
//        startActivity(intent);
        //*****************************************************************//

    }
}