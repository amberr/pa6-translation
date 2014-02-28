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
	private HashSet<String> pronounSet;
	HashSet<String> nounSet;
	HashSet<String> conjSet;
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
		
		pronounSet = new HashSet<String>();
		pronounSet.add("P0");
		pronounSet.add("PD");
		pronounSet.add("PE");
		pronounSet.add("PI");
		pronounSet.add("PN");
		pronounSet.add("PP");
		pronounSet.add("PR");
		pronounSet.add("PT");
		pronounSet.add("PX");
		
		nounSet = new HashSet<String>();
		nounSet.add("NC");
		nounSet.add("NP");
		
		conjSet = new HashSet<String>();
		conjSet.add("CC");
		conjSet.add("CS");
	}
	
	/* Swap adj/noun strategy
	 * ---------------------------------------------
	 */
	private void switchAdjNouns(TaggedSentence tsentence) {
		HashSet<String> adjSet = new HashSet<String>();
		adjSet.add("AQ");
		tsentence.swapAllAdjacent(nounSet, adjSet);
	}
	
	/* Swap negation/verb strategy
	 * ---------------------------------------------
	 */
	private void switchNegation(TaggedSentence tsentence) {
		HashSet<String> negSet = new HashSet<String>();
		negSet.add("RN");

		tsentence.swapAllAdjacent(negSet, verbSet);
	}
	
	/* Swap object/verb strategy
	 * ---------------------------------------------
	 */
	private void switchObjVerbs(TaggedSentence tsentence) {
		HashSet<String> objSet = new HashSet<String>();
		objSet.add("PP"); // he, she, it
		objSet.add("P0"); // reflexives
		
		tsentence.swapAllAdjacent(objSet, verbSet);
	}
		
	/* Strategy to flip the verb and object in a question
	 * ---------------------------------------------
	 */
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
	
	/* Strategy to detect best translation for "por"
	 * ---------------------------------------------
	 */
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
	
	/* Strategy to detect best translation for "para"
	 * ---------------------------------------------
	 */
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

	/* Resolve ambiguity with the word "Que" Strategy
	 * ----------------------------------------------
	 */
	private void resolveQueAmbiguity(TaggedSentence taggedSentence) {
		for(int i = 0; i < taggedSentence.length(); i++) {
			if(taggedSentence.getSpanish(i).equals("que")) {
				HashSet<String> comparisonWords = new HashSet<String>();
				comparisonWords.add("más");
				comparisonWords.add("menos");
				
				HashSet<String> timeWords = new HashSet<String>(); // since, until, while
				timeWords.add("desde");
				timeWords.add("hasta");
				timeWords.add("mientras");
				
				if ((i > 0 && comparisonWords.contains(taggedSentence.getSpanish(i-1))) ||
					(i > 1 && comparisonWords.contains(taggedSentence.getSpanish(i-2)))) {
					taggedSentence.setEnglish(i, "than");
				} else if (i > 0 && timeWords.contains(taggedSentence.getSpanish(i-1))) {
					taggedSentence.setEnglish(i, "");
				}
			}
		}
	}
	
	/* Detect Common Phrases Strategy
	 * ------------------------------
	 */
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
		
	/* Fix Infinitive Strategy
	 * -----------------------
	 */
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

	/* Add pronoun to verb strategy
	 * ---------------------------
	 */
	public void addPronounToVerb(TaggedSentence tsentence) {
		HashSet<String> pronounVerbSet = new HashSet<String>();
		pronounVerbSet.add("VAI");
		pronounVerbSet.add("VMI");
		pronounVerbSet.add("VSI");
		pronounVerbSet.add("VAP");
		pronounVerbSet.add("VMP");
		pronounVerbSet.add("VSP");
		pronounVerbSet.add("VAS");
		pronounVerbSet.add("VMS");
		pronounVerbSet.add("VSS");
		
		for (int i = 2; i < tsentence.length()-1; i++) {
			if (pronounVerbSet.contains(tsentence.getPos(i)) && !nounSet.contains(tsentence.getPos(i-1)) 
									 						 && !nounSet.contains(tsentence.getPos(i-2))
															 && !pronounSet.contains(tsentence.getPos(i-1)) 
															 && !pronounSet.contains(tsentence.getPos(i+1))
															 && !tsentence.getEnglish(i-1).contains("\"") 
															 && !tsentence.getEnglish(i+1).contains("\"") 
															 ) {
				tsentence.setEnglish(i, "it " + tsentence.getEnglish(i));
			} 
		}
		
		// check first words
		int i = 1;
		if (pronounVerbSet.contains(tsentence.getPos(i)) && !nounSet.contains(tsentence.getPos(i-1))
														 && !pronounSet.contains(tsentence.getPos(i-1))
														 && !tsentence.getEnglish(i-1).contains("\"") 
													     && !tsentence.getEnglish(i+1).contains("\"")
													     && ((!tsentence.isQuestion()) )
													     ) {
			tsentence.setEnglish(i, "it " + tsentence.getEnglish(i));
		}
	}
	
	/* Remove reflexive strategy
	 * -------------------------
	 */
	private void removeReflexive(TaggedSentence tsentence) {
		for (int i = 0; i < tsentence.length() - 1; i++) {
			if (tsentence.getSpanish(i).equalsIgnoreCase("me") &&
				verbSet.contains(tsentence.getPos(i+1))) {
				String[] words = tsentence.getEnglish(i+1).split("\\s");
				if(Arrays.asList(words).contains("I")) {
					tsentence.setEnglish(i, "");
				}
			}
		}	
	}
	
	public void applyStrategies(TaggedSentence tsentence) {
		// Apply all of the strategies!
		removeReflexive(tsentence);
		resolveQueAmbiguity(tsentence);
		detectCommonPhrases(tsentence);
		switchAdjNouns(tsentence);
		switchNegation(tsentence);
		switchObjVerbs(tsentence);
		flipQuestionWord(tsentence);
		fixInfinitive(tsentence);
		porBy(tsentence);
		paraInOrderTo(tsentence);
		addPronounToVerb(tsentence);
		
		if (tsentence.getEnglish(0).equals("¿"))
			tsentence.removeEnglish(0);
	}

	/* Takes array of tokenized Spanish words, and creates array of corresponding
	 * English words from SpanishEnglishDictionary. Stores this in a TaggedSentence.
	 */
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
			}
		}
		tsentence.addEnglishWords(englishWords);
	}
	
	
	/* Reads in dev or test file, then performs direct translation, and then
	 * applies strategies and prints out the result.
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fr = new FileInputStream("../corpus/dev_sentences.txt");
//		FileInputStream fr = new FileInputStream("../corpus/test_sentences.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fr));
		String sentence = null;
		Translator translator = new Translator();
		while ((sentence = reader.readLine()) != null) {
			TaggedSentence tsentence = pp.process(sentence); // see Preprocessor
			translator.directTranslate(tsentence);
			translator.applyStrategies(tsentence);
			System.out.println(tsentence.sentenceString());
		}
		reader.close();
	}
}
