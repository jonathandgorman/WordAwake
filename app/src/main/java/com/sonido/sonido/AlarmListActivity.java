package com.sonido.sonido;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class AlarmListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static ArrayList<AlarmListItem> alarmItems;
    public static ListView alarmListView;
    public static ListViewCustomAdapter alarmListAdapter;
    public String ALARM_LIST_STORAGE = "Alarm List Storage";

    private static final String ACTIVITY_TAG = "AlarmListActivity"; // Used for logging purposes

    // Default activity onCreate method called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(ACTIVITY_TAG, "1) Creating AlarmListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        /*
        *    Create the alarm list to hold AlarmListItems Objects
        */
        alarmListView = (ListView) findViewById(R.id.listView);
        alarmItems = new ArrayList<AlarmListItem>();

        /*
        * Set up the CustomAdapter to display the alarmItems
         */
        alarmListAdapter = new ListViewCustomAdapter(this, alarmItems);
        alarmListView.setAdapter(alarmListAdapter);

        // Set up parts of the view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This action button is used by the user to add another alarm, calls the
        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.fab);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SetAlarmActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.v(ACTIVITY_TAG, "2) AlarmListActivity created");
    }

    // Called when application is started
    @Override
    protected void onStart()
    {
        Log.v(ACTIVITY_TAG, "1) Starting AlarmListActivity");
        super.onStart();
        Log.v(ACTIVITY_TAG, "2) AlarmListActivity started");
    }

    // Called when application is paused
    @Override
    protected void onPause()
    {
        Log.v(ACTIVITY_TAG, "Pausing AlarmListActivity");
        saveAlarmList(); // save the alarm list, either as the user is leaving temporarily or permanently
        super.onPause();
        Log.v(ACTIVITY_TAG, "AlarmListActivity paused");
    }

    // Called when application is resumed
    @Override
    protected void onResume()
    {
        /*
        * On resuming the activity, the alarmList is first re-loaded and we check if alarm has been added.
        * If so, we add it to the alarmList. In any case, we force the adapter to update its values.
        * */

        Log.v(ACTIVITY_TAG, "1) Resuming AlarmListActivity");
        loadAlarmList();
        super.onResume();

        /*
        * Check if an intent has been received (only when coming from SetAlarm activity)
        */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            // Create a new alarm item and add the alarm data
            AlarmListItem alarmItem = new AlarmListItem();

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
            alarmItems.add(alarmItem);
            saveAlarmList();
        }

        alarmListAdapter.updateAlarmList(alarmItems);
        Log.v(ACTIVITY_TAG, "2) AlarmListActivity resumed");
    }

    // Saves the alarm list data - uses internal storage
    public void saveAlarmList()
    {
        try {
            Log.v(ACTIVITY_TAG, "1) Saving alarm list");
            FileOutputStream fos = openFileOutput(ALARM_LIST_STORAGE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(alarmItems);
            of.flush();
            of.close();
            fos.close();
            Log.v(ACTIVITY_TAG, "2) Alarm list saved - saved " + alarmItems.size() + " alarms");
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    // Loads the alarm list data - uses internal storage
    public void loadAlarmList()
    {
        try {

            Log.v(ACTIVITY_TAG, "1) Loading alarm list");
            FileInputStream fis = openFileInput(ALARM_LIST_STORAGE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            alarmItems = (ArrayList<AlarmListItem>) oi.readObject();
            oi.close();
            Log.v(ACTIVITY_TAG, "2) Alarm list loaded - loaded " + alarmItems.size() + " alarms");

        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        }catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

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

    // Overwritten function for view and layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.alarm_list, menu);
        return true;
    }

    // Overwritten function for view and layout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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





