package com.google.developer.bugmaster.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.developer.bugmaster.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    /* Schedule the alarm based on user preferences */
    public static void scheduleAlarm(Context context) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        String keyReminder = context.getString(R.string.pref_key_reminder);
        String keyAlarm = context.getString(R.string.pref_key_alarm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean enabled = preferences.getBoolean(keyReminder, false);

        //Intent to trigger
        Intent intent = new Intent(context, ReminderService.class);
        PendingIntent operation = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (enabled) {
            //Gather the time preference
            Calendar startTime = Calendar.getInstance();
            String alarmPref = preferences.getString(keyAlarm, "12:00");
            //Debugging Task 4
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmPref.substring(0, 2)));
            startTime.set(Calendar.MINUTE, Integer.parseInt(alarmPref.substring(3, 5)));
            startTime.set(Calendar.SECOND, 0);

            //Start at the preferred time
            //If that time has passed today, set for tomorrow
            if (Calendar.getInstance().after(startTime)) {
                startTime.add(Calendar.DATE, 1);
            }

            Log.d(TAG, "Scheduling quiz reminder alarm");
            //Debugging Task 5
            manager.setRepeating(AlarmManager.RTC, startTime.getTimeInMillis(), 86400000, operation);
        } else {
            Log.d(TAG, "Disabling quiz reminder alarm");
            manager.cancel(operation);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Schedule alarm on BOOT_COMPLETED
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            scheduleAlarm(context);
        }
    }

}
