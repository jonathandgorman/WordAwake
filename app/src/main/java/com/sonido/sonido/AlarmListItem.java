package com.sonido.sonido;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: The alarmListItem class is the "alarm" used in WordAwake, and stores all of the neccesary info
* about the alarms created by the users.
* ---------------------------------------------------------------------------------------------------------------*/

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Calendar;

// The details of the alarm list items which populate the list
public class AlarmListItem extends AppCompatActivity implements Serializable
{
    // Primary alarm information
    public String alarmTime;
    public String alarmName;
    public String initialLanguage;
    public String targetLanguage;

    // Alarm day flags
    public boolean mondayFlag;
    public boolean tuesdayFlag;
    public boolean wednesdayFlag;
    public boolean thursdayFlag;
    public boolean fridayFlag;
    public boolean saturdayFlag;
    public boolean sundayFlag;

    // Alarm options
    public boolean repeatFlag;
    public boolean vibrateFlag;
    public boolean activatedFlag;
    public int alarmVolume;
    public String snoozeDuration;
    public boolean fadeInFlag;

    public AlarmManager alarmManager;
    public PendingIntent alarmIntent; // pending intent used to sound alarm
    public Intent intent;

    // Takes context from the main activity - thus allowing us to access android alarm services.
    public AlarmListItem(AlarmManager alarmManager)
    {
        this.alarmManager = alarmManager;
    }

    // Alarm list item constructor
    public void setAlarm()
    {
        // Create calendar according to alarm
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());

        String[] timeParts = alarmTime.split(":");
        int alarmTimeHour = Integer.parseInt(timeParts[0]);
        int alarmTimeMinute = Integer.parseInt(timeParts[1]);

        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmTimeHour);
        alarmCalendar.set(Calendar.MINUTE, alarmTimeMinute);

        intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Alarm is scheduled to sound at EXACTLY the time mentioned and WAKEUP the device, then REPEATING at the shown interval
        this.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
                1000 * 60 * 1, alarmIntent);

        System.out.println("ALARM SET FOR 1 MIN");
    }

    public void cancelAlarm()
    {
        alarmIntent.cancel();
        System.out.println("ALARM CANCELLED");
    }
}