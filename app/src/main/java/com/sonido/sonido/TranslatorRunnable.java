package com.sonido.sonido;

import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslatorRunnable implements Runnable
{
    public String wordOfDay;
    public String language;

    public String translatedText = "ERROR";
    public String targetLangCode;
    public String sourceLangCode;

    TranslatorRunnable(String wordOfDay, String language)
    {
        this.wordOfDay = wordOfDay;
        this.language = language;

        // Provide the default source translation code
        sourceLangCode = "en";
        // Given a source and a target language, figure out the required language codes
        switch (language) {
            case "englishButton":
                targetLangCode = "en";
                break;
            case "spanishButton":
                targetLangCode = "es";
                break;
            case "swedishButton":
                targetLangCode = "sv";
                break;
            case "frenchButton":
                targetLangCode = "fr";
                break;
            case "germanButton":
                targetLangCode = "de";
                break;
            case "indianButton":
                targetLangCode = "hi";
                break;
            case "russianButton":
                targetLangCode = "ru";
                break;
            case "chineseButton":
                targetLangCode = "zh";
                break;
            case "japaneseButton":
                targetLangCode = "ja";
                break;
            case "portugeseButton":
                targetLangCode = "pt";
                break;
            case "polishButton":
                targetLangCode = "pl";
                break;
            case "italianButton":
                targetLangCode = "it";
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + language);
        }
    }

    public String getTranslatedText()
    {
        return translatedText;
    }

    @Override
    public void run()
    {
        runTranslation();
    }

    public synchronized void runTranslation()
    {
        URL url;
        HttpURLConnection urlConnection = null;
        try {

            String query = URLEncoder.encode(wordOfDay, "UTF-8");
            String languagePair = URLEncoder.encode(sourceLangCode + "|" + targetLangCode, "UTF-8");
            url = new URL("http://api.mymemory.translated.net/get?q=" + query + "&langpair=" + languagePair);
            System.out.println("Attempted URL for " + languagePair + " :" + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            String dataString = "";
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                dataString = dataString + current;
            }
            System.out.println("dataString: " + dataString);

            // Parse translated word from the dataString
            translatedText = dataString.substring(dataString.indexOf("translatedText\":\"") + 17, dataString.indexOf(",\"match") - 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.d("TranslationThread", "Finished text translation: " + translatedText);
            }
        }
    }
}
