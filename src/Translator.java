import java.util.HashMap;

public class Translator {
	
	public static void main(String[] args) {
		HashMap<String, String> dictionary = new SpanishEnglishDictionary().dictionary();
		// for each of the first five sentences, create array of tokens, map Spanish words
		// to array of English words. If the word does not exist in the dictionary, just place
		// the Spanish word in the new array (this means its a named entity).
	}
}
