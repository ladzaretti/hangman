import java.util.Arrays;

public class Game {
    private String orgWord = WordChoose.randWord(new WordBank("Bank.csv")); // String containing the chosen word from
                                                                            // the words bank
    private String wordToGuess = orgWord.toLowerCase(); // change orgWord to lower case
    private StringBuffer abc = new StringBuffer("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"); // letter bank
    private char[] blankedWord = hideWord(wordToGuess).toCharArray(); // create blanked out representation of the given
                                                                      // word
    private final int numLettersToGuess = countAlpha(wordToGuess); // total number of letters to guess
    private int revealedChar = 0; // letters revealed counter
    private int guesses = 0; // guesses counter
    private Status gameOn = Status.Continue; // word reveal flag
    private int misses = 0;
    public static final int lives = 10; /* number of allowed mistakes */

    /*
     * create blanked word from the given string, changes all alphabetic characters
     * to an underscore
     */
    private String hideWord(String wordToChange) {
        return wordToChange.replaceAll("[\\w]", "_");
    }

    /* count alphabetic characters in the given string */
    private int countAlpha(String word) {
        int countAlpha = 0;
        for (int i = 0; i < word.length(); i++)
            if (Character.isLetter(word.charAt(i)))
                countAlpha++;
        return countAlpha;
    }

    /* returns a string from the blanked out char array containing the game word */
    public String printBlankedWord() {
        return Arrays.toString(blankedWord).replaceAll("[\\[\\],]", "");
    }

    /* reveal characters in the char array, if given ch exists in the org word. */
    public Boolean revealChar(char ch) {
        int indexInAbc = abc.indexOf((Character.toString(ch)).toUpperCase());
        if (indexInAbc != -1) {
            guesses++;// guess counts only if ch is still in the letters bank
            abc.setCharAt(indexInAbc, ' ');
        }
        int index = wordToGuess.indexOf(ch = Character.toLowerCase(ch));
        if (index == -1) {
            misses++;
            if (misses == lives)
                gameOn = Status.GameOver;
            return false;
        }
        while (index != -1 && indexInAbc != -1) {
            revealedChar++;
            blankedWord[index] = orgWord.charAt(index);
            index = wordToGuess.indexOf(ch, ++index);
        }
        if (revealedChar == numLettersToGuess) {
            gameOn = Status.Success;
        }
        return true;
    }

    /* return game status to the caller */
    public Status gameStatus() {
        return gameOn;
    }

    public int misses() {
        return misses;
    }

    public int revealed() {
        return revealedChar;
    }

    public int numGuesses() {
        return guesses;
    }

    public int getNumLettersToGuess() {
        return numLettersToGuess;
    }

    /* return the string representation of the abc bank array */
    public String printLetterBank() {
        return new String(abc);
    }
}