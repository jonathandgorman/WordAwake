package com.sonido.sonido;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 26/12/2016
*
* Description: XXX
* ---------------------------------------------------------------------------------------------------------------*/

public class AlarmReceiver extends BroadcastReceiver
{
    public Context genContext;
    public Intent genIntent;

    // Called when the intent is received from the alarm
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.genContext = context;
        this.genIntent = intent;

        // Check if the alarm is flagged to sound today
        if (soundToday() == true)
        {
            WakeLocker.acquire(context); // Firstly, acquire a wakelock in case the device is asleep

            Log.d("onReceive", "Alarm broadcast received");
            Intent alarmReceivedIntent = new Intent(context, AlarmSounding.class);

            alarmReceivedIntent.putExtra("START_PHRASE", intent.getStringExtra("START_PHRASE"));
            alarmReceivedIntent.putExtra("END_PHRASE", intent.getStringExtra("END_PHRASE"));
            alarmReceivedIntent.putExtra("INITIAL_WORD", intent.getStringExtra("INITIAL_WORD"));
            alarmReceivedIntent.putExtra("TARGET_WORD", intent.getStringExtra("TARGET_WORD"));
            alarmReceivedIntent.putExtra("INITIAL_LANGUAGE", intent.getStringExtra("INITIAL_LANGUAGE"));
            alarmReceivedIntent.putExtra("TARGET_LANGUAGE", intent.getStringExtra("TARGET_LANGUAGE"));
            alarmReceivedIntent.setFlags(FLAG_ACTIVITY_NEW_TASK); // Indicate that the activity is new and should be opened seperately in a new task
            context.startActivity(alarmReceivedIntent);

            WakeLocker.release(); // Finally, release the wakelock once the activity has been started
        }else{
            Log.d("debugMsg", "Alarm not scheduled to sound today");
        }
    }

    // Checks if the alarm is flagged to sound today
    public boolean soundToday()
    {
        ArrayList<String> activeDays = genIntent.getStringArrayListExtra("ACTIVE_ALARM_DAYS"); // Take the active alarm days from the intent

        // Get teh current day, and check if the day has been activated - where (Sun = 1, ..., Sat = 7)
        Calendar currCal = Calendar.getInstance();
        Log.d("onReceive", "Current day index (Sun = 1, Sat = 7): " + currCal.get(Calendar.DAY_OF_WEEK));
        Log.d("onReceive", "Current day active?: " + activeDays.get(currCal.get(Calendar.DAY_OF_WEEK) - 1));

        if (activeDays.get(currCal.get(Calendar.DAY_OF_WEEK) - 1).equals("true"))
        {
            return true; // Today marked as active
        }

       return false; // Today marked as false
    }
}