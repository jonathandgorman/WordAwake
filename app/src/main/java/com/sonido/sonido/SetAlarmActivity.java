package com.sonido.sonido;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: The logic controlling the Set Alarm screen. Allows the user to create an alarm with options, etc.
* Alarm details are sent to the Alarm List which creates ana alarm and adds it to the list
* ---------------------------------------------------------------------------------------------------------------*/

public class SetAlarmActivity extends AppCompatActivity {

    public boolean answerName;
    public boolean answerDays;
    static public AlertDialog alert;
    public String initialOrTargetChange; // determines if the user is changing the target or the initial language
    public String initialLanguageName;
    public String targetLanguageName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set default values upon creation
        initialOrTargetChange = "initial"; // set to initial
        initialLanguageName = "englishButton"; // default initial language icon
        targetLanguageName = "spanishButton"; // default initial language icon

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        // Give the default time as the current system time
        final Calendar deviceCalendar = Calendar.getInstance();
        String hour = String.valueOf(deviceCalendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(deviceCalendar.get(Calendar.MINUTE));
        TextView timeTextView = (TextView) findViewById(R.id.alarmTimeText);

        // In the case that hour or minute is equal to "0" or is less than "10" and will not display correctly
        if (hour.equals("0"))
        {hour = "00";}
        else if (Integer.valueOf(hour) < 10)
        {hour = "0" + hour;}
        if (minute.equals("0"))
        {minute = "00";}
        else if (Integer.valueOf(minute) < 10)
        {minute = "0" + minute;}

        timeTextView.setText(hour + ":" + minute);
    }

    // Called when the user has hit "OK" to create their alarm
    public void okAlarm(View v)
    {
        // Ensures that every time that the "OK" alarm button is hit that all required fields are checked.
        answerDays = false;
        answerName = false;
        if (checkName() == true)  // checks if the user has chosen a empty name, or not
        {
            if (checkDaysEmpty() == true) { // checks if the user has not chosen at least 1 day
                createAlarm();
            }
        }
    }

    // Create the alarm intent according to the users choices - the last part called after the user presses okay
    public void createAlarm()
    {
        //Second, intent to change activity to alarm list screen
        Intent setAlarmOk = new Intent(this, AlarmListActivity.class);

        //Add primary alarm data
        EditText alarmName = (EditText) findViewById(R.id.alarmNameText);
        setAlarmOk.putExtra("NAME", alarmName.getText().toString());
        System.out.println("alarm name sent: " + alarmName.getText());
        EditText alarmTime = (EditText) findViewById(R.id.alarmTimeText);
        setAlarmOk.putExtra("TIME", alarmTime.getText().toString());

        // Add chosen languages
        setAlarmOk.putExtra("INITIAL", initialLanguageName);
        setAlarmOk.putExtra("TARGET", targetLanguageName);

        // Add active days data
        CheckBox monday = (CheckBox) findViewById(R.id.mondayCheck);
        setAlarmOk.putExtra("MONDAY", monday.isChecked());
        CheckBox tuesday = (CheckBox) findViewById(R.id.tuesdayCheck);
        setAlarmOk.putExtra("TUESDAY", tuesday.isChecked());
        CheckBox wednesday = (CheckBox) findViewById(R.id.wednesdayCheck);
        setAlarmOk.putExtra("WEDNESDAY", wednesday.isChecked());
        CheckBox thursday = (CheckBox) findViewById(R.id.thursdayCheck);
        setAlarmOk.putExtra("THURSDAY", thursday.isChecked());
        CheckBox friday = (CheckBox) findViewById(R.id.fridayCheck);
        setAlarmOk.putExtra("FRIDAY", friday.isChecked());
        CheckBox saturday = (CheckBox) findViewById(R.id.saturdayCheck);
        setAlarmOk.putExtra("SATURDAY", saturday.isChecked());
        CheckBox sunday = (CheckBox) findViewById(R.id.sundayCheck);
        setAlarmOk.putExtra("SUNDAY", sunday.isChecked());

        // Add alarm options data
        Switch repeat = (Switch) findViewById(R.id.repeatSwitch);
        setAlarmOk.putExtra("REPEAT", repeat.isChecked());
        Switch vibrate = (Switch) findViewById(R.id.vibrateSwitch);
        setAlarmOk.putExtra("VIBRATE", vibrate.isChecked());
        Switch activate = (Switch) findViewById(R.id.activatedSwitch);
        setAlarmOk.putExtra("ACTIVATE", activate.isChecked());
        Switch fade = (Switch) findViewById(R.id.fadeSwitch);
        setAlarmOk.putExtra("FADE", fade.isChecked());
        SeekBar volume = (SeekBar) findViewById(R.id.volumeSeekBar);
        setAlarmOk.putExtra("VOLUME", volume.getProgress());
        setAlarmOk.putExtra("DURATION","5 Mins");

        setAlarmOk.setFlags(FLAG_ACTIVITY_CLEAR_TOP); // Note, this FLAG_ACTIVITY_CLEAR_TOP flag will ensure that the older AlarmListActivity will receive the intent and be updated. All other activitie above it, including this, will be destroyed
        startActivity(setAlarmOk);
    }

