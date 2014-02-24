import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Translator {
	
	public static void main(String[] args) throws IOException {
		HashMap<String, String> dictionary = new SpanishEnglishDictionary().dictionary();
		// for each of the first five sentences, create array of tokens, map Spanish words
		// to array of English words. If the word does not exist in the dictionary, just place
		// the Spanish word in the new array (this means its a named entity).
		
		BufferedReader reader = new BufferedReader(new FileReader("dev_sentences.txt"));
		String sentence = null;
		while ((sentence = reader.readLine()) != null) {
			translate(sentence.split("\\s"), dictionary);
		}
	}

	private static void translate(String[] spanishWords, HashMap<String, String> dictionary) {
		String[] englishWords = new String[spanishWords.length];
		for (int i = 0; i < spanishWords.length; i++) {
			String spanishWord = spanishWords[i].replaceFirst("^[¿\",\\.]+", "").replaceAll("[\"\\.,?`:]+$", "").toLowerCase();
			String englishWord = dictionary.get(spanishWord);
			if (englishWord != null) {
				englishWords[i] = englishWord;
			} else {
				englishWords[i] = spanishWords[i];
			}
		}
		System.out.println(Arrays.toString(englishWords));
	}
}
