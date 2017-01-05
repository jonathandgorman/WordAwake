package com.sonido.sonido;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 13/10/2016
*
* Description: A custom adapter which acts as a bridge between the user's alarm data, and the primary list
* view of the application.
* ----------------------------------------------------------------+-----------------------------------------------*/

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

        // Inflate the view, and add the user alarm data for the new alarm view - then return it
        vi = inflater.inflate(R.layout.alarm_list_item, null);

        TextView alarmName = (TextView) vi.findViewById(R.id.alarmNameText);
        alarmName.setText(items.get(position).alarmName);

        TextView alarmTime = (TextView) vi.findViewById(R.id.alarmTimeText);
        alarmTime.setText(items.get(position).alarmTime);

        // check to determine the alarms active days
        TextView activeDays = (TextView) vi.findViewById(R.id.activeDays);

        // Check if everyday is chosen
        if (items.get(position).mondayFlag && items.get(position).tuesdayFlag && items.get(position).wednesdayFlag
                && items.get(position).thursdayFlag && items.get(position).fridayFlag && items.get(position).saturdayFlag
                    && items.get(position).sundayFlag)
        {
            activeDays.setText("Everyday");
        }
        // Otherwise, check if all weekdays are chosen
        else if (items.get(position).mondayFlag && items.get(position).tuesdayFlag && items.get(position).wednesdayFlag
                && items.get(position).thursdayFlag && items.get(position).fridayFlag
                && !items.get(position).saturdayFlag && !items.get(position).sundayFlag)
        {
            activeDays.setText("Weekdays");

        }
        // Otherwise, check if all weekends are chosen
        else if (!items.get(position).mondayFlag && !items.get(position).tuesdayFlag && !items.get(position).wednesdayFlag
                && !items.get(position).thursdayFlag && !items.get(position).fridayFlag
                && items.get(position).saturdayFlag && items.get(position).sundayFlag)
        {
            activeDays.setText("Weekend");
        }
        else
        {
            // Else, individual days have been chosen and shown as a combo
            String activeDaysText = "";
            if (items.get(position).mondayFlag)
            {activeDaysText = activeDaysText + "Mon ";}
            if (items.get(position).tuesdayFlag)
            {activeDaysText = activeDaysText + "Tue ";}
            if (items.get(position).wednesdayFlag)
            {activeDaysText = activeDaysText + "Wed ";}
            if (items.get(position).thursdayFlag)
            {activeDaysText = activeDaysText + "Thur ";}
            if (items.get(position).fridayFlag)
            {activeDaysText = activeDaysText + "Fri ";}
            if (items.get(position).saturdayFlag)
            {activeDaysText = activeDaysText + "Sat ";}
            if (items.get(position).sundayFlag)
            {activeDaysText = activeDaysText + "Sun ";}

            activeDays.setText(activeDaysText);
        }

        // Then set the activated switch accordingly
        Switch activateSwitch = (Switch) vi.findViewById(R.id.activatedSwitch);
        activateSwitch.setChecked(items.get(position).activatedFlag);

        if (items.get(position).initialLanguage != null)
        {
            // Set the language images
            ImageButton initialLanguageImage = (ImageButton) vi.findViewById(R.id.alarmInitialLanguageSmall);
            switch (items.get(position).initialLanguage) {
                case "englishButton":
                    initialLanguageImage.setImageResource(R.mipmap.englishiconsmall);
                    break;
                case "spanishButton":
                    initialLanguageImage.setImageResource(R.mipmap.spanishiconsmall);
                    break;
                case "frenchButton":
                    initialLanguageImage.setImageResource(R.mipmap.frenchiconsmall);
                    break;
                case "germanButton":
                    initialLanguageImage.setImageResource(R.mipmap.germaniconsmall);
                    break;
                case "chineseButton":
                    initialLanguageImage.setImageResource(R.mipmap.chineseiconsmall);
                    break;
                case "indianButton":
                    initialLanguageImage.setImageResource(R.mipmap.indianiconsmall);
                    break;
                case "italianButton":
                    initialLanguageImage.setImageResource(R.mipmap.italianiconsmall);
                    break;
                case "polishButton":
                    initialLanguageImage.setImageResource(R.mipmap.polishiconsmall);
                    break;
                case "russianButton":
                    initialLanguageImage.setImageResource(R.mipmap.russianiconsmall);
                    break;
                case "swedishButton":
                    initialLanguageImage.setImageResource(R.mipmap.swedishiconsmall);
                    break;
                case "portugeseButton":
                    initialLanguageImage.setImageResource(R.mipmap.portugeseiconsmall);
                    break;
                case "japaneseButton":
                    initialLanguageImage.setImageResource(R.mipmap.japaneseiconsmall);
                    break;
                default:
                    initialLanguageImage.setImageResource(R.mipmap.irishiconsmall);
                    break;
            }
        }


        if (items.get(position).targetLanguage != null) {
            // Set the language images
            ImageButton targetLanguageImage = (ImageButton) vi.findViewById(R.id.alarmTargetLanguageSmall);
            switch (items.get(position).targetLanguage) {
                case "englishButton":
                    targetLanguageImage.setImageResource(R.mipmap.englishiconsmall);
                    break;
                case "spanishButton":
                    targetLanguageImage.setImageResource(R.mipmap.spanishiconsmall);
                    break;
                case "frenchButton":
                    targetLanguageImage.setImageResource(R.mipmap.frenchiconsmall);
                    break;
                case "germanButton":
                    targetLanguageImage.setImageResource(R.mipmap.germaniconsmall);
                    break;
                case "chineseButton":
                    targetLanguageImage.setImageResource(R.mipmap.chineseiconsmall);
                    break;
                case "indianButton":
                    targetLanguageImage.setImageResource(R.mipmap.indianiconsmall);
                    break;
                case "italianButton":
                    targetLanguageImage.setImageResource(R.mipmap.italianiconsmall);
                    break;
                case "polishButton":
                    targetLanguageImage.setImageResource(R.mipmap.polishiconsmall);
                    break;
                case "russianButton":
                    targetLanguageImage.setImageResource(R.mipmap.russianiconsmall);
                    break;
                case "swedishButton":
                    targetLanguageImage.setImageResource(R.mipmap.swedishiconsmall);
                    break;
                case "portugeseButton":
                    targetLanguageImage.setImageResource(R.mipmap.portugeseiconsmall);
                    break;
                case "japaneseButton":
                    targetLanguageImage.setImageResource(R.mipmap.japaneseiconsmall);
                    break;
                default:
                    targetLanguageImage.setImageResource(R.mipmap.irishiconsmall);
                    break;
            }
        }
        return vi;
    }

    // Clears the current alarmList, and updates it with the argument alarmList
    public void updateAlarmList(List<AlarmListItem> newAlarmList) {
        items.clear();
        items.addAll(newAlarmList);
        this.notifyDataSetChanged();
    }

}