import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

/*
 * Initialize this with something like:
 * TaggedSentence taggedSentence = new TaggedSentence(wordArray, POSArray);
 * where wordArray and POSArray are arrays of translated words and corresponding
 * tagged parts of speech.
 */
public class TaggedSentence{
	public class TaggedWord {
		public String word;
		public String pos;
		
		public TaggedWord(String word, String pos) {
			this.word = word;
			this.pos = pos;
		}
	}
	
	ArrayList<TaggedWord> englishSentence;
	ArrayList<String> spanishSentence;
	
	public TaggedSentence(String[] spanishWordArr, String[] englishWordArr, String[] posArr) {
		englishSentence = new ArrayList<TaggedWord>();
		spanishSentence = new ArrayList<String>();
		for (int i=0; i < Math.min(englishWordArr.length, posArr.length); i++) {
			englishSentence.add(new TaggedWord(englishWordArr[i], posArr[i]));
			spanishSentence.add(spanishWordArr[i]);
		}
	}
	
	/*
	 * Takes a rule of the form (part of speech, part of speech) and applies it
	 * to the entire sentence, swapping any adjacent words matching the rule.
	 */
	public void swapAllAdjacent(String pos1, String pos2) {
		for (int i=0; i < englishSentence.size()-1; i++) {
			if (englishSentence.get(i).pos.equals(pos1) && englishSentence.get(i+1).pos.equals(pos2)) {
				Collections.swap(englishSentence, i, i+1);
			}
		}
	}
	
	/*
	 * Returns sentence as a string array.
	 */
	public String[] sentence() {
		ArrayList<String> out = new ArrayList<String>();
		for (TaggedWord tWord : englishSentence) {
			out.add(tWord.word);
		}
		return out.toArray(new String[out.size()]);
	}
	
	public String[] tags() {
		ArrayList<String> out = new ArrayList<String>();
		for (TaggedWord tWord : englishSentence) {
			out.add(tWord.pos);
		}
		return out.toArray(new String[out.size()]);
	}
	
//	public static void main(String[] args) {
//		String[] englishWordArray = new String[] {"Hello", "I", "am", "a", "sentence"};
//		String[] spanishWordArray = new String[] {"Hola", "yo", "soy", "una", "oración"};
//		String[] POSArray = new String[] {"A", "B", "C", "A", "B"};
//		TaggedSentence taggedSentence = new TaggedSentence(spanishWordArray, englishWordArray, POSArray);
//		taggedSentence.swapAllAdjacent("A", "B");
//		System.out.println(Arrays.toString(taggedSentence.sentence()));
//	}
}
