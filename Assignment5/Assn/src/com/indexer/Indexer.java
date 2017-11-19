package com.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class Indexer 
{
	public static Document getDocument(File f) throws IOException
	{
		Document doc = new Document();
		List<String> content = Files.readAllLines(Paths.get(f.getAbsolutePath()));
		String docContent = "";
		for(String line : content)
		{
			line = line.trim();
			String field = line.split(":",2)[0].trim();
			String value = line.split(":",2)[1].trim();
			docContent += value+ " ";
			doc.add(new Field(field,value,Field.Store.YES,Field.Index.ANALYZED));			
		}
		doc.add(new Field(Constants.contentField, docContent, Field.Store.YES,Field.Index.ANALYZED));
		return doc;
	}
	public static void createIndex() throws Exception, LockObtainFailedException, IOException,FileNotFoundException
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		Directory indexDirectory = FSDirectory.open(new File(Constants.indexDirName)); 
		IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(Version.LUCENE_36,analyzer));
		File dataDir = new File(Constants.dataDirName);
		File[] files = dataDir.listFiles();
		for(File f : files)
		{
			Document doc = getDocument(f);
			writer.addDocument(doc);
		}
		writer.close();		
	}
	public static void main(String[] args) throws Exception, LockObtainFailedException, IOException
	{
		createIndex();
		System.out.println("Indexing complete");
	}
}
