package util;

import java.io.File;
import java.io.IOException;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TextIndexer {
	private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
	private static IndexWriter writer;
	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
	
	/**
	 * Constructor
	 * @param indexDir
	 * @throws IOException
	 */
	public TextIndexer(String indexDir) throws IOException {
		FSDirectory dir = FSDirectory.open(new File(indexDir));
		writer = new IndexWriter(dir, config);
	}
	
	public void addReview(String reviewId, String review) throws IOException {
		Document doc = new Document();
		doc.add(new StringField("review_id", reviewId, Field.Store.YES));
		doc.add(new TextField("review", review, Field.Store.YES));
		writer.addDocument(doc);
	}
	
	public void closeIndex() throws IOException {
		writer.close();
	}
	
}
