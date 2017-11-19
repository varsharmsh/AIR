package com.indexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.store.LockObtainFailedException;

public class Driver 
{
	public static void main(String[] args) throws LockObtainFailedException, FileNotFoundException, IOException, Exception
	{
		int choice = 0;
		String query = "";
		String queryType = "";
		Indexer.createIndex();
		Scanner key = new Scanner(System.in);		
		do
		{
			System.out.println("MENU");
			System.out.println("1. Regular Search");
			System.out.println("2. Phrase Search");
			System.out.println("3. Wildcard Search");
			System.out.println("4. Boolean Search");
			System.out.println("5. Boosted Search");
			System.out.println("6. Fuzzy Search");
			System.out.println("Enter choice");
			choice = key.nextInt();
			System.out.println("Enter the query");
			query = key.next();
			switch(choice)
			{
			case 1: 
				queryType = "regularSearch";
				break;
			case 2: 
				queryType = "phraseSearch";
				break;
			case 3: 
				queryType = "wildcardSearch";
				break;
			case 4: 
				queryType = "booleanSearch";
				break;
			case 5: 
				queryType = "boostedSearch";
				break;
			case 6: 
				queryType = "fuzzySearch";
				break;		
			
			}
		Searcher.search(query, queryType);
			
		}while(choice>0 && choice <=6);
		key.close();
	}
}
