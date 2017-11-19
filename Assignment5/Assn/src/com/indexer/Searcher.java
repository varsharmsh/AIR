package com.indexer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class Searcher 
{
	public static void displayDocs(IndexSearcher searcher,TopDocs hits) throws CorruptIndexException, IOException
	{
		System.out.println("Number of hits : " + hits.scoreDocs.length);
		for(ScoreDoc d : hits.scoreDocs)
		{
			System.out.println(searcher.doc(d.doc).get(Constants.titleField));
		}
	}
	public static Query getPhraseSearchQuery(String phrase)
	{
		int slope = Integer.parseInt(phrase.split("~")[1]);
		phrase = phrase.replaceAll("\\~[0-9]+", "");
		System.out.println(phrase);
		String[] words = phrase.split(" ");
		PhraseQuery query = new PhraseQuery();
		query.setSlop(slope);
		for(String w : words)
		{
			query.add(new Term(Constants.contentField,w));
		}
		System.out.println(query);
		return query;
	}
	public static Query getRegularSearchQuery(String query)
	{
		return new TermQuery(new Term(Constants.contentField,query));
	}
	public static Query getWildcardSearchQuery(String expression)
	{
		return new WildcardQuery(new Term(Constants.contentField,expression));
	}
	public static Query getBooleanSearchQuery(String queryString) throws ParseException
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36,Constants.contentField,analyzer);
		Query query = parser.parse(queryString);
		return query;
	}
	public static Query getBoostedQuery(String queryString)
	{
		return null;
	}
	public static void search(String queryString,String type) throws CorruptIndexException, IOException, ParseException
	{
		Directory indexDir = FSDirectory.open(new File(Constants.indexDirName));
		IndexReader reader = IndexReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = null;
		if(type == "regularSearch")
			query = getRegularSearchQuery(queryString);
		else if(type == "phraseSearch")
			query = getPhraseSearchQuery(queryString);
		else if(type == "wildcardSearch")
			query = getWildcardSearchQuery(queryString);
		else if(type == "booleanSearch")
			query = getBooleanSearchQuery(queryString);
		TopDocs hits = searcher.search(query,1000);
		displayDocs(searcher,hits);		
		searcher.close();
		
	}
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException 
	{	
		search("family or happiness","booleanSearch");
	}
}
