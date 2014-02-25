import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Translator {
	
	public HashMap<String, String> dictionary;
	public POSTaggerME posTagger;
	
	public Translator() throws Exception {
		dictionary = new SpanishEnglishDictionary().dictionary();
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
	
	public void switchAdjNouns(TaggedSentence tsentence) {
		HashSet<String> nounSet = new HashSet<String>();
		nounSet.add("NC");
		nounSet.add("NP");

		HashSet<String> adjSet = new HashSet<String>();
		adjSet.add("AQ");
		tsentence.swapAllAdjacent(nounSet, adjSet);
	}
	
	public static void main(String[] args) throws Exception {
		// for each of the first five sentences, create array of tokens, map Spanish words
		// to array of English words. If the word does not exist in the dictionary, just place
		// the Spanish word in the new array (this means its a named entity).
		
		BufferedReader reader = new BufferedReader(new FileReader("dev_sentences.txt"));
		String sentence = null;
		Translator translator = new Translator();
		Preprocessor pp = new Preprocessor();
		while ((sentence = reader.readLine()) != null) {
			TaggedSentence tsentence = pp.process(sentence);
			System.out.println(Arrays.toString(tsentence.sentence()));
			System.out.println(Arrays.toString(tsentence.posList()));
			translator.directTranslate(tsentence);
			translator.switchAdjNouns(tsentence);
			System.out.println(Arrays.toString(tsentence.translation()));
			System.out.println("-------------------------------\n-------------------------------");
		}
		reader.close();
	}
}