    // User has hit "CANCEL" to cancel their alarm - connected to "CANCEL" button
    public void cancelAlarm(View v)
    {
        // Change activity to alarm list screen
        Intent setAlarmCancel = new Intent(this, AlarmListActivity.class);
        startActivity(setAlarmCancel);
    }

    // checks that the name field of the alarm is valid
    public boolean checkName()
    {
        EditText currText = (EditText) findViewById(R.id.alarmNameText);
        if (currText.getText().length() == 0)// Check if the name is empty, if so, show a warning
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your alarm does not have a name. Are you sure that you want to continue?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    doOnOkName();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    doOnCancelName();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }else{
            answerName = true;
        }
        return answerName;
    }

    // Checks that that the user has chosen at least 1 day to sound the alarm
    public boolean checkDaysEmpty()
    {
        CheckBox currMonday = (CheckBox) findViewById(R.id.mondayCheck);
        CheckBox currTuesday = (CheckBox) findViewById(R.id.tuesdayCheck);
        CheckBox currWednesday = (CheckBox) findViewById(R.id.wednesdayCheck);
        CheckBox currThursday = (CheckBox) findViewById(R.id.thursdayCheck);
        CheckBox currFriday = (CheckBox) findViewById(R.id.fridayCheck);
        CheckBox currSaturday = (CheckBox) findViewById(R.id.saturdayCheck);
        CheckBox currSunday = (CheckBox) findViewById(R.id.sundayCheck);

        // Check if all checkboxes are unchecked, if so displays a dialog message
        if (!currMonday.isChecked() && !currTuesday.isChecked() && !currWednesday.isChecked() && !currThursday.isChecked()
                && !currFriday.isChecked() && !currSaturday.isChecked() && !currSunday.isChecked())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No day has been chosen. Are you sure that you want to continue?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)
                {
                    doOnOkDays();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {
                    doOnCancelDays();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else {

            answerDays = true;
            createAlarm();
        }
        return answerDays;
    }

    // Set the answer flag according to the user's option at the pop up dialogs
    private void doOnOkName()
    {
        answerName = true;
        checkDaysEmpty(); // positive response, check the the empty days
    }

    private void doOnCancelName()
    {
        answerName = false;
    }

    private void doOnOkDays()
    {
        answerDays = true;
        createAlarm();// teh second check is positive, create te alarm
    }

    private void doOnCancelDays()
    {
        answerDays = false;
    }

    // Call the dialog fragment which allows the use to select the time
    public void analogSetTime(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    // A method which is called when the user wishes to select a new language
    public void changeLanguagePopup(View v)
    {
        if(v.getResources().getResourceEntryName(v.getId()).equals("initialLanguageImage"))
        {
            initialOrTargetChange = "initial"; // target language is changing
        }
        else
        {
            initialOrTargetChange = "target"; // target language is changing
        }

        LayoutInflater inflater= LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.language_choice_popup, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomTransparentDialog);
        alertDialog.setView(view);
        alert = alertDialog.create();
        alert.show();
    }

    // Take the current button image, and change it for the newly selected image using the button ID
    public void languageChange(View v)
    {
        ImageView img;
        if (initialOrTargetChange.equals("initial"))
        {
            img = (ImageView) findViewById(R.id.initialLanguageImage);
            initialLanguageName = v.getResources().getResourceEntryName(v.getId());

        }else
        {
            img = (ImageView) findViewById(R.id.targetLanguageImage);
            targetLanguageName = v.getResources().getResourceEntryName(v.getId());
        }

        switch (v.getResources().getResourceEntryName(v.getId()))
        {
            case "englishButton":
                img.setImageResource(R.drawable.englishicon);
                alert.dismiss();
                break;
            case "spanishButton":
                img.setImageResource(R.drawable.spanishicon);
                alert.dismiss();
                break;
            case "frenchButton":
                img.setImageResource(R.drawable.frenchicon);
                alert.dismiss();
                break;
            case "germanButton":
                img.setImageResource(R.drawable.germanicon);
                alert.dismiss();
                break;
            case "indianButton":
                img.setImageResource(R.drawable.indianicon);
                alert.dismiss();
                break;
            case "russianButton":
                img.setImageResource(R.drawable.russianicon);
                alert.dismiss();
                break;
            case "chineseButton":
                img.setImageResource(R.drawable.chineseicon);
                alert.dismiss();
                break;
            case "japaneseButton":
                img.setImageResource(R.drawable.japaneseicon);
                alert.dismiss();
                break;
            case "swedishButton":
                img.setImageResource(R.drawable.swedishicon);
                alert.dismiss();
                break;
            case "portugeseButton":
                img.setImageResource(R.drawable.portugeseicon);
                alert.dismiss();
                break;
            case "polishButton":
                img.setImageResource(R.drawable.polishicon);
                alert.dismiss();
                break;
            case "italianButton":
                img.setImageResource(R.drawable.italianicon);
                alert.dismiss();
                break;
            default:
                alert.dismiss();
                break;
        }
    }
}
