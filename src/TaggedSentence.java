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
	
	ArrayList<TaggedWord> sentence;
	
	public TaggedSentence(String[] wordArr, String[] posArr) {
		sentence = new ArrayList<TaggedWord>();
		for (int i=0; i < Math.min(wordArr.length, posArr.length); i++) {
			sentence.add(new TaggedWord(wordArr[i], posArr[i]));
		}
	}
	
	/*
	 * Takes a rule of the form (part of speech, part of speech) and applies it
	 * to the entire sentence, swapping any adjacent words matching the rule.
	 */
	public void swapAllAdjacent(String pos1, String pos2) {
		for (int i=0; i < sentence.size()-1; i++) {
			if (sentence.get(i).pos == pos1 && sentence.get(i+1).pos == pos2) {
				Collections.swap(sentence, i, i+1);
			}
		}
	}
	
	public String sentence() {
		StringBuilder sb = new StringBuilder();
		for (TaggedWord tWord : sentence) {
			sb.append(tWord.word);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String[] wordArray = new String[] {"Hello", "I", "am", "a", "sentence"};
		String[] POSArray = new String[] {"A", "B", "C", "A", "B"};
		TaggedSentence taggedSentence = new TaggedSentence(wordArray, POSArray);
		taggedSentence.swapAllAdjacent("A", "B");
		System.out.println(taggedSentence.sentence());
	}
}
