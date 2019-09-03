import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/*creates word bank from a given CSV file*/
public class WordBank {
    private final ArrayList<String> words = new ArrayList<>();

    public WordBank(String fileName) {
        try {
            Scanner scan = new Scanner(Paths.get(fileName));    /*open scanner for the given file*/
            scan.useDelimiter(", *");                   /*use comma as delimiter, scan till the next comma*/
            while (scan.hasNext())                              /*get next word*/
                words.add(scan.next());
            scan.close();                                       /*close scanner*/
        } catch (IOException e) {
            System.out.println("File not Found.");
            System.exit(0);
        }
    }

    /*return bank size*/
    public int getBankSize() {
        return words.size();
    }

    /*get word by index*/
    public String getWord(int index) {
        if (index < words.size() && index >= 0)
            return words.get(index);
        return null;
    }
}