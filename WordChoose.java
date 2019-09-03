import java.security.SecureRandom;

/*create words bank from given text file and support for getting random word from it*/
public class WordChoose {
    public static String randWord(WordBank bank) {
        SecureRandom rand = new SecureRandom();
        return bank.getWord(rand.nextInt(bank.getBankSize()));
    }
}