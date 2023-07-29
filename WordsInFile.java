import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import edu.duke.FileResource;
import edu.duke.DirectoryResource;

public class WordsInFile {
    private HashMap<String, ArrayList> wordCounter;
    
    public WordsInFile () {
        wordCounter = new HashMap<String, ArrayList>();
    }
    
    private void addWordsFromFile (File f) {
        String name = f.getName();
        FileResource fr = new FileResource(f);
        for (String word : fr.words()) {
            //word = word.toLowerCase();
            if (!wordCounter.containsKey(word)) {
                ArrayList<String> fileNames = new ArrayList();
                fileNames.add(name);
                wordCounter.put(word, fileNames);
            }
            else {
                ArrayList<String> fileNames2 = wordCounter.get(word);
                fileNames2.add(name);
                wordCounter.put(word, fileNames2);
            }
        }
    }
    
    private void buildWorldFileMap () {
        wordCounter.clear();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            addWordsFromFile(f);          
        }
    }
    
    private int maxNumber () {
        int maxNum = 0;
        int currNum;
        for (String word : wordCounter.keySet()) {
            currNum = wordCounter.get(word).size();
            if (maxNum < currNum) {
                maxNum = currNum;
            }
        }
        return maxNum;
    }
    
    private ArrayList<String> wordsInNumFiles (int number) {
        int currNum;
        ArrayList<String> wordsExactly = new ArrayList<String>();
        for (String word : wordCounter.keySet()) {
            currNum = wordCounter.get(word).size();
            if (currNum == number) {
                wordsExactly.add(word);
            }
        }
        return wordsExactly;
    }
    
    private void printFilesIn (String word) {
        ArrayList<String> names = wordCounter.get(word);
        for (String fileName : names) {
            System.out.println(fileName);
        }
    }
    
    public void tester () {
        buildWorldFileMap();
        System.out.println("====================================");
        int maxNumberOfFiles = maxNumber();
        System.out.println("Max number of files any word is in: " + maxNumberOfFiles);
        System.out.println("====================================");
        ArrayList<String> wordsExactly = wordsInNumFiles(4);
        System.out.println(wordsExactly.size());
        for (String word : wordsExactly) {
            System.out.print("\"" + word + "\" ");
        }
        System.out.println();
        printFilesIn("tree");
    }
}
