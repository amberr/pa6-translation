import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class Preprocessor {	
	private POSModel englishPosModel;
	private POSModel spanishPosModel;
	private POSTaggerME englishPosTagger;
	private POSTaggerME spanishPosTagger;
	
	public Preprocessor() throws Exception {
		// Spanish tagset: http://www.lsi.upc.edu/~nlp/SVMTool/parole.html
		InputStream spanishModelIn = new FileInputStream("opennlp-es-pos-maxent-pos-universal.model");
		spanishPosModel = new POSModel(spanishModelIn);
		spanishPosTagger = new POSTaggerME(spanishPosModel);
		
		// English tagset: http://blog.dpdearing.com/2011/12/opennlp-part-of-speech-pos-tags-penn-english-treebank/
		InputStream englishModelIn = new FileInputStream("en-pos-maxent.bin");
		englishPosModel = new POSModel(englishModelIn);
		englishPosTagger = new POSTaggerME(englishPosModel);
	}
	
	public String[] tagEnglishPOS(String[] sentence) {
		return englishPosTagger.tag(sentence);
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
		String[] posArr = spanishPosTagger.tag(spanishArr);
	//	System.out.println(Arrays.toString(posArr));
		return new TaggedSentence(spanishArr, posArr);
	}	
}
