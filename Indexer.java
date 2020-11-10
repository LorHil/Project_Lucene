package com.lucene.fuzzy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class Indexer {
 private IndexWriter writer;
 public Indexer(String indexDirectoryPath) throws IOException{
	 //this directory will contain the indexes
	 Directory indexDirectory =
	 FSDirectory.open(new File(indexDirectoryPath));
	 //create the indexer
	 writer = new IndexWriter(indexDirectory,
	 new StandardAnalyzer(Version.LUCENE_36),true, //version of Lucene I use is 3.6.2
	 IndexWriter.MaxFieldLength.UNLIMITED);
	 }
	 public void close() throws CorruptIndexException, IOException{
		 try { 
		      writer.optimize(); 
		      writer.close();  // You must close the Index after updates are done
		    } catch (Exception e) { 
		      System.out.println("Got an Exception: " + e.getMessage()); 
		    } 
		  }
	 private Document getDoc(File file) throws IOException{
	 Document document = new Document();
	 //index file contents
	 Field contentField = new Field(LuceneConstants.CONTENTS,
	 new FileReader(file));
	 //index file name
	 Field fileNameField = new Field(LuceneConstants.FILE_NAME,
	 file.getName(),
	 Field.Store.YES,Field.Index.NOT_ANALYZED);
	 //index file path
	 Field filePathField = new Field(LuceneConstants.FILE_PATH,
	 file.getCanonicalPath(),
	 Field.Store.YES,Field.Index.NOT_ANALYZED);
	 document.add(contentField);
	 document.add(fileNameField);
	 document.add(filePathField);
	 return document;
	 }
	 private void indexFile(File file) throws IOException{
	 //System.out.println("Indexing "+file.getCanonicalPath()); //Only if you want to see whether all docs are indexed
	 Document document = getDoc(file);
	 writer.addDocument(document);
	 }
	 public int createIndex(String dataDirPath, FileFilter filter)
	 throws IOException{
	 //get all files in the data directory
	 File[] files = new File(dataDirPath).listFiles();
	 for (File file : files) {
	 if(!file.isDirectory()
	 && !file.isHidden()
	 && file.exists()
	 && file.canRead()
	 && filter.accept(file) //Here only text files are accepted
	 ){
	 indexFile(file);
	 }
	 }
	 return writer.numDocs(); //return the number of documents that are indexed
	 }
	}
