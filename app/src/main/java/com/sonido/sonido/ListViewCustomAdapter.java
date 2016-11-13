package com.sonido.sonido;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: A custom adapter which is used acts as a bridge betwene the user's alarm data, and the primary list
* view of the application.
* ---------------------------------------------------------------------------------------------------------------*/

public class ListViewCustomAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    List<AlarmListItem> items;

    public ListViewCustomAdapter(Activity context, List<AlarmListItem> items)
    {
        super();
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Overwritten methods for handling the list
    @Override
    public int getCount()
    {
        return items.size();
    }
    @Override
    public Object getItem(int position)
    {
        return null;
    }
    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if(convertView == null)
        {
            // Inflate the view, and add the user alarm data for the new alarm view - then return it
            vi = inflater.inflate(R.layout.alarm_list_item, null);

            TextView alarmName = (TextView) vi.findViewById(R.id.alarmNameText);
            alarmName.setText(items.get(position).alarmName);

            TextView alarmTime = (TextView) vi.findViewById(R.id.alarmTimeText);
            alarmTime.setText(items.get(position).alarmTime);

            TextView activeDays = (TextView) vi.findViewById(R.id.activeDays);
            activeDays.setText("Everyday");

            /*
            CheckBox monday = (CheckBox) findViewById(R.id.mondayCheck);
            CheckBox tuesday = (CheckBox) findViewById(R.id.tuesdayCheck);
            CheckBox wednesday = (CheckBox) findViewById(R.id.wednesdayCheck);
            CheckBox thursday = (CheckBox) findViewById(R.id.thursdayCheck);
            CheckBox friday = (CheckBox) findViewById(R.id.fridayCheck);
            CheckBox saturday = (CheckBox) findViewById(R.id.saturdayCheck);
            CheckBox sunday = (CheckBox) findViewById(R.id.sundayCheck);
            */

            Switch activateSwitch = (Switch) vi.findViewById(R.id.activatedSwitch);
            activateSwitch.setChecked(items.get(position).activatedFlag);
        }
        return vi;
    }
}