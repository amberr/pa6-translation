import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
		InputStream modelIn = new FileInputStream("opennlp-es-pos-maxent-pos-universal.model");
		POSModel posModel = new POSModel(modelIn);
		posTagger = new POSTaggerME(posModel);
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
			System.out.println(Arrays.toString(translator.posTag(spanishWords)));
			translator.directTranslate(sentence);
		}
	}

	public String[] tokenize(String sentence) {
		String[] words = sentence.split("\\s");
		String[] tokenized = new String[words.length];
		for (int i=0; i < words.length; i++) {
			String strippedWord = words[i].replaceFirst("^[�\",\\.]+", "").replaceAll("[\"\\.,?`:]+$", "").toLowerCase();
			tokenized[i] = strippedWord;
		}
		return tokenized;
	}
	
	public void directTranslate(String spanishSentence) {
		String[] spanishWords = tokenize(spanishSentence);
		String[] englishWords = new String[spanishWords.length];
		for (int i = 0; i < spanishWords.length; i++) {
			String spanishWord = spanishWords[i];
			String englishWord = dictionary.get(spanishWord);
			if (englishWord != null) {
				englishWords[i] = englishWord;
			} else {
				englishWords[i] = spanishWords[i];
			}
		}
		//System.out.println(Arrays.toString(englishWords));
	}
	
	public String[] posTag(String[] spanishWords) {
		return posTagger.tag(spanishWords);
	}
}
