package com.sonido.sonido;

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
import java.util.ArrayList;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: The main activity of the application. This activity houses the primary alarm list used to display
*              all registered alarms. From here, the user may add, delete, or edit alarms.
*
* ---------------------------------------------------------------------------------------------------------------*/
public class AlarmListActivity extends AppCompatActivity
{
    private ArrayList<AlarmListItem> alarmItems;
    private ListView alarmListView;
    private ListViewCustomAdapter alarmListAdapter;
    private final String ACTIVITY_TAG = "AlarmListActivity"; // Used for logging purposes

    // Default activity onCreate method called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(ACTIVITY_TAG, "onCreate called in AlarmListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_alarm_list);

       // Create alarm list view and items
        alarmListView = (ListView) findViewById(R.id.listView);
        alarmItems = new ArrayList<AlarmListItem>();
        alarmListAdapter = new ListViewCustomAdapter(AlarmListActivity.this, alarmItems);
        alarmListView.setAdapter(alarmListAdapter);

        // Set up parts of the view - floating button and toolbar
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

    // Called by default when application is paused
    @Override
    protected void onPause() {
        Log.d(ACTIVITY_TAG, "onPause called in AlarmListActivity");
        alarmListAdapter.saveAlarmList(); // Save the alarm list, either as the user is leaving temporarily or permanently
        super.onPause();
    }

    // As AlarmListActivity is singleTop - all received intents go through onNewIntent
    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(ACTIVITY_TAG, "onNewIntent called in AlarmListActivity");
        super.onNewIntent(intent);

        // Take the new alarm intent from the SetAlarmActivity
        AlarmListItem alarmItem = (AlarmListItem) intent.getSerializableExtra("ALARM");

        // Check if the alarm is being edited or is new - determines whether the alarmList object is updated or added as new
        if(alarmItem.editedFlag == true)
        {
            alarmListAdapter.items.set(alarmItem.position, alarmItem);
        }else{
            alarmListAdapter.items.add(alarmItem.position, alarmItem);
        }

        // Add the new alarmItem and save the new list
        alarmListAdapter.saveAlarmList();
    }

    // Called by default when application is resumed
    @Override
    protected void onResume()
    {
        Log.d(ACTIVITY_TAG, "onResume called in AlarmListActivity");
        super.onResume();

        alarmListAdapter.loadAlarmList();        // Load the alarm list and then update the list view
        alarmItems = (ArrayList<AlarmListItem>) alarmListAdapter.items.clone(); // clone here to copy by value, not by reference
        alarmListAdapter.updateAlarmList(alarmItems);
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
            alarmListAdapter.resetAlarmList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}