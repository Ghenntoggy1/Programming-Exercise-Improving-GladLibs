import edu.duke.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GladLib {
    private HashMap<String, ArrayList> myMap;
    private ArrayList<String> usedWordsList;
    
    private Random myRandom;
    
    private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "Examples/data";
    
    public GladLib(){
        myMap = new HashMap<String, ArrayList>();
        initializeFromSource(dataSourceDirectory);
        myRandom = new Random();
    }
    
    public GladLib(String source){
        initializeFromSource(source);
        myRandom = new Random();
    }
    
    private void initializeFromSource(String source) {
        String[] labels = {"country", "noun", "color", "adjective",
                           "name", "animal", "timeframe", "verb", 
                           "fruit"};
        for (String s : labels) {
            ArrayList<String> list = readIt(source + "/" + s + ".txt");
            myMap.put(s, list);
        }
        usedWordsList = new ArrayList<String>();
    }
    
    private String randomFrom(ArrayList<String> source){
        int index = myRandom.nextInt(source.size());
        return source.get(index);
    }
    
    private String getSubstitute(String label) {
        if (label.equals("number")){
            return ""+myRandom.nextInt(50)+5;
        }
        return randomFrom(myMap.get(label));
    }
    
    private String processWord(String w){
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        if (first == -1 || last == -1){
            return w;
        }
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        String sub = getSubstitute(w.substring(first+1,last));
        String newWord = prefix+sub+suffix;
        while (true) {
            if (!usedWordsList.contains(sub)) {
                usedWordsList.add(sub);
                break;
            }
            else {
                sub = getSubstitute(w.substring(first+1,last));
                newWord = prefix+sub+suffix;
            }
        }
        return newWord;
    }
    
    private void printOut(String s, int lineWidth){
        int charsWritten = 0;
        for(String w : s.split("\\s+")){
            if (charsWritten + w.length() > lineWidth){
                System.out.println();
                charsWritten = 0;
            }
            System.out.print(w+" ");
            charsWritten += w.length() + 1;
        }
    }
    
    private String fromTemplate(String source){
        String story = "";
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        return story;
    }
    
    private ArrayList<String> readIt(String source){
        ArrayList<String> list = new ArrayList<String>();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        return list;
    }
    
    private int totalWordsInMap () {
        int counter = 0;
        for (String category : myMap.keySet()) {
            ArrayList<String> wordsList = myMap.get(category);
            counter += wordsList.size();
        }
        return counter;
    }
    
    private int totalWordsConsidered () {
        int counter = 0;
        for (ArrayList list : myMap.values()) {
            for (String wordUsed : usedWordsList) {
                if (list.contains(wordUsed)) {
                    counter += list.size();
                    break;
                }
            }
        }
        return counter;
    }
    
    public void makeStory(){
        usedWordsList.clear();
        System.out.println("\n");
        String story = fromTemplate("Examples/data/madtemplate3.txt");
        printOut(story, 60);
        System.out.println("\nNumber of words that were replaced: " + usedWordsList.size());
        System.out.println("Total number of words that were possible to pick: " + totalWordsInMap());
        System.out.println("Total number of words of the categories that were used: " + totalWordsConsidered());
    }
}
