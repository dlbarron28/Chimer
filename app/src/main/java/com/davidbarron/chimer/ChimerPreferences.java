package com.davidbarron.chimer;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ChimerPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "ChimerPreferences";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean active = sharedPreferences.getBoolean(getResources().getString(R.string.alarm_run_switch), false);
        if (active) {
            int quietTimeStart = sharedPreferences.getInt(getResources().getString(R.string.quiet_time_start),getResources().getInteger(R.integer.default_start_hour));
            int quietTimeEnd = sharedPreferences.getInt(getResources().getString(R.string.quiet_time_end),getResources().getInteger(R.integer.default_end_hour));
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), ChimerAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            if(calendar.get(Calendar.HOUR_OF_DAY) >= quietTimeStart) {
                calendar.set(Calendar.HOUR_OF_DAY,quietTimeEnd);
                calendar.add(Calendar.DATE,1);
            }
            Date date = new Date();
            date.setTime(calendar.getTimeInMillis());
            Toast.makeText(getActivity(), "Next Alarm at " + date.toString(), Toast.LENGTH_SHORT).show();
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
         }
        else {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), ChimerAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        }
    }
}
