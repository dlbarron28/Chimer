package com.davidbarron.chimer;


import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
    TimePicker timePicker;
    int hour;

    private static final String TAG = "TimePreference";
    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.time_pick);
        setPersistent(true);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        timePicker = (TimePicker) view.findViewById(R.id.time_pick);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(0);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(positiveResult) {
            hour = timePicker.getCurrentHour();
            persistInt(hour);
            Log.i(TAG,"Time Picker time: " + hour);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            hour = getPersistedInt(0);
        }
        else {
            hour = (Integer) defaultValue;
            persistInt(hour);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 1);
    }
}
