package com.sonido.sonido;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //Use the current time as the default values for the time picker
        final Calendar deviceCalendar = Calendar.getInstance();
        int hour = deviceCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = deviceCalendar.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int intHourOfDay, int intMinute) {

        String stringHourOfDay;
        String stringMinute;

        // Edit the int value if less than 10, as the prefixed "0" is removed
        if (intHourOfDay < 10)
        {stringHourOfDay = String.valueOf("0" + intHourOfDay);
        }else{
            stringHourOfDay = String.valueOf(intHourOfDay);
        }

        // Edit the int value if less than 10, as the prefixed "0" is removed
        if (intMinute < 10)
        {stringMinute = String.valueOf("0" + intMinute);
        }else{
            stringMinute = String.valueOf(intMinute);
        }

        //Get reference of host activity (XML Layout File) TextView widget
        TextView setTimeFieldFragment = (TextView) getActivity().findViewById(R.id.alarmTimeText);
        setTimeFieldFragment.setText(stringHourOfDay + ":" + stringMinute);
    }
}