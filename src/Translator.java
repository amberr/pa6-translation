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
		objSet.add("P0"); // reflexives
		
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
	
	private void porBy(TaggedSentence tsentence) {
		HashSet<String> participles = new HashSet<String>();
		participles.add("VAP");
		participles.add("VMP");
		participles.add("VSP");
		participles.add("AQ");
		for (int i=1; i < tsentence.length(); i++) {
			if (tsentence.getSpanish(i).equalsIgnoreCase("por") &&
					participles.contains(tsentence.getPos(i-1))) {
				tsentence.setEnglish(i, "by");
			 }
		}
	}
	
	private void paraInOrderTo(TaggedSentence tsentence) {
		HashSet<String> infinitives = new HashSet<String>();
		infinitives.add("VAN");
		infinitives.add("VMN");
		infinitives.add("VSM");
		for (int i=0; i < tsentence.length()-1; i++) {
			if (tsentence.getSpanish(i).equalsIgnoreCase("para") &&
					infinitives.contains(tsentence.getPos(i+1))) {
				String curr_inf = tsentence.getEnglish(i+1);
				tsentence.setEnglish(i, "to");
				if (curr_inf.split("\\s")[0].equalsIgnoreCase("to")) {
					tsentence.setEnglish(i+1, curr_inf.substring(3));
				}
			 }
		}
	}

	private void resolveQueAmbiguity(TaggedSentence taggedSentence) {
		for(int i = 0; i < taggedSentence.length(); i++) {
			if(taggedSentence.getSpanish(i).equals("que")) {
				HashSet<String> comparisonWords = new HashSet<String>();
				comparisonWords.add("más");
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
				if(i < taggedSentence.length() - 1 && taggedSentence.getSpanish(i + 1).equals("a")) {
					taggedSentence.setEnglish(i, "due");
					taggedSentence.setEnglish(i+1, "to");
				} else if (i < taggedSentence.length() - 1 && taggedSentence.getSpanish(i + 1).equals("al")) {
					taggedSentence.setEnglish(i, "due");
					taggedSentence.setEnglish(i+1, "to the");	
				}
				
			}
		}
	}
				
	private void fixInfinitive(TaggedSentence tsentence) {
		HashSet<String> prepositionSet = new HashSet<String>();
		prepositionSet.add("SP");
		
		HashSet<String> infinSet = new HashSet<String>();
		infinSet.add("VAN");
		infinSet.add("VMN");
		infinSet.add("VSN");
		
		// removes preposition (not 'to') before an infinitive ('for to go somewhere' vs 'to go somewhere')
		for (int i = 0; i < tsentence.length()-1; i++) {
			if (prepositionSet.contains(tsentence.getPos(i)) && infinSet.contains(tsentence.getPos(i+1))) {
				if (!tsentence.getPos(i).contains("to")){
					tsentence.removeEnglish(i);
				}
			}
		}
		
		// removes the second 'to' when two infinitives are adjacent ('to go to do something' vs 'to go do something')
		for (int i = 0; i < tsentence.length()-1; i++) {
			if (verbSet.contains(tsentence.getPos(i)) && infinSet.contains(tsentence.getPos(i+1))) {
				String infin = tsentence.getEnglish(i+1);
				if (infin.contains("to ")) {
					infin = infin.replace("to ", "");
				}
				tsentence.setEnglish(i+1, infin);
			}
		}
		
		HashSet<String> conjSet = new HashSet<String>();
		conjSet.add("CC");
		conjSet.add("CS");
		
		HashSet<String> puncSet = new HashSet<String>();
		puncSet.add(".");

		// removes 'to' when conjunction is between verb and infinitive 
		// ('could not understand nor to support' vs 'could not understand nor support')
		
		// case1: no punctuation before conjunction 
		for (int i = 0; i < tsentence.length()-2; i++) {
			if (verbSet.contains(tsentence.getPos(i)) && conjSet.contains(tsentence.getPos(i+1)) 
													  && infinSet.contains(tsentence.getPos(i+2))) {
				String infin = tsentence.getEnglish(i+2);
				if (infin.contains("to ")) {
					infin = infin.replace("to ", "");
				}
				tsentence.setEnglish(i+2, infin);
			}
		}
		// case2: punctuation is before conjunction
		for (int i = 0; i < tsentence.length()-3; i++) {
			if (verbSet.contains(tsentence.getPos(i)) && puncSet.contains(tsentence.getPos(i+1)) 
					 								  && conjSet.contains(tsentence.getPos(i+2)) 
					 								  && infinSet.contains(tsentence.getPos(i+3))) { 
				String infin = tsentence.getEnglish(i+3);
				if (infin.contains("to ")) {
					infin = infin.replace("to ", "");
				}
				tsentence.setEnglish(i+3, infin);
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
		fixInfinitive(taggedSentence);
		porBy(taggedSentence);
		paraInOrderTo(taggedSentence);
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
		FileInputStream fr = new FileInputStream("test_sentences.txt");
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
			System.out.println(tsentence.sentenceString());
			System.out.println("-------------------------------\n-------------------------------");
		}
		reader.close();
	}
}
