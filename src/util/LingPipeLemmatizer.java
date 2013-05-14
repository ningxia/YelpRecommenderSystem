package util;

import java.util.ArrayList;
import java.util.List;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.Tokenization;
import com.aliasi.tokenizer.TokenizerFactory;

public class LingPipeLemmatizer {
	
	public static List<String> stemmingLingPipe(List<String> list) {
		TokenizerFactory tfIndoEuro = IndoEuropeanTokenizerFactory.INSTANCE;
		TokenizerFactory tfPorterStemmer = new PorterStemmerTokenizerFactory(tfIndoEuro);
		List<String> result = new ArrayList<String>();
		Tokenization tk;
		for(String token: list) {
			tk = new Tokenization(token, tfPorterStemmer);
			result.add(tk.tokens()[0]);
		}
		return result;
	}
	
}
