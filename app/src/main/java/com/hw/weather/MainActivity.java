package com.hw.weather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hw.weather.fragment.MainFragment;
import com.hw.weather.fragment.MySettingFragment;
import com.hw.weather.fragment.SensorFragment;
import com.hw.weather.fragment.httpsRequest.MainWeather;
import com.hw.weather.fragment.search.SearchFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import static android.view.View.inflate;


public class MainActivity extends AppCompatActivity implements Constants {


    private CoordinatorLayout coordinatorLayout;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeatherFromServer(View view, String city) {
        try {
            String country = "RU";
            String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + WEATHER_API_KEY;
            final URL uri = new URL(WEATHER_URL);
            final Handler handler = new Handler();
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                    String result = getLines(in);
                    Gson gson = new Gson();
                    MainWeather weatherRequest = gson.fromJson(result, MainWeather.class);
                    handler.post(() -> saveSearchSetting(weatherRequest));
                } catch (Exception e) {
                    Snackbar.make(view, "Ошибка соединения", BaseTransientBottomBar.LENGTH_SHORT).show();
                    Log.e(TAG, "Ошибка соединения", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Snackbar.make(view, "Ошибка адресса", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка адресса", e);
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void saveSearchSetting(MainWeather MainWeather) {
        Double temp = MainWeather.getMain().getTemp() - 273.15;
        long press = MainWeather.getVisibility().longValue();
        Double wind = MainWeather.getWind().getSpeed().doubleValue();
        String up = MainWeather.getTimezone().toString();
        String tempString = temp.toString();
        Toast.makeText(getApplicationContext(), tempString, Toast.LENGTH_LONG).show();
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }


    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MainFragment mainFragment = new MainFragment();
            startFragment(mainFragment);

        } else if (id == R.id.nav_settings) {
            MySettingFragment mySettingFragment = new MySettingFragment();
            startFragment(mySettingFragment);

        } else if (id == R.id.nav_search) {
            SearchFragment searchFragment = new SearchFragment();
            startFragment(searchFragment);

        } else if (id == R.id.nav_sensor) {
            SensorFragment sensorFragment = new SensorFragment();
            startFragment(sensorFragment);

        } else if (id == R.id.nav_feedback) {
            Snackbar.make(findViewById(R.id.drawer_layout),"Feedback",Snackbar.LENGTH_LONG).setAction(" -> ",view -> {
                    String url = "https://MrAlex@gmail.com";
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
            }).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this,"Developed by MrAlex", Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        final SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                getWeatherFromServer(coordinatorLayout, query);
                Snackbar.make(searchText, query, Snackbar.LENGTH_LONG).show();
                return true;
            }

            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка выбора пункта меню приложения (активити)
        int id = item.getItemId();

        if (id == R.id.action_add) {
            return true;
        }

        if (id == R.id.action_clear) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initToolbar();
        initDrawer(toolbar);
        MainFragment mainFragment = new MainFragment();
        startFragment(mainFragment);
    }

    public void startFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.test_replace, fragment);
        ft.commit();
    }
}