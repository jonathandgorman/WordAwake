package com.sonido.sonido;


public class AudioGenerator {

    private String initialLanguage;
    private String targetLanguage;

    AudioGenerator(String initialLanguage, String targetLanguage, String wordOfDay)
    {
        this.initialLanguage = initialLanguage;
        this.targetLanguage = targetLanguage;
    }

    // Generates the start phrase - E.G. Good Morning. The word of the day in _______ is:
    String GenStartPhrase()
    {
        String returnPhrase = "";
        switch (initialLanguage) {
            case "englishButton":
                returnPhrase = "Good morning. The word of the day is: ";
                break;
            case "spanishButton":
                returnPhrase = "Buenas dias. La palabra del dia es: ";
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
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage); }
        return returnPhrase;
    }

    // Generates the end phrase - E.G. Which means ...
    String GenEndPhrase()
    {
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
                returnPhrase = "Ce qui signifie:";
                break;
            case "germanButton":
                returnPhrase = "Was bedeutet:";
                break;
            case "indianButton":
                returnPhrase = "जिसका मतलब है:";
                break;
            case "russianButton":
                returnPhrase = "Что значит:";
                break;
            case "chineseButton":
                returnPhrase = "意思是:";
                break;
            case "japaneseButton":
                returnPhrase = "つまり:";
                break;
            case "portugeseButton":
                returnPhrase = "Que significa:";
                break;
            case "polishButton":
                returnPhrase = "Co znaczy:";
                break;
            case "italianButton":
                returnPhrase = "Che significa:";
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage); }
        return returnPhrase;
    }

    // Generates the initial word - in the initial language
    String GenInitialWord()
    {
        String returnPhrase = "";
        switch (initialLanguage) {
            case "englishButton":
                returnPhrase = "The Banana";
                break;
            case "spanishButton":
                returnPhrase = "El Platano";
                break;
            case "swedishButton":
                returnPhrase = "Bananen";
                break;
            case "frenchButton":
                returnPhrase = "La banane";
                break;
            case "germanButton":
                returnPhrase = "Die Banane";
                break;
            case "indianButton":
                returnPhrase = "केला";
                break;
            case "russianButton":
                returnPhrase = "Банан";
                break;
            case "chineseButton":
                returnPhrase = "香蕉";
                break;
            case "japaneseButton":
                returnPhrase = "バナナ";
                break;
            case "portugeseButton":
                returnPhrase = "A banana";
                break;
            case "polishButton":
                returnPhrase = "banan";
                break;
            case "italianButton":
                returnPhrase = "La banana";
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + initialLanguage);
        }
        return returnPhrase;
    }

    // Generates the target word - in the target language
    String GenEndWord()
    {
        String returnPhrase = "";
        switch (targetLanguage) {
            case "englishButton":
                returnPhrase = "The Banana";
                break;
            case "spanishButton":
                returnPhrase = "El Platano";
                break;
            case "swedishButton":
                returnPhrase = "Bananen";
                break;
            case "frenchButton":
                returnPhrase = "La banane";
                break;
            case "germanButton":
                returnPhrase = "Die Banane";
                break;
            case "indianButton":
                returnPhrase = "केला";
                break;
            case "russianButton":
                returnPhrase = "Банан";
                break;
            case "chineseButton":
                returnPhrase = "香蕉";
                break;
            case "japaneseButton":
                returnPhrase = "バナナ";
                break;
            case "portugeseButton":
                returnPhrase = "A banana";
                break;
            case "polishButton":
                returnPhrase = "banan";
                break;
            case "italianButton":
                returnPhrase = "La banana";
                break;
            default:
                throw new IllegalArgumentException("Error - Invalid language chosen: " + targetLanguage);
        }
        return returnPhrase;
    }
}
