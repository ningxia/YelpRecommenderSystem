package util;

import java.util.ArrayList;
import java.util.Set;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.Tokenization;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.CollectionUtils;

public class Tokenizer {
	
	private static final Set<String> stopSet = CollectionUtils.asSet("!", ",", ".", ":");
	
	public static ArrayList<String> tokenizeLingPipe(String line) {
		// new IndoEuropeanTokenizerFactory instance
		TokenizerFactory tfIndoEuro = IndoEuropeanTokenizerFactory.INSTANCE;
		// create new object for lower case tokenizing
		TokenizerFactory tfLowerCase = new LowerCaseTokenizerFactory(tfIndoEuro);
		// create new object for English stop word list
		TokenizerFactory tfEngStop = new EnglishStopTokenizerFactory(tfLowerCase);
		TokenizerFactory tfStopFilter = new StopTokenizerFactory(tfEngStop, stopSet);
		// do the tokenization for line based on the created tokenizers
		Tokenization tk = new Tokenization(line, tfStopFilter);
		String[] tokens = tk.tokens();
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < tokens.length; i ++) {
			result.add(tokens[i]);
		}
		return result;
	}
	
	public static String removeStopWords(String document) {
		TokenizerFactory tfIndoEuro = IndoEuropeanTokenizerFactory.INSTANCE;
		TokenizerFactory tfEngStop = new EnglishStopTokenizerFactory(tfIndoEuro);
		Tokenization tk = new Tokenization(document, tfEngStop);
		String[] tokens = tk.tokens();
		String result = "";
		for(String token: tokens) {
			result = result.concat(token).concat(" ");
		}
		return result;
	}
	
	public static String tokenizeLine(String document) {
		return document.replaceAll("[\\.]+", ".").replaceAll("[^A-Za-z0-9\'\\.]", " ");
	}
	
	public static ArrayList<String> tokenize(String document) {
		String[] tokens = document.split("[^A-Za-z0-9\'\\.]+");
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < tokens.length; i ++) {
			result.add(tokens[i].toLowerCase());
		}
		return result;
	}
	
}
