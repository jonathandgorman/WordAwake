package com.sonido.sonido;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/*----------------------------------------------------------------------------------------------------------------
* Author: Jonathan Gorman
* Date: 26/12/2016
*
* Description: This activity is started via a pending intent and is used to notify that the alarm is sounding.
* The activity appears over lock-screen, from which it can be dismissed or snoozed.
* ---------------------------------------------------------------------------------------------------------------*/

public class AlarmSounding extends AppCompatActivity implements TextToSpeech.OnInitListener
{
    private MediaPlayer mPlayer = null;
    private TextToSpeech tts = null;
    private float FULL_SPEECH_RATE = 1.0f;
    private float THREE_QUARTER_SPEECH_RATE = 0.75f;
    private float HALF_SPEECH_RATE = 0.5f;
    private float QUARTER_SPEECH_RATE = 0.25f;
    private String starterPhrase;
    private String initialWordOfDay;
    private String targetWordOfDay;
    private String endPhrase;
    private String initialLanguage;
    private String targetLanguage;
    private Runnable audioRunnable;
    private Thread audioThread;
    private boolean isSilenced = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Process extras received with intent
        Intent intentReceived = getIntent();
        starterPhrase = intentReceived.getStringExtra("START_PHRASE");
        endPhrase = intentReceived.getStringExtra("END_PHRASE");
        initialWordOfDay = intentReceived.getStringExtra("INITIAL_WORD");
        targetWordOfDay = intentReceived.getStringExtra("TARGET_WORD");
        initialLanguage = intentReceived.getStringExtra("INITIAL_LANGUAGE");
        targetLanguage =  intentReceived.getStringExtra("TARGET_LANGUAGE");

        // Gets access to the window, even if lockscreen has been enabled - does NOT require extra permissions
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Set view and any variable parts of the view
        setContentView(R.layout.alarm_sounding);
        TextView currentTimeText = (TextView) findViewById(R.id.currentTime);
        Calendar currCal = Calendar.getInstance();
        String hour = String.valueOf(currCal.get(Calendar.HOUR_OF_DAY)); // HOUR_OF_DAY ensures 24 hour clock is used
        String minute = String.valueOf(currCal.get(Calendar.MINUTE));
        if (Integer.valueOf(hour) < 10)
        {hour = "0" + String.valueOf(hour);}
        if (Integer.valueOf(minute) < 10)
        {minute = "0" + String.valueOf(minute);}
        currentTimeText.setText(hour + ":" + minute);

        tts = new TextToSpeech(AlarmSounding.this , this);
    }

    @Override
    public void onInit(int status){
        if(status == TextToSpeech.SUCCESS)
        {
            Log.d("AlarmSoundingActivity", "TextToSpeech enabled");

            // Maximise the volume of the alarm speech
            AudioManager phraseAudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int maxVolPhrase = phraseAudio.getStreamMaxVolume(phraseAudio.STREAM_MUSIC);
            phraseAudio.setStreamVolume(phraseAudio.STREAM_MUSIC, maxVolPhrase, 0);

            mPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.modern_alarm_clock);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            audioRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    // Loop until the user silences the alarm
                    while (isSilenced != true)
                    {
                        if (!tts.isSpeaking())
                        {
                            // Speak starter phrase
                            tts.setLanguage(new Locale(findLocale(initialLanguage)));
                            tts.speak(starterPhrase, TextToSpeech.QUEUE_ADD, null);

                            // Speak word of the day at two different speech rates
                            tts.setLanguage(new Locale(findLocale(targetLanguage)));
                            tts.speak(targetWordOfDay, TextToSpeech.QUEUE_ADD, null);

                            tts.setSpeechRate(QUARTER_SPEECH_RATE);
                            tts.speak(targetWordOfDay, TextToSpeech.QUEUE_ADD, null);

                            // Speak ending phrase
                            tts.setSpeechRate(FULL_SPEECH_RATE);
                            tts.setLanguage(new Locale(findLocale(initialLanguage)));
                            tts.speak(endPhrase + ":" + initialWordOfDay, TextToSpeech.QUEUE_ADD, null);

                        }
                    }
                }
            };

            audioThread = new Thread(audioRunnable);
            audioThread.start();

        }else{
            Log.d("myapp", "TextToSpeech not enabled. Accessing media player.");
            // Play alarm sound file
            mPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.modern_alarm_clock);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        }
    }

    // Given a language, finds the corresponding locale for audio
    public String findLocale(String language)
    {
        Log.d("LocaleFinding", "Finding locale " + language);
        String returnLocale = "";

        switch (language) {
            case "englishButton":
                returnLocale = "en_UK";
                break;
            case "spanishButton":
                returnLocale = "es_ES";
                break;
            case "swedishButton":
                returnLocale = "sv_SE";
                break;
            case "frenchButton":
                returnLocale = "fr_FR";
                break;
            case "germanButton":
                returnLocale = "de_DE";
                break;
            case "indianButton":
                returnLocale = "hi_IN";
                break;
            case "russianButton":
                returnLocale = "ru_RU";
                break;
            case "chineseButton":
                returnLocale = "zh_CN";
                break;
            case "japaneseButton":
                returnLocale = "ja_JP ";
                break;
            case "portugeseButton":
                returnLocale = "pt_PT";
                break;
            case "polishButton":
                returnLocale = "pl_PL";
                break;
            case "italianButton":
                returnLocale = "it_IT";
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + language);
        }

        return returnLocale;
    }

    // Called when the user hits the DISMISS button
    public void dismissAlarm(View view)
    {
        isSilenced = true;
        if (tts != null)
        {
            tts.stop(); // stop the TTS
            tts.shutdown(); // finish the TTS
            audioThread.interrupt(); // interrupt and stop the thread
            finish(); // finish the activity
        }else{
            mPlayer.stop(); //Stop the media player
            mPlayer.release(); // Release any resources
            mPlayer = null; // nullify the object
        }
    }

    // Called when the user hits the SNOOZE button - snooze time is defined by the user when they created the alarm, and can be edited
    public void snoozeAlarm(View view)
    {
        isSilenced = true;
        if (tts != null)
        {
            tts.stop(); // stop the TTS
            tts.shutdown(); // finish the TTS
            audioThread.interrupt(); // interrupt and stop the thread
            finish(); // finish the activity
        }else{
            mPlayer.stop(); //Stop the media player
            mPlayer.release(); // Release any resources
            mPlayer = null; // nullify the object
        }
    }
}
