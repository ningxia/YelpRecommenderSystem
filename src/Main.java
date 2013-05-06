import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import model.Review;
import model.Votes;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.Tokenization;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.CollectionUtils;

import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Main {
	
	private static File file = new File("");
	private static String absolutePath = file.getAbsolutePath();
	private static final Set<String> stopSet = CollectionUtils.asSet("!", ",", ".", ":");
	
	public static void ReviewText(DB db) throws IOException {
		
		File f = new File(absolutePath + "/data/" + "review.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		Morphia morphia = new Morphia();
		morphia.map(Review.class).map(Votes.class);
		DBCollection reviews = db.getCollection("review");
		DBCursor cursor = reviews.find();
		DBObject obj = null;
		String line = "";
		while(cursor.hasNext()) {
			obj = cursor.next();
//			System.out.println(obj.get("user_id") + "\t" + obj.get("business_id") + "\t" + obj.get("text"));
			line = /**obj.get("review_id") + "|" + obj.get("user_id") + "|" + obj.get("business_id") + "|" +*/ obj.get("text").toString();
			line = line.replaceAll("\\s*[\\r\\n]+\\s*", "") + "\n";
			bw.write(line);
		}
		bw.close();
	}
	
	public static ArrayList<String> tokenize(String line) {
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
	
	public static ArrayList<String> naiveTokenize(String line) {
		String[] tokens = line.split("[^A-Za-z0-9\']+");
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < tokens.length; i ++) {
			result.add(tokens[i].toLowerCase());
		}
		return result;
	}
	
	public static ArrayList<String> stemming(ArrayList<String> tokens) {
		TokenizerFactory tfIndoEuro = IndoEuropeanTokenizerFactory.INSTANCE;
		TokenizerFactory tfPorterStemmer = new PorterStemmerTokenizerFactory(tfIndoEuro);
		ArrayList<String> result = new ArrayList<String>();
		Tokenization tk;
		for(String token: tokens) {
			tk = new Tokenization(token, tfPorterStemmer);
			result.add(tk.tokens()[0]);
		}
		return result;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		try {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("yelp_db");
			boolean auth = db.authenticate("test", "test".toCharArray());
			if(!auth) {
				System.out.println("Authentication failed: " + auth);
			}
			
//			/** Create reviews text file. */
//			ReviewText(db);
			
			File f = new File(absolutePath + "/data/" + "review-test.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = "";
			while((line = br.readLine()) != null) {
				System.out.println(stemming(naiveTokenize(line)));
			}
			br.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
