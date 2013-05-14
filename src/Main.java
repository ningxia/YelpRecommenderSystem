import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import util.StanfordLemmatizer;
import util.TextIndexer;
import util.Tokenizer;

import model.Review;
import model.Votes;

import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

//import util.StanfordLemmatizer;

public class Main {
	
	public static void ReviewText(DB db) throws IOException {
		
		File f = new File("./data/" + "review.txt");
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
			bw.write(line.toLowerCase());
		}
		bw.close();
	}
	
	/**
	 * Test data clean.
	 * @param fileName
	 * @throws IOException
	 */
	public static void TestDataClean(String fileName) throws IOException {
		StanfordLemmatizer sl = new StanfordLemmatizer();
		File f = new File("./data/" + fileName);
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = "";
			while((line = br.readLine()) != null) {
				System.out.println(sl.lemmatizeString(Tokenizer.tokenizeLine(line)));
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Build index for generating TF-IDF weighted vector.
	 * @param db
	 * @throws IOException
	 */
	public static void BuildIndex(DB db) throws IOException {
		Morphia morphia = new Morphia();
		morphia.map(Review.class).map(Votes.class);
		DBCollection reviews = db.getCollection("review");
		DBCursor cursor = reviews.find();
		DBObject obj = null;
		String reviewId = "";
		String review = "";
		StanfordLemmatizer sl = new StanfordLemmatizer();
		TextIndexer index = new TextIndexer("./index/testIndex");
		while(cursor.hasNext()) {
			obj = cursor.next();
			reviewId = obj.get("review_id").toString();
			review = sl.lemmatizeString(Tokenizer.tokenizeLine(obj.get("text").toString()));
			index.addReview(reviewId, review);
		}
		index.closeIndex();
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
			
//			/** Test data clean. */
//			TestDataClean("review-100.txt");
			
//			/** Build idnex for generating TF-IDF weighted vector. */
//			BuildIndex(db);
			
			FSDirectory dir = FSDirectory.open(new File("./index/testIndex"));
			
			IndexReader reader = IndexReader.open(dir);
			System.out.println(reader.docFreq(new Term("review", "mary's")));
			System.out.println(reader.document(2).get("review"));
			
			
//			IndexSearcher searcher = new IndexSearcher(IndexReader.open(dir));
//			Term t = new Term("review", "great");
//			Query query = new TermQuery(t);
//			int hitsPerPage = 2000000;
//			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
//			searcher.search(query, collector);
//			ScoreDoc[] hits = collector.topDocs().scoreDocs;
//			
//			System.out.println("Found " + hits.length + " hits.");
//			for(int i=0;i<hits.length;++i) {
//			    int docId = hits[i].doc;
//			    Document d = searcher.doc(docId);
//			    System.out.println((i + 1) + ". " + d.get("review_id") + "\t" + d.get("review"));
//			}
			
			reader.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
