package com.lucene.fuzzy;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.lucene.fuzzy.Indexer;
import com.lucene.fuzzy.TextFileFilter;


public class GlobalTester {
	
	 String indexDir = "C:\\lucene-3.6.2\\IndexLore";
	 String dataDir = "C:\\lucene-3.6.2\\DatasetLore";
	 Indexer indexer;
	 Searcher searcher;

   public static void main(String[] args) {
	   GlobalTester firsttester;
      
      try {
    	  
    	  firsttester = new GlobalTester();
    	  firsttester.createIndex();    	  
    	  
    	  BufferedReader bufread;
    		String choice = "";
    		System.out.println("*** Testing the document retrieval system ***");
    		System.out.println("Enter fuzzy query to search:");
    		
    		bufread = new BufferedReader(new InputStreamReader(System.in));
    		try {
    			choice = bufread.readLine();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
    			bufread.close();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    		
    		//Optional for phrases (multiple words)
    		
    	    String[] searcharray = choice.split(" ");
    	    for (int i = 0; i < searcharray.length; i++)
    	    {
    	    	System.out.println("Search results for word " + (i+1) + ": " + searcharray[i]);
    	    	firsttester.searchUsingFuzzyQuery(searcharray[i]);
    	    }
    		
     		 //Uncomment below line for only one word queries            
    		 //firsttester.searchUsingFuzzyQuery(choice);

         
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }
   
   private void createIndex() throws IOException{
	   indexer = new Indexer(indexDir);
	   int numIndexed;
	   long startTime = System.currentTimeMillis();
	   numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	   long endTime = System.currentTimeMillis();
	   indexer.close();
	   System.out.println(numIndexed+" File(s) indexed, time taken: "
	   +(endTime-startTime)+" ms");
	   }
   
   
   private void searchUsingFuzzyQuery(String searchQuery)
      throws IOException, ParseException{
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      //create a term to search file name
      //Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
      //Create a term to search in contents of the documents
      Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
      //create the term query object
      Query query = new FuzzyQuery(term);
      //search!
      
      TopDocs hits = searcher.search(query); //10 top documents
      long endTime = System.currentTimeMillis();

      System.out.println(hits.totalHits +
         " matching documents found. Time: " + (endTime - startTime) + "ms");
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDoc(scoreDoc);
         System.out.print("Score: "+ scoreDoc.score + " ");
         System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
      }
      searcher.close();
   }
}
