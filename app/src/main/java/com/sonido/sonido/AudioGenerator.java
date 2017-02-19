package com.sonido.sonido;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioGenerator extends Thread{

    public String initialLanguage;
    public String targetLanguage;
    public String startPhrase;
    public String endPhrase;
    public String wordOfDay;
    public String initialWord;
    public String finalWord;

    AudioGenerator(String initialLanguage, String targetLanguage) {
        this.initialLanguage = initialLanguage;
        this.targetLanguage = targetLanguage;
        this.wordOfDay = new WordOfDayGenerator().genWord();

        startPhrase = this.GenStartPhrase();
        endPhrase = this.GenEndPhrase();
        initialWord = this.GenInitialWord();
        finalWord = this.GenEndWord();
    }

  // Generates the start phrase - E.G. Good Morning. The word of the day in _______ is:
    String GenStartPhrase() {
        String returnPhrase = "";
        switch (initialLanguage) {
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
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage);
        }
        return returnPhrase;
    }

    // Generates the end phrase - E.G. Which means ...
    String GenEndPhrase() {
        String returnPhrase = "";
        switch (initialLanguage) {
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
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage);
        }
        return returnPhrase;
    }

    // Generates the initial word - in the initial language
    String GenInitialWord()
    {
        TranslatorRunnable runnable = null;
        Thread thread;

        String returnPhraseUnicode = "";
        switch (initialLanguage) {
            case "englishButton":
                returnPhraseUnicode = wordOfDay;
                break;
            case "spanishButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "swedishButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "frenchButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "germanButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "indianButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "japaneseButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "portugeseButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "polishButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "italianButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "chineseButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "russianButton":
                runnable = new TranslatorRunnable(wordOfDay,initialLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage);
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
        switch (targetLanguage) {
            case "englishButton":
                returnPhraseUnicode = wordOfDay;
                break;
            case "spanishButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "swedishButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "frenchButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "germanButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "indianButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "japaneseButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "portugeseButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "polishButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "italianButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "chineseButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            case "russianButton":
                runnable = new TranslatorRunnable(wordOfDay,targetLanguage);
                thread = new Thread(runnable);
                thread.start();
                returnPhraseUnicode = runnable.getTranslatedText();
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + targetLanguage);
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


