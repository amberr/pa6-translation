import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;

/*
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
	
	public TaggedSentence(String[] englishArr, String[] spanishArr, String[] posArr) {
		if ((englishArr.length != spanishArr.length) ||
			(englishArr.length != posArr.length)) {
			throw new IllegalArgumentException("Arrays must be of same length");
		}
		sentence = new ArrayList<TaggedWord>();
		question = false;
		for (int i=0; i < englishArr.length; i++) {
			sentence.add(new TaggedWord(englishArr[i], spanishArr[i], posArr[i]));
			if (posArr[i] == "Fit") {
				question = true;
			}
		}
	}
	
	public TaggedSentence(String[] spanishArr, String[] posArr) {
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
	
	public String getSpanish(int i) {
		return sentence.get(i).spanishWord;
	}
	
	public String getPos(int i) {
		return sentence.get(i).pos;
	}
	
	public void setEnglish(int i, String word) {
		sentence.get(i).englishWord = word;
	}
	
	public void swap(int i, int j) {
		Collections.swap(sentence, i, j);
	}
	
	/*
	 * Takes a rule of the form (part of speech, part of speech) and applies it
	 * to the entire sentence, swapping any adjacent words matching the rule.
	 */
	public void swapAllAdjacent(HashSet<String> posSet1, HashSet<String> posSet2) {
		for (int i=0; i < sentence.size()-1; i++) {
			if (posSet1.contains(this.getPos(i)) && posSet2.contains(this.getPos(i+1))) {
				this.swap(i, i+1);
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
	
	public static void main(String[] args) {
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
//	public static void main(String[] args) {
//		String[] englishWordArray = new String[] {"Hello", "I", "am", "a", "sentence"};
//		String[] spanishWordArray = new String[] {"Hola", "yo", "soy", "una", "oraci�n"};
//		String[] POSArray = new String[] {"A", "B", "C", "A", "B"};
//		TaggedSentence taggedSentence = new TaggedSentence(spanishWordArray, englishWordArray, POSArray);
//		taggedSentence.swapAllAdjacent("A", "B");
//		System.out.println(Arrays.toString(taggedSentence.sentence()));
//	}
}
