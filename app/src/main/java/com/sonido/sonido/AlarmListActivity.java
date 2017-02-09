package com.sonido.sonido;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: This is the main activity of the application and is used to display a list of all registered alarms for the device.
*              From here, the user can add, edit and delete alarms.
*
* ---------------------------------------------------------------------------------------------------------------*/

public class AlarmListActivity extends AppCompatActivity
{
    private static ArrayList<AlarmListItem> alarmItems;
    private static ListView alarmListView;
    private static ListViewCustomAdapter alarmListAdapter;
    private String ALARM_LIST_STORAGE = "Alarm List Storage";
    private static final String ACTIVITY_TAG = "AlarmListActivity"; // Used for logging purposes
    private static AlarmManager alarmManager;

    // Default activity onCreate method called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(ACTIVITY_TAG, "onCreate called in AlarmListActivity");
        setContentView(R.layout.app_bar_alarm_list);

        // 1)  Create the alarm list to hold AlarmListItems Objects
        alarmListView = (ListView) findViewById(R.id.listView);
        alarmItems = new  ArrayList<AlarmListItem>();

        // 2)  Create an alarmManager using the system service that is provided with the context of the main activity class
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // take the ALARM_SERVICE

        // 3) Set up the CustomAdapter to display the alarmItems
        alarmListAdapter = new ListViewCustomAdapter(this, alarmItems);
        alarmListView.setAdapter(alarmListAdapter);

        // 5) Set up parts of the view - floating button and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.fab);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SetAlarmActivity.class));
            }
        });
    }

    // Called when application is paused
    @Override
    protected void onPause() {
        saveAlarmList(); // save the alarm list, either as the user is leaving temporarily or permanently
        super.onPause();
    }

    // As AlarmListActivity is singleTop - all received intents go through onNewIntent
    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(ACTIVITY_TAG, "Entered onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent); // this discards the original Intent

        // If an intent is received from another activity
        if ((intent.getExtras() != null) && (intent.getStringExtra("TIME") != null))
        {


            Log.d(ACTIVITY_TAG, "Intent received, creating new alarm");
            Bundle extras = intent.getExtras();
            // Create a new alarm item and add the alarm data
            AlarmListItem alarmItem = new AlarmListItem(this, this.alarmManager);

            // Take primary information
            alarmItem.alarmName = extras.getString("NAME");
            alarmItem.alarmTime = extras.getString("TIME");
            alarmItem.initialLanguage = extras.getString("INITIAL");
            alarmItem.targetLanguage = extras.getString("TARGET");
            // Take days
            alarmItem.mondayFlag = extras.getBoolean("MONDAY");
            alarmItem.tuesdayFlag = extras.getBoolean("TUESDAY");
            alarmItem.wednesdayFlag = extras.getBoolean("WEDNESDAY");
            alarmItem.thursdayFlag = extras.getBoolean("THURSDAY");
            alarmItem.fridayFlag = extras.getBoolean("FRIDAY");
            alarmItem.saturdayFlag = extras.getBoolean("SATURDAY");
            alarmItem.sundayFlag = extras.getBoolean("SUNDAY");
            // Take options
            alarmItem.repeatFlag = extras.getBoolean("REPEAT");
            alarmItem.vibrateFlag = extras.getBoolean("VIBRATE");
            alarmItem.activatedFlag = extras.getBoolean("ACTIVATE");
            alarmItem.alarmVolume = extras.getInt("VOLUME");
            alarmItem.snoozeDuration = extras.getString("DURATION");
            alarmItem.fadeInFlag = extras.getBoolean("FADE");

            // Add to the alarm list, notify the adapter, and save the final list
            alarmItems.add(0, alarmItem); // Note,new element added to position "0"
            saveAlarmList();

            // If alarm is flagged to be activated, set the alarm
            if (alarmItem.activatedFlag == true)
            {
                alarmItem.setAlarm();
            }
        }
        alarmListAdapter.updateAlarmList(alarmItems);
    }

    // Called when application is resumed
    @Override
    protected void onResume()
    {
        Log.d(ACTIVITY_TAG, "onResume called in AlarmListActivity");
        super.onResume();
        loadAlarmList();
        alarmListAdapter.updateAlarmList(alarmItems);
    }

    // Saves the alarm list data - uses internal storage
    public void saveAlarmList() {
        try {
            FileOutputStream fos = openFileOutput(ALARM_LIST_STORAGE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(alarmItems);
            of.flush();
            of.close();
            fos.close();
            Log.v(ACTIVITY_TAG, "2) Alarm list saved - saved " + alarmItems.size() + " alarms");
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    // Saves the alarm list data - uses internal storage
    public void resetAlarmList() {
        try {
            FileOutputStream fos = openFileOutput(ALARM_LIST_STORAGE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            alarmItems.clear();
            of.writeObject(alarmItems);
            of.flush();
            of.close();
            fos.close();
            Log.v(ACTIVITY_TAG, "2) Alarm list reset - currently " + alarmItems.size() + " alarms");
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }

        // Then notify the adapter that the list has changed
        alarmListAdapter.updateAlarmList(alarmItems);
    }

    // Loads the alarm list data - uses internal storage
    public void loadAlarmList() {
        try {
            FileInputStream fis = openFileInput(ALARM_LIST_STORAGE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            alarmItems = (ArrayList<AlarmListItem>) oi.readObject();
            oi.close();
            Log.v(ACTIVITY_TAG, "2) Alarm list loaded - loaded " + alarmItems.size() + " alarms");

        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    // Overwritten function for view and layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_list, menu);
        return true;
    }

    // Overwritten function for view and layout, gives access to menu and contained items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {return true;}
        if (id == R.id.action_about)
        {return true;}
        if (id == R.id.action_faq)
        {return true;}

        if (id == R.id.action_delete_all)
        {
            resetAlarmList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}