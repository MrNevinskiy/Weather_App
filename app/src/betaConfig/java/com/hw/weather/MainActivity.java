package com.hw.weather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hw.weather.broadcast.NetworkAlerts;
import com.hw.weather.broadcast.PowerAlerts;
import com.hw.weather.fragment.main.MainFragment;
import com.hw.weather.fragment.maps.MapsFragment;
import com.hw.weather.fragment.search.SearchFragment;
import com.hw.weather.fragment.sensor.SensorFragment;
import com.hw.weather.fragment.setting.MySettingFragment;
import com.hw.weather.fragment.weatherRequest.MainWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements Constants, SupportItemSelect {

    private NetworkAlerts networkAlerts = new NetworkAlerts();
    private PowerAlerts powerAlerts = new PowerAlerts();
    private CoordinatorLayout coordinatorLayout;
    private OpenWeatherByName openWeatherByName;
    private String text;


    private void initNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("3", "channel", importance);
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "getInstanceId failed", task.getException());
                return;
            }

            // Get Instance ID token
            String token = task.getResult().getToken();
            Log.d(TAG, token);
        });
    }

    private void registerBroadcastReceivers() {
        registerReceiver(networkAlerts, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        registerReceiver(powerAlerts, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherByName = retrofit.create(OpenWeatherByName.class);
    }

    private void requestRetrofit(String cityCountry, String keyApi) {
        openWeatherByName.loadWeather(cityCountry, keyApi)
                .enqueue(new Callback<MainWeather>() {
                    @Override
                    public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                        if (response.body() != null) {
                            Double temp = response.body().getMain().getTemp() + absoluteZero;
                            Toast.makeText(getApplicationContext(), temp.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
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
            startFragment(new MainFragment());

        } else if (id == R.id.nav_settings) {
            startFragment(new MySettingFragment());

        } else if (id == R.id.nav_search) {
            startFragment(new SearchFragment());

        } else if (id == R.id.nav_sensor) {
            startFragment(new SensorFragment());

        } else if (id == R.id.nav_map) {
            startFragment(new MapsFragment());

        } else if (id == R.id.nav_feedback) {
            Snackbar.make(findViewById(R.id.drawer_layout), "Feedback", Snackbar.LENGTH_LONG).setAction(" -> ", view -> {
                String url = "https://MrAlex@gmail.com";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Developed by MrAlex", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkAlerts);
        unregisterReceiver(powerAlerts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        final SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                text = query;
                requestRetrofit(query, WEATHER_API_KEY);
//                Snackbar.make(searchText, query, Snackbar.LENGTH_LONG).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initToolbar();
        initDrawer(toolbar);
        initRetrofit();
        registerBroadcastReceivers();
        initNotificationChannel();
        startFragment(new MainFragment());
    }

    @Override
    public void startFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.test_replace, fragment);
        ft.commit();
    }

    @Override
    public boolean NavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startFragment(new MainFragment());
                return true;
            case R.id.navigation_setting:
                startFragment(new MySettingFragment());
                return true;
            case R.id.navigation_search:
                startFragment(new SearchFragment());
                return true;
            case R.id.icon_about:
                Snackbar.make(findViewById(R.id.test_replace), "Developed by MrAlex / Designed by Dimas_sugih from Freepik", Snackbar.LENGTH_LONG).setAction("Перейти", view -> {
                    String url = "http://www.freepik.com";
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }).show();
                return true;
        }
        return false;
    }

}