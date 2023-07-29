import java.util.HashMap;
import edu.duke.FileResource;

public class CodonCount {
    private HashMap<String, Integer> codonCount;
    
    public CodonCount () {
        codonCount = new HashMap<String, Integer>();
    }
    
    private void buildCodonMap (int start, String dna) {
        codonCount.clear();
        int currStop = start + 3;
        while (true) {
            if (currStop > dna.length()) {
                break;
            }
            String codon = dna.substring(start, currStop).toUpperCase();
            if (codonCount.containsKey(codon)) {
                codonCount.put(codon, codonCount.get(codon) + 1);
            }
            else {
                codonCount.put(codon, 1);
            }
            start = currStop;
            currStop += 3;
        }
    }
    
    private String getMostCommonCodon () {
        int maxValue = -1;
        int currValue;
        String freqCodon = "";
        for (String codon : codonCount.keySet()) {
            currValue = codonCount.get(codon);
            if (maxValue < currValue) {
                maxValue = currValue;
                freqCodon = codon;
            }
        }
        return freqCodon;
    }
    
    public void tester () {
        FileResource fr = new FileResource();
        String dna = fr.asString().trim();
        System.out.println("DNA Strand is: " + dna.toUpperCase());
        
        int start = 0;
        buildCodonMap(start, dna);
        
        int count;
        System.out.println("Reading frame starting with " + start + " results in " + codonCount.size() + " unique codons");
        
        String freqCodon = getMostCommonCodon();
        System.out.println("and most common codon is " + freqCodon + " with count " + codonCount.get(freqCodon));
        
        int lowerBound = 7;
        int upperBound = 7;
        System.out.println("Counts of codons between " + lowerBound + " and " + upperBound + " inclusive are:");
        for (String codon : codonCount.keySet()) {
            count = codonCount.get(codon);
            if (count >= lowerBound && count <= upperBound) {
                System.out.println(codon + " : " + codonCount.get(codon));
            }
        }
    }
}
