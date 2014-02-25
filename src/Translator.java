import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
//import opennlp.tools.chunker.ChunkerModel;
//import opennlp.tools.chunker.ChunkerME;

public class Translator {
	
	public HashMap<String, String> dictionary;
	public POSTaggerME posTagger;
//	public ChunkerME chunker;
	
	public Translator() throws Exception {
		dictionary = new SpanishEnglishDictionary().dictionary();
		InputStream modelIn = new FileInputStream("opennlp-es-pos-maxent-pos-universal.model");
		POSModel posModel = new POSModel(modelIn);
		posTagger = new POSTaggerME(posModel);
//		ChunkerModel chunkerModel = new ChunkerModel(new FileInputStream("en-chunker.bin"));
//		chunker = new ChunkerME(chunkerModel);
	}
	
	public static void main(String[] args) throws Exception {
		// for each of the first five sentences, create array of tokens, map Spanish words
		// to array of English words. If the word does not exist in the dictionary, just place
		// the Spanish word in the new array (this means its a named entity).
		BufferedReader reader = new BufferedReader(new FileReader("dev_sentences.txt"));
		String sentence = null;
		Translator translator = new Translator();
		while ((sentence = reader.readLine()) != null) {
			String[] spanishWords = translator.tokenize(sentence);
			TaggedSentence taggedSentence = translator.directTranslate(sentence);
			System.out.println(Arrays.toString(taggedSentence.sentence()));
			applyStrategies(taggedSentence);
		}
	}

	private static void applyStrategies(TaggedSentence taggedSentence) {
		// apply all of the strategies!
		
	}

	public String[] tokenize(String sentence) {
		String[] words = sentence.split("\\s");
		ArrayList<String> tokenized = new ArrayList<String>();
		for (int i=0; i < words.length; i++) {
			Pattern p = Pattern.compile("(^[\"�,\\.]*)([^\"�?,\\.]+)([\"\\.,?:]*$)");
			Matcher m = p.matcher(words[i]);
			while(m.find()) {
				for (int j = 1; j < 4; j++) {
					if (!m.group(j).equals("")) {
						tokenized.add(m.group(j));
					}
				}
			}
		}
		String[] arr = new String[tokenized.size()];
		return tokenized.toArray(arr);
	}
	
	public TaggedSentence directTranslate(String spanishSentence) {
		String[] spanishWords = tokenize(spanishSentence);
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
		return new TaggedSentence(spanishWords, englishWords, posTag(spanishWords));
		
	}
	
	public String[] posTag(String[] spanishWords) {
		return posTagger.tag(spanishWords);
	}
	
//	public String[] chunk(String[] spanishWords, String[] posTags) {
//		return chunker.chunk(spanishWords, posTags);
//	}
}
