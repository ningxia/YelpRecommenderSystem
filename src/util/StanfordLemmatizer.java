package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordLemmatizer {
	
	protected StanfordCoreNLP pipeline;
	
	public StanfordLemmatizer() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
		Properties props = new Properties();
//		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		// StanfordCoreNLP loads a lot of models, so you probably only want to do this once per execution
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public List<String> lemmatize(String document) {
		List<String> lemmas = new LinkedList<String>();
		
		// create an empty Annotation just with the given text
        Annotation annotation = new Annotation(document);
        
        // run all Annotators on this text
        this.pipeline.annotate(annotation);
        
        // Iterate over all of the sentences found
        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
        	// Iterate over all tokens in a sentence
        	for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
        		// Retrieve and add the lemma for each word into the list of lemmas
        		lemmas.add(token.get(LemmaAnnotation.class).replaceAll("'ll", " will"));
        	}
        }
        
		return lemmas;
	}
	
	public String lemmatizeString(String document) {
		String line = "";
		for(String s: lemmatize(document)) {
			if(s.matches("[^A-Za-z0-9]")) {
				continue;
			}
			else if(s.matches("[^A-Za-z0-9].+")) {
				line = line.concat(s);
			}
			else {
				line = line.concat(" ").concat(s);
//				line = line.concat(s);
			}
		}
		return line.trim();
	}
}
