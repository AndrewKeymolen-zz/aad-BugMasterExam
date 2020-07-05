package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.google.developer.bugmaster.reminders.AlarmReceiver;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        //Feature Task 11
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (Objects.equals(key, getResources().getString(R.string.pref_key_alarm)) || Objects.equals(key, getResources().getString(R.string.pref_key_reminder))) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_BOOT_COMPLETED);

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            }
        };
    }

    //Feature Task 11
    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(listener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmReceiver,
                intentFilter);
    }

    //Feature Task 11
    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(listener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alarmReceiver);
    }

}
