package com.sonido.sonido;

public class WordOfDayGenerator
{
    private static String[] wordList = {"The banana", "The monkey", "The pineapple", "The boy", "The girl", "The mother", "The mother", "The mother", "The mother", "The dog", "The cow"
            , "The picture", "The brother", "The sister", "The joke", "The town", "The car", "The house", "The cowboy", "The light", "The moon", "The sun"
            , "The school", "The teacher", "The guitar", "The phone", "The computer", "The passport", "The apartment", "The pencil", "The pen", "The mountain", "The country"
            , "The hunger", "The feeling", "The language", "The alarm clock", "The lightning", "The food", "The drink", "The trip", "The airport", "The bus", "The baloon"};

    public String genWord()
    {
        String wordOfDay = wordList[(int) (Math.random() * wordList.length)];
        return wordOfDay;
    }

}
