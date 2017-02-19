package com.sonido.sonido;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static AlarmManager alarmManager;
    ProgressDialog progress;
    AlarmListItem alarmItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set default values upon creation
        initialOrTargetChange = "initial"; // set to initial
        initialLanguageName = "englishButton"; // default initial language icon
        targetLanguageName = "spanishButton"; // default initial language icon
        progress = new ProgressDialog(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        // 2)  Create an alarmManager using the system service that is provided with the context of the main activity class
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // take the ALARM_SERVICE

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
        // Create a new alarm item and add the alarm data
        alarmItem = new AlarmListItem(this, this.alarmManager);

        // Take primary information from the user
        EditText chosenAlarmName = (EditText) findViewById(R.id.alarmNameText);
        alarmItem.alarmName = chosenAlarmName.getText().toString();
        EditText chosenAlarmTime = (EditText) findViewById(R.id.alarmTimeText);
        alarmItem.alarmTime = chosenAlarmTime.getText().toString();
        alarmItem.initialLanguage = initialLanguageName;
        alarmItem.targetLanguage = targetLanguageName;

        // Take alarm active days
        CheckBox monday = (CheckBox) findViewById(R.id.mondayCheck);
        CheckBox tuesday = (CheckBox) findViewById(R.id.tuesdayCheck);
        CheckBox wednesday = (CheckBox) findViewById(R.id.wednesdayCheck);
        CheckBox thursday = (CheckBox) findViewById(R.id.thursdayCheck);
        CheckBox friday = (CheckBox) findViewById(R.id.fridayCheck);
        CheckBox saturday = (CheckBox) findViewById(R.id.saturdayCheck);
        CheckBox sunday = (CheckBox) findViewById(R.id.sundayCheck);
        alarmItem.mondayFlag = monday.isChecked();
        alarmItem.tuesdayFlag = tuesday.isChecked();
        alarmItem.wednesdayFlag = wednesday.isChecked();
        alarmItem.thursdayFlag = thursday.isChecked();
        alarmItem.fridayFlag = friday.isChecked();
        alarmItem.saturdayFlag = saturday.isChecked();
        alarmItem.sundayFlag = sunday.isChecked();

        // Add alarm options data
        Switch repeat = (Switch) findViewById(R.id.repeatSwitch);
        Switch vibrate = (Switch) findViewById(R.id.vibrateSwitch);
        Switch activate = (Switch) findViewById(R.id.activatedSwitch);
        Switch fade = (Switch) findViewById(R.id.fadeSwitch);
        SeekBar volume = (SeekBar) findViewById(R.id.volumeSeekBar);
        alarmItem.repeatFlag = repeat.isChecked();
        alarmItem.vibrateFlag = vibrate.isChecked();
        alarmItem.activatedFlag = activate.isChecked();
        alarmItem.alarmVolume = volume.getProgress();
        alarmItem.snoozeDuration = "5 Mins";
        alarmItem.fadeInFlag = fade.isChecked();

        SubAudioGenerator sub = new SubAudioGenerator();
        sub.execute();
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

    // Subclass
    class SubAudioGenerator extends AsyncTask<Void, String, Void>
    {
        public String initialLanguage;
        public String targetLanguage;
        public String startPhrase;
        public String endPhrase;
        public String wordOfDay;
        public String initialWord;
        public String finalWord;

        @Override
        protected void onPreExecute() {

            progress.setTitle("Please Wait");
            progress.setMessage("Downloading resources...");
            progress.setCancelable(false);
            progress.show();

        }
        @Override
        protected Void doInBackground(Void... params){

            wordOfDay = new WordOfDayGenerator().genWord();
            alarmItem.startPhrase = GenStartPhrase();
            alarmItem.endPhrase = GenEndPhrase();
            alarmItem.initialWordOfDay = GenInitialWord();
            alarmItem.targetWordOfDay = GenEndWord();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();

            // If alarm is flagged to be activated, set the alarm
            if (alarmItem.activatedFlag == true)
            {
                alarmItem.setAlarm();
            }

            // Finally, send an intent to the main activity with the new alarm object
            Intent setAlarmOk = new Intent(SetAlarmActivity.this, AlarmListActivity.class);
            setAlarmOk.putExtra("ALARM", alarmItem);
            setAlarmOk.setFlags(FLAG_ACTIVITY_CLEAR_TOP); // Note, this FLAG_ACTIVITY_CLEAR_TOP flag will ensure that the older AlarmListActivity will receive the intent and be updated. All other activitie above it, including this, will be destroyed
            startActivity(setAlarmOk);

        }

        // Generates the start phrase - E.G. Good Morning. The word of the day in _______ is:
        String GenStartPhrase() {
            String returnPhrase = "";
            switch (alarmItem.initialLanguage) {
                case "englishButton":
                    returnPhrase = "Good morning. The word of the day is: ";
                    break;
                case "spanishButton":
                    returnPhrase = "Buenos dias. La palabra del dia es: ";
                    break;
                case "swedishButton":
                    returnPhrase = "God morgon. Ordet för dagen är: ";
                    break;
                case "frenchButton":
                    returnPhrase = "Bonjour. Le mot du jour est:";
                    break;
                case "germanButton":
                    returnPhrase = "Guten Morgen. Das Wort des Tages ist:";
                    break;
                case "indianButton":
                    returnPhrase = "शुभ प्रभात। दिन का शब्द है:";
                    break;
                case "russianButton":
                    returnPhrase = "Доброе утро. Слово дня:";
                    break;
                case "chineseButton":
                    returnPhrase = "早上好。一天的话是：";
                    break;
                case "japaneseButton":
                    returnPhrase = "おはようございます。今日の言葉は：";
                    break;
                case "portugeseButton":
                    returnPhrase = "Bom Dia. A palavra do dia é:";
                    break;
                case "polishButton":
                    returnPhrase = "Dzień dobry. Słowo dnia jest:";
                    break;
                case "italianButton":
                    returnPhrase = "Buongiorno. La parola del giorno è il seguente:";
                    break;
                default:
                    throw new IllegalArgumentException("Error - Invalid language chosen: " + alarmItem.initialLanguage);
            }
            return returnPhrase;
        }
        // Generates the end phrase - E.G. Which means ...
        String GenEndPhrase() {
            String returnPhrase = "";
            switch (alarmItem.initialLanguage) {
                case "englishButton":
                    returnPhrase = "Which means: ";
                    break;
                case "spanishButton":
                    returnPhrase = "Que significa: ";
                    break;
                case "swedishButton":
                    returnPhrase = "Som betyder: ";
                    break;
                case "frenchButton":
                    returnPhrase = "Ce qui signifie: ";
                    break;
                case "germanButton":
                    returnPhrase = "Was bedeutet: ";
                    break;
                case "indianButton":
                    returnPhrase = "जिसका मतलब है: ";
                    break;
                case "russianButton":
                    returnPhrase = "Что значит: ";
                    break;
                case "chineseButton":
                    returnPhrase = "意思是: ";
                    break;
                case "japaneseButton":
                    returnPhrase = "つまり: ";
                    break;
                case "portugeseButton":
                    returnPhrase = "Que significa: ";
                    break;
                case "polishButton":
                    returnPhrase = "Co znaczy: ";
                    break;
                case "italianButton":
                    returnPhrase = "Che significa: ";
                    break;
                default:
                    throw new IllegalArgumentException("Error - Invalid language chosen: " + alarmItem.initialLanguage);
            }
            return returnPhrase;
        }
        // Generates the initial word - in the initial language
        String GenInitialWord()
        {
            TranslatorRunnable runnable = null;
            Thread thread;

            String returnPhraseUnicode = "";
            switch (alarmItem.initialLanguage) {
                case "englishButton":
                    returnPhraseUnicode = wordOfDay;
                    break;
                case "spanishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "swedishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "frenchButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "germanButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "indianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "japaneseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "portugeseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "polishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "italianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "chineseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "russianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.initialLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                default:
                    throw new IllegalArgumentException("Error - Invalid language chosen: " + alarmItem.initialLanguage);
            }

            while (returnPhraseUnicode.equals("ERROR"))
            {
                returnPhraseUnicode = runnable.getTranslatedText();
            }

            Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
            Matcher m = p.matcher(returnPhraseUnicode);
            StringBuffer returnPhraseASCII = new StringBuffer(returnPhraseUnicode.length());
            while (m.find()) {
                String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
                m.appendReplacement(returnPhraseASCII, Matcher.quoteReplacement(ch));
            }
            m.appendTail(returnPhraseASCII);

            Log.d("Unicode conversion", "Converted " + returnPhraseUnicode + " to " + returnPhraseASCII.toString());

            return returnPhraseASCII.toString();

        }
        // Generates the target word - in the target language
        String GenEndWord()
        {
            TranslatorRunnable runnable = null;
            Thread thread;

            String returnPhraseUnicode = "";
            switch (alarmItem.targetLanguage) {
                case "englishButton":
                    returnPhraseUnicode = wordOfDay;
                    break;
                case "spanishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "swedishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "frenchButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "germanButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "indianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "japaneseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "portugeseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "polishButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "italianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "chineseButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                case "russianButton":
                    runnable = new TranslatorRunnable(wordOfDay,alarmItem.targetLanguage);
                    thread = new Thread(runnable);
                    thread.start();
                    returnPhraseUnicode = runnable.getTranslatedText();
                    break;
                default:
                    throw new IllegalArgumentException("Error - Invalid language chosen: " + alarmItem.targetLanguage);
            }

            while (returnPhraseUnicode.equals("ERROR"))
            {
                returnPhraseUnicode = runnable.getTranslatedText();
            }

            Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
            Matcher m = p.matcher(returnPhraseUnicode);
            StringBuffer returnPhraseASCII = new StringBuffer(returnPhraseUnicode.length());
            while (m.find()) {
                String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
                m.appendReplacement(returnPhraseASCII, Matcher.quoteReplacement(ch));
            }
            m.appendTail(returnPhraseASCII);

            Log.d("Unicode conversion", "Converted " + returnPhraseUnicode + " to " + returnPhraseASCII.toString());
            return returnPhraseASCII.toString();
        }
    }
}