import java.util.*;
import java.io.*;

public class Solution 
{
	public static void main(String[] args)
	{
		Scanner key = new Scanner(System.in);
		File test = new File("tests");
		File[] corpus = test.listFiles();
		PositionalInvertedIndex index = new PositionalInvertedIndex(corpus);
		String query;
		do
		{
			query = key.nextLine();
			if(query.contains("."))
				System.out.println(index.getContainingDocs(query.replace(".", "")));
			else if(query.contains("AND"))
			{
				String[] temp = query.split("AND");
				if(temp[0].trim().split(" ").length > 1 || temp[1].trim().split(" ").length > 1)
					System.out.println(index.phraseIntersection(temp[0].trim(), temp[1].trim()));
				else
					System.out.println(index.intersectingDocs(temp[0].trim(),temp[1].trim()));
			}
			else if(query.contains("OR"))
			{
				String[] temp = query.split("OR");
			    System.out.println(index.unionOfDocs(temp[0].trim(),temp[1].trim()));
			}
			else if(query.contains("/"))
			{
				String[] temp = query.split(" ");
				System.out.println(temp[0] + Integer.parseInt(temp[1].substring(1)) + temp[2]);
				System.out.println(index.wordWithinNextKWords(temp[0],Integer.parseInt(temp[1].substring(1)), temp[2]));
			}	
			else
				System.out.println(index.searchPhrase(query));
				
		}
		while(!query.equalsIgnoreCase("exit"));
		key.close();
		
	}
}
