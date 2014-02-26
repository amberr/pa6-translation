import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Translator {
	
	private HashMap<String, String> dictionary;
	private static Preprocessor pp;
	
	public Translator() throws Exception {
		pp = new Preprocessor();
		dictionary = new SpanishEnglishDictionary().dictionary();
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
		HashSet<String> verbSet = new HashSet<String>();
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
		
		tsentence.swapAllAdjacent(negSet, verbSet);
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

	public void applyStrategies(TaggedSentence taggedSentence) {
		// apply all of the strategies!
		switchAdjNouns(taggedSentence);
		switchNegation(taggedSentence);
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
