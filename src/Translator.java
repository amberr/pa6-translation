import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Translator {
	
	private HashMap<String, String> dictionary;
	private HashSet<String> verbSet;
	private static Preprocessor pp;
	
	public Translator() throws Exception {
		pp = new Preprocessor();
		dictionary = new SpanishEnglishDictionary().dictionary();
		verbSet = new HashSet<String>();
		verbSet.add("VAG");
		verbSet.add("VAI");
		verbSet.add("VAM");
		verbSet.add("VAN");
		verbSet.add("VAP");
		verbSet.add("VAS");
		verbSet.add("VMG");
		verbSet.add("VMI");
		verbSet.add("VMM");
		verbSet.add("VMN");
		verbSet.add("VMP");
		verbSet.add("VMS");
		verbSet.add("VSG");
		verbSet.add("VSI");
		verbSet.add("VSM");
		verbSet.add("VSN");
		verbSet.add("VSP");
		verbSet.add("VSS");
	}
	
	private void switchAdjNouns(TaggedSentence tsentence) {
		HashSet<String> nounSet = new HashSet<String>();
		nounSet.add("NC");
		nounSet.add("NP");

		HashSet<String> adjSet = new HashSet<String>();
		adjSet.add("AQ");
		tsentence.swapAllAdjacent(nounSet, adjSet);
	}
	
	private void switchNegation(TaggedSentence tsentence) {
		HashSet<String> negSet = new HashSet<String>();
		negSet.add("RN");

		tsentence.swapAllAdjacent(negSet, verbSet);
	}
	
	private void switchObjVerbs(TaggedSentence tsentence) {
		HashSet<String> objSet = new HashSet<String>();
		objSet.add("PP"); // he, she, it

		tsentence.swapAllAdjacent(objSet, verbSet);
	}
		
	private void flipQuestionWord(TaggedSentence taggedSentence) {
		if (taggedSentence.isQuestion()) {
			String firstWord = taggedSentence.getEnglish(1);
			String[] words = firstWord.split(" ");
			String[] tags = pp.tagEnglishPOS(words);
			int modalIdx = Arrays.asList(tags).indexOf("MD");
			if (modalIdx > -1) {
				ArrayList<String> retArr = new ArrayList<String>(Arrays.asList(words));
				String modal = retArr.get(modalIdx);
				retArr.remove(modalIdx);
				retArr.add(0, modal);
				
				String retString = "";
				for (String s: retArr) {
					retString += s + " ";
				}
				taggedSentence.setEnglish(1, retString.substring(0, retString.length() - 1));
			}
			
		}
	}

	private void resolveQueAmbiguity(TaggedSentence taggedSentence) {
		for(int i = 0; i < taggedSentence.length(); i++) {
			if(taggedSentence.getSpanish(i).equals("que")) {
				HashSet<String> comparisonWords = new HashSet<String>();
				comparisonWords.add("m�s");
				comparisonWords.add("menos");
				if ((i > 0 && comparisonWords.contains(taggedSentence.getSpanish(i-1))) ||
					(i > 1 && comparisonWords.contains(taggedSentence.getSpanish(i-2)))) {
					taggedSentence.setEnglish(i, "than");
				}
			}
		}
	}
	
	public void detectCommonPhrases(TaggedSentence taggedSentence) {
		for(int i = 0; i < taggedSentence.length(); i++) {
			String word = taggedSentence.getSpanish(i);
			if(word.equals("embargo")) {
				if(i > 0 && taggedSentence.getSpanish(i - 1).equals("sin")) {
					taggedSentence.setEnglish(i-1, ""); // not deleting, so future loops don't get tripped up
					taggedSentence.setEnglish(i, "however");
				}
			} else if(word.toLowerCase().equals("debido")) {
				HashSet<String> toWords = new HashSet<String>();
				toWords.add("a");
				toWords.add("al");
				if(i < taggedSentence.length() - 1 && toWords.contains(taggedSentence.getSpanish(i+1))) {
					taggedSentence.setEnglish(i, "due");
					taggedSentence.setEnglish(i+1, "to");
				}
			}
		}
	}
	
	public void applyStrategies(TaggedSentence taggedSentence) {
		// apply all of the strategies!
		resolveQueAmbiguity(taggedSentence);
		detectCommonPhrases(taggedSentence);
		switchAdjNouns(taggedSentence);
		switchNegation(taggedSentence);
		switchObjVerbs(taggedSentence);
		flipQuestionWord(taggedSentence);
	}

	public void directTranslate(TaggedSentence tsentence) {
		String[] spanishWords = tsentence.sentence();
		String[] englishWords = new String[spanishWords.length];
		for (int i = 0; i < spanishWords.length; i++) {
			String spanishWord = spanishWords[i];
			String englishWord = dictionary.get(spanishWord.toLowerCase());
			if (englishWord != null) {
				englishWords[i] = englishWord;
			} else {
				englishWords[i] = spanishWords[i];
				//System.out.println(spanishWord);
			}
		}
		//System.out.println(Arrays.toString(englishWords));
		tsentence.addEnglishWords(englishWords);
	}
	
	
	public static void main(String[] args) throws Exception {
		// for each of the first five sentences, create array of tokens, map Spanish words
		// to array of English words. If the word does not exist in the dictionary, just place
		// the Spanish word in the new array (this means its a named entity).
		FileInputStream fr = new FileInputStream("dev_sentences.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fr));
		String sentence = null;
		Translator translator = new Translator();
		while ((sentence = reader.readLine()) != null) {
			TaggedSentence tsentence = pp.process(sentence);
			System.out.println(Arrays.toString(tsentence.sentence()));
			System.out.println(Arrays.toString(tsentence.tags()));
			translator.directTranslate(tsentence);
			translator.applyStrategies(tsentence);
			System.out.println(Arrays.toString(tsentence.translation()));
			System.out.println("-------------------------------\n-------------------------------");
		}
		reader.close();
	}
}
