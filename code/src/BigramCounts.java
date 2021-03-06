import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

/* A class to read in bigram counts from http://www.ngrams.info */
public class BigramCounts {
	
	private HashMap<String, Integer> counts;
	
	public BigramCounts() throws Exception {
		counts = new HashMap<String, Integer>();
		FileInputStream fr = new FileInputStream("../corpus/bigrams.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fr));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split("\\s");
			String[] bigram = new String[2];
			System.arraycopy(tokens, 1, bigram, 0, 2);
			counts.put(Arrays.toString(bigram), Integer.parseInt(tokens[0]));
		}
	}
	
	/* Returns the number of bigrams matching the bigram passed in */
	public int numBigrams(String[] bigram) {
		if(!counts.containsKey(Arrays.toString(bigram))) {
			return 0;
		}
		return counts.get(Arrays.toString(bigram));
	}
	
}
