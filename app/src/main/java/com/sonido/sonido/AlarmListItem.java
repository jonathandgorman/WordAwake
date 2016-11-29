package com.sonido.sonido;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: The alarmListItem class is the "alarm" used in WordAwake, and stores all of the neccesary info
* about the alarms created by the users.
* ---------------------------------------------------------------------------------------------------------------*/

import java.io.Serializable;

// The details of the alarm list items which populate the list
public class AlarmListItem implements Serializable
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

    // Alarm list item constructor
    AlarmListItem()
    {
    }


}