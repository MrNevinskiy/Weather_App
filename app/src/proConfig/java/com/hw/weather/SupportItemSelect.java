package com.hw.weather;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface SupportItemSelect {
    void startFragment(Fragment fragment);
    boolean NavigationItemSelected(@NonNull MenuItem item);
}
