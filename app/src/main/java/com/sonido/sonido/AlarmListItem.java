package com.sonido.sonido;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: The AlarmListItem class is the "alarm" used in WordAwake, and stores all of the necessary info
* about the alarms created by the users.
* ---------------------------------------------------------------------------------------------------------------*/

// The details of the alarm list items which populate the list
public class AlarmListItem implements Serializable
{
    // General alarm variables
    public int position; // the position in the alarm list that the alarm object should be placed
    public boolean editedFlag;

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

    // Audio strings
    public String startPhrase;
    public String endPhrase;
    public String initialWordOfDay = "";
    public String targetWordOfDay = "";

    // Non serializable members
    transient public PendingIntent pendingAlarmIntent; // pending intent used to sound alarm
    transient public Intent intent;
    transient static public AlarmManager alarmManager;
    transient public Context context;

    // Takes context from the main activity - thus allowing us to access android alarm services.
    public AlarmListItem(Context context, AlarmManager alarmManager)
    {
        this.alarmManager = alarmManager;
        this.context = context;
        this.position = 0;
        this.editedFlag = false;
    }

    // Alarm list item constructor
    public void setAlarm()
    {
        // Split string time into hour and minute components for setting the alarm
        String[] timeParts = alarmTime.split(":");
        int alarmTimeHour = Integer.parseInt(timeParts[0]);
        int alarmTimeMinute = Integer.parseInt(timeParts[1]);

        // Create a calendar object in milliseconds that represents the current time that the alarm is set at
        Calendar currentCalendar = Calendar.getInstance(); // current calendar instance
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        // Create calendar object in milliseconds that represents the time that the alarm should sound
        Calendar alarmCalendar = Calendar.getInstance(); // calendar instance to be modified
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmTimeHour);
        alarmCalendar.set(Calendar.MINUTE, alarmTimeMinute);
        alarmCalendar.set(Calendar.SECOND, 0); // ensures that the alarm sounds on the state change of minute

        // LOGIC: in the case that the time of the alarmCalendar is LESS than that of the currentCalendar, the day of the alarmCalendar should be incremented by 1 day
        if (alarmCalendar.get(Calendar.HOUR) < currentCalendar.get(Calendar.HOUR))
        {
            alarmCalendar.add(Calendar.DATE, 1); // increment day by 1
        }
        else if((alarmCalendar.get(Calendar.HOUR) <= currentCalendar.get(Calendar.HOUR)) && (alarmCalendar.get(Calendar.MINUTE) <= currentCalendar.get(Calendar.MINUTE)))
        {
            alarmCalendar.add(Calendar.DATE, 1); // increment day by 1
        }

        // Work out the difference between current and alarm time in order to provide a toast notification
        long timeDifference = alarmCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
        System.out.println(alarmCalendar.getTimeInMillis() + " V.S " + currentCalendar.getTimeInMillis());
        System.out.println(timeDifference);
        long numberOfDays = (timeDifference/1000)/86400;
        long numberOfHours = ((timeDifference/1000)%86400)/3600;
        long numberOfMinutes = (((timeDifference/1000)%86400)%3600)/60;

        if (!initialWordOfDay.equals("ERROR") && !targetWordOfDay.equals("ERROR"))
        {
            // Create intent and add any information that is required at the RX to the intent
            intent = new Intent(this.context, AlarmReceiver.class); //Intent to AlarmReceiver class
            ArrayList<String> activeAlarmDays = new ArrayList<String>();
            activeAlarmDays.add(String.valueOf(sundayFlag));
            activeAlarmDays.add(String.valueOf(mondayFlag));
            activeAlarmDays.add(String.valueOf(tuesdayFlag));
            activeAlarmDays.add(String.valueOf(wednesdayFlag));
            activeAlarmDays.add(String.valueOf(thursdayFlag));
            activeAlarmDays.add(String.valueOf(fridayFlag));
            activeAlarmDays.add(String.valueOf(saturdayFlag));

            intent.putExtra("ACTIVE_ALARM_DAYS", activeAlarmDays);
            intent.putExtra("START_PHRASE", startPhrase);
            intent.putExtra("END_PHRASE", endPhrase);
            intent.putExtra("INITIAL_WORD", initialWordOfDay);
            intent.putExtra("TARGET_WORD", targetWordOfDay);
            intent.putExtra("INITIAL_LANGUAGE", initialLanguage);
            intent.putExtra("TARGET_LANGUAGE", targetLanguage);

            Log.d("ALARM_PHRASE_DATA", "DATA: " + startPhrase + ", " + endPhrase + ", " + initialWordOfDay + ", " + targetWordOfDay + ", " + initialLanguage + ", " + targetLanguage);

            // Add intent to pendingIntent and ensure that it is only set once - and then just updated
            pendingAlarmIntent = PendingIntent.getBroadcast(this.context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Sets the alarmClock for the specified time and using the specified pending intent
            this.alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(alarmCalendar.getTimeInMillis(),
                            this.pendingAlarmIntent), this.pendingAlarmIntent);

            // Create a toast notification to say that the alarm is activated and when it will activate
            if (numberOfDays > 0)
            {Toast.makeText(context, "Activated alarm will sound in " + numberOfDays + " Days " + numberOfHours + " Hours " + "and " + numberOfMinutes + " Minutes" , Toast.LENGTH_LONG).show();}
            else if (numberOfMinutes > 0)
            {Toast.makeText(context, "Activated alarm will sound in " + numberOfHours + " Hours " + "and " + numberOfMinutes + " Minutes" , Toast.LENGTH_LONG).show();}
            else
            {Toast.makeText(context, "Activated alarm will sound in " + numberOfMinutes + " Minutes" , Toast.LENGTH_LONG).show();}
        }
    }

    // Cancels the alarms pending intent, cancelling the alarm ...
    public void cancelAlarm()
    {
        pendingAlarmIntent.cancel();
        System.out.println("ALARM CANCELLED");
    }
}