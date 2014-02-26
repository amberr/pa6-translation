import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class Preprocessor {	
	private POSModel posModel;
	private POSTaggerME posTagger;
	
	public Preprocessor() throws Exception {
		InputStream modelIn = new FileInputStream("opennlp-es-pos-maxent-pos-universal.model");
		posModel = new POSModel(modelIn);
		posTagger = new POSTaggerME(posModel);
	}
	
	/*
	 * Tokenize and tag the sentence.
	 */
	public TaggedSentence process(String sentence) {
		ArrayList<String> tokenized = new ArrayList<String>();
		String[] words = sentence.split("\\s");
		for (int i=0; i < words.length; i++) {			
			Pattern p = Pattern.compile("(^[^\\p{L}]*)(\\p{L}+)([^\\p{L}]*$)");
			Matcher m = p.matcher(words[i]);
			while(m.find()) {
				for (int j = 1; j < 4; j++) {
					if (!m.group(j).equals("")) {
						tokenized.add(m.group(j));
					}
				}
			}
		}
		String[] spanishArr = new String[tokenized.size()];
		tokenized.toArray(spanishArr);
		String[] posArr = posTagger.tag(spanishArr);
	//	System.out.println(Arrays.toString(posArr));
		return new TaggedSentence(spanishArr, posArr);
	}	
}
