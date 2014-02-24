import java.util.Collections;
import java.util.ArrayList;

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
	public void SwapAllAdjacent(String pos1, String pos2) {
		for (int i=0; i < sentence.size()-1; i++) {
			if (sentence.get(i).pos == pos1 && sentence.get(i+1).pos == pos2) {
				Collections.swap(sentence, i, i+1);
			}
		}
	}
}
