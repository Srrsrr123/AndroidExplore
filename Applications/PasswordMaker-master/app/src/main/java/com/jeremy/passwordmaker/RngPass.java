package com.jeremy.passwordmaker;


import java.util.Random;

/**
 * Created by Jeremy on 20/06/2017.
 */

public class RngPass {

 // Random Pass Gen


    Arrays arrays = new Arrays();

    private String pass;
    private String newString;
    private int minLength;
     int min;
     int max;
    int rand;
    int random;

    int ranNumber;
    int randomLetters;
    int randomEng;
    int randomSym;
    int randomNum;
    int randomEngWords;
    int randomGerWords;

    private int letterLength;
    private int engLetterLength;
    private int symLength;
    private   int numlength = 6;
    private int englishWordLength;
    private int germanWordLength;

    boolean boolNoSymbols = false;
     boolean boolEnglishLetters = false;
     boolean boolNoNumbers = false;
     boolean boolEnglishWords = false;
     boolean boolGermanWords = false;



    public String newPass(String password) {

        pass = "";
        newString = "";

        Random rando = new Random();

        max = rando.nextInt(20) + 9;
        min = rando.nextInt(8) + 4;
        //Random number to be selected for max length of passwords

        //This would be updated in future so that then user can pick length of password + length of numbers/symbol

        ranNumber = rando.nextInt((max - min) + 6) + min;

        while (ranNumber < 7) {
            ranNumber = rando.nextInt((max - min) + 6) + min;
        }



        numlength = arrays.numbers.length;
        letterLength = arrays.letters.length;
        engLetterLength = arrays.englishLetters.length;
        symLength = arrays.symbols.length;
        englishWordLength = arrays.englishWords.length;
        germanWordLength = arrays.deutschWords.length;

        while (newString.length() < minLength){
            for (int x = 0; x < ranNumber; x++) {


                rand = (int) (Math.random() * 10);


//For loop to run through for passwords

                // This is not a switch as of yet due to using Booleans
                // may change it to a switch at a later date


                if (rand == 0 || rand == 5) {


                    if (!boolEnglishLetters && !boolGermanWords && !boolEnglishWords) {
                        random = (int) (Math.random() * letterLength);

                        newString += arrays.letters[random];

                    } else if (boolEnglishLetters) {
                        random = (int) (Math.random() * engLetterLength);

                        newString += arrays.englishLetters[random];

                    } else if (boolEnglishWords) {
                        random = (int) (Math.random() * englishWordLength);

                        newString += arrays.englishWords[random];
                    } else if (boolGermanWords) {
                        random = (int) (Math.random() * germanWordLength);

                        newString += arrays.deutschWords[random];
                    }
                } else if (rand == 1 && !boolNoNumbers) {
                    //New string adds new random # of numbers
                    random = (int) (Math.random() * numlength);

                    newString += arrays.numbers[random];


                } else if (rand == 3 && !boolNoSymbols || rand == 6 && !boolNoSymbols || rand == 9 && !boolNoSymbols) {
                    //New string adds new random # of symbols
                    random = (int) (Math.random() * symLength);

                    newString += arrays.symbols[random];



                }


            }
        // Random becomes the new password

    }
        return password = newString;

    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public boolean isBoolEnglishWords() {
        return boolEnglishWords;
    }

    public void setBoolEnglishWords(boolean boolEnglishWords) {
        this.boolEnglishWords = boolEnglishWords;
    }

    public boolean isBoolGermanWords() {
        return boolGermanWords;
    }

    public void setBoolGermanWords(boolean boolGermanWords) {
        this.boolGermanWords = boolGermanWords;
    }

    public boolean isBoolNoSymbols() {
        return boolNoSymbols;
    }

    public void setBoolNoSymbols(boolean boolNoSymbols) {
        this.boolNoSymbols = boolNoSymbols;

    }

    public boolean isBoolEnglishLetters() {
        return boolEnglishLetters;
    }

    public void setBoolEnglishLetters(boolean boolEnglishLetters) {
        this.boolEnglishLetters = boolEnglishLetters;
    }


    public boolean isBoolNoNumbers() {
        return boolNoNumbers;
    }

    public void setBoolNoNumbers(boolean boolNoNumbers) {
        this.boolNoNumbers = boolNoNumbers;
    }
}
