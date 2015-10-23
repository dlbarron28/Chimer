package com.davidbarron.chimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ChimerBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Boot Received",Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean active = sharedPreferences.getBoolean(context.getResources().getString(R.string.alarm_run_switch), false);
        if(active) {
            int quietTimeStart = sharedPreferences.getInt(context.getResources().getString(R.string.quiet_time_start),context.getResources().getInteger(R.integer.default_start_hour));
            int quietTimeEnd = sharedPreferences.getInt(context.getResources().getString(R.string.quiet_time_end),context.getResources().getInteger(R.integer.default_end_hour));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(context, ChimerAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            if(calendar.get(Calendar.HOUR_OF_DAY) >= quietTimeStart) {
                calendar.set(Calendar.HOUR_OF_DAY,quietTimeEnd);
                calendar.add(Calendar.DATE, 1);
            }
            Date date = new Date();
            date.setTime(calendar.getTimeInMillis());
            Toast.makeText(context, "Next Alarm at " + date.toString(), Toast.LENGTH_SHORT).show();
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
}
