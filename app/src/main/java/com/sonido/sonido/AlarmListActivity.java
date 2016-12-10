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
* Description: This is the main activity and is used to display a list of all registered alarms for the device.
*              From here, the user can add, edit and delete alarms.
*
* ---------------------------------------------------------------------------------------------------------------*/

public class AlarmListActivity extends AppCompatActivity
{
    public static ArrayList<AlarmListItem> alarmItems;
    public static ListView alarmListView;
    public static ListViewCustomAdapter alarmListAdapter;
    public String ALARM_LIST_STORAGE = "Alarm List Storage";

    public transient static AlarmManager alarmManager;

    private static final String ACTIVITY_TAG = "AlarmListActivity"; // Used for logging purposes

    // Default activity onCreate method called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_alarm_list);

        /*
        *   1)  Create the alarm list to hold AlarmListItems Objects
        */
        alarmListView = (ListView) findViewById(R.id.listView);
        alarmItems = new ArrayList<AlarmListItem>();

        /*
        * 2) Create ana alarmMnaager using the services which are only available in onCreate() method
        */
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /*
        *   2) Set up the CustomAdapter to display the alarmItems
        */
        alarmListAdapter = new ListViewCustomAdapter(this, alarmItems);
        alarmListView.setAdapter(alarmListAdapter);

        /*
        *   3) Set up parts of the view - floating button and toolbar
        */
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

    // Called when application is started
    @Override
    protected void onStart() {
        super.onStart();
    }

    // Called when application is paused
    @Override
    protected void onPause() {
        saveAlarmList(); // save the alarm list, either as the user is leaving temporarily or permanently
        super.onPause();
    }

    // Called when application is resumed
    @Override
    protected void onResume() {

        /*
        * On resuming the activity, the alarmList is first re-loaded and we check if alarm has been added.
        * If so, we add it to the alarmList. In any case, we force the adapter to update its values.
        * */
        loadAlarmList();
        super.onResume();

        /*
        * Check if an intent has been received (only when coming from SetAlarm activity)
        */
        Intent intent = getIntent();
        if (intent.hasExtra("TIME"))
        {
            Bundle extras = intent.getExtras();

            // Create a new alarm item and add the alarm data
            AlarmListItem alarmItem = new AlarmListItem(alarmManager);

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

            setIntent(null); // set the intent to null

            // If alarm is flagged to be activated, set the alarm
            if (alarmItem.activatedFlag == true)
            {
                alarmItem.setAlarm();
            }
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alarmListAdapter.updateAlarmList(alarmItems);
    }
}




























    /*
    // Overwritten function for view and layout
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    */


/*
    // Overwritten function for view and layout
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */





