import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Data structure to store a Spanish sentence, its POS tags, and its ongoing
 * English translation
 * -------------------------------------------------------------------------
 * Initialize this with something like:
 * TaggedSentence taggedSentence = new TaggedSentence(wordArray, POSArray);
 * where wordArray and POSArray are arrays of translated words and corresponding
 * tagged parts of speech.
 */
public class TaggedSentence{
	private class TaggedWord {
		public String englishWord;
		public String spanishWord;
		public String pos;
				
		public TaggedWord(String englishWord, String spanishWord, String pos) {
			this.englishWord = englishWord;
			this.spanishWord = spanishWord;
			this.pos = pos;
		}
	}
	
	private ArrayList<TaggedWord> sentence;
	private boolean question;
	private BigramCounts bigramCounts;
	
	public TaggedSentence(String[] englishArr, String[] spanishArr, String[] posArr) throws Exception {
		
		bigramCounts = new BigramCounts();
		
		if ((englishArr.length != spanishArr.length) ||
			(englishArr.length != posArr.length)) {
			throw new IllegalArgumentException("Arrays must be of same length");
		}
		sentence = new ArrayList<TaggedWord>();
		question = false;
		for (int i=0; i < englishArr.length; i++) {
			sentence.add(new TaggedWord(englishArr[i], spanishArr[i], posArr[i]));
			if (posArr[i].equals("Fia")) {
				question = true;
			}
		}
	}
	
	public TaggedSentence(String[] spanishArr, String[] posArr) throws Exception {
		this(new String[spanishArr.length], spanishArr, posArr);
	}
	
	public void addEnglishWords(String[] englishArr) {
		if (englishArr.length != sentence.size()) {
			throw new IllegalArgumentException("Arrays must be of same length");
		}
		for (int i=0; i < englishArr.length; i++) {
			sentence.get(i).englishWord = englishArr[i];
		}
	}
	
	public int length() {
		return sentence.size();
	}
	
	public boolean isQuestion() {
		return question;
	}
	
	public String getEnglish(int i) {
		return sentence.get(i).englishWord;
	}
	
	public void removeEnglish(int i) {
		sentence.remove(i);
	}
	
	public String getSpanish(int i) {
		return sentence.get(i).spanishWord;
	}
	
	public String getPos(int i) {
		return sentence.get(i).pos;
	}
	
	public void setEnglish(int i, String word) {
		sentence.get(i).englishWord = word;
	}
	
	/* The bigram count strategy!
	 * Vetos a swap if it does not produce a bigram with greater probability than the original
	 */
	public void swap(int i, int j) {
		
		
		String[] bigram1 = new String[2];
		bigram1[0] = this.getEnglish(i).toLowerCase();
		bigram1[1] = this.getEnglish(i+1).toLowerCase();
		int count1 = bigramCounts.numBigrams(bigram1);
		
		String[] bigram2 = new String[2];
		bigram2[0] = this.getEnglish(i+1).toLowerCase();
		bigram2[1] = this.getEnglish(i).toLowerCase();
		int count2 = bigramCounts.numBigrams(bigram2);
		

		Pattern p = Pattern.compile("(^[^\\p{L}]+)");
		Matcher m1 = p.matcher(this.getEnglish(i));
		Matcher m2 = p.matcher(this.getEnglish(i+1));
		if(!m1.find() && !m2.find() && count2 >= count1) {
			Collections.swap(sentence, i, j);
		}
		

	}
	
	/*
	 * Takes a rule of the form (part of speech, part of speech) and applies it
	 * to the entire sentence, swapping any adjacent words matching the rule.
	 */
	public void swapAllAdjacent(HashSet<String> posSet1, HashSet<String> posSet2) {
		for (int i=0; i < sentence.size()-1; i++) {
			if (posSet1.contains(this.getPos(i)) && posSet2.contains(this.getPos(i+1))) {
				swap(i, i+1);
				i++; // any word only gets swapped once
			}
		}
	}
	
	/*
	 * Returns Spanish sentence as a string array.
	 */
	public String[] sentence() {
		ArrayList<String> out = new ArrayList<String>();
		for (TaggedWord tWord : sentence) {
			out.add(tWord.spanishWord);
		}
		return out.toArray(new String[out.size()]);
	}
	
	public String[] translation() {
		ArrayList<String> out = new ArrayList<String>();
		for (TaggedWord tWord : sentence) {
			out.add(tWord.englishWord);
		}
		return out.toArray(new String[out.size()]);
	}
	
	public String[] tags() {
		ArrayList<String> out = new ArrayList<String>();
		for (TaggedWord tWord : sentence) {
			out.add(tWord.pos);
		}
		return out.toArray(new String[out.size()]);

	}
	
	/* Returns sentence as a String, for printing the final result! */
	public String sentenceString() {
		String ret = "";
		for(int i = 0; i < this.length(); i++) {
			ret += this.getEnglish(i) + " ";
		}
		ret = ret.replaceAll("¿ ", "¿").replaceAll(" \\?", "?").replaceAll("  ", " ").replaceAll(" \"", "\"").replaceAll(" :", ":").replaceAll(" ,", ",").replaceAll(" \\.", ".").replaceAll("\" ", "\"");
		if (ret.charAt(0) == '\"' || ret.charAt(0) == '¿') {
			return ret.substring(0, 1) + Character.toUpperCase(ret.charAt(1)) + ret.substring(2);
		} else {
			return Character.toUpperCase(ret.charAt(0)) + ret.substring(1);
		}
	}
	
	public static void main(String[] args) throws Exception {
		String[] englishArray = new String[] {"Hello", "I", "am", "a", "sentence"};
		String[] spanishArray = new String[] {"Hello", "I", "am", "a", "sentence"};
		String[] POSArray = new String[] {"A", "B", "C", "A", "B"};
		TaggedSentence taggedSentence = new TaggedSentence(englishArray,
														   spanishArray, POSArray);
		HashSet<String> a = new HashSet<String>();
		a.add("A");
		HashSet<String> b = new HashSet<String>();
		b.add("B");
		taggedSentence.swapAllAdjacent(a,b);
		System.out.println(Arrays.toString(taggedSentence.sentence()));

  }

}
