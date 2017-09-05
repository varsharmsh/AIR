import java.io.File;
import java.io.*;
import java.util.Scanner;
import java.util.*;
public class Utils 
{
	public static String readFile(File file)
	{
		String content = "";		
		try
		{
			Scanner in = new Scanner(file);
			content = in.useDelimiter("\\Z").next();
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return content;
	}
	public static String[] tokenize(String content)
	{
		return tokenizeWord(content).split("\\s+");
	}
	public static HashMap<String,WikiDocument> readCSV(File file)
	{
		HashMap<String,WikiDocument> docs = new HashMap<String,WikiDocument>();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			String line="";
			int docCount = 0;
			 while ((line = br.readLine()) != null) 
			 {
				 String url = line.split("\t")[0];
				 String content = line.split("\t")[1];
				 if(docs.containsKey(url))
					 docs.get(url).content.append(content);
				 else
					 docs.put(url,new WikiDocument(docCount++,url,new StringBuilder(content)));
	         }
			 //System.out.println(docs.get("http://en.wikipedia.org/wiki/Didier_Dubois"));
			 br.close();
		
		}
		catch(Exception e)
		{
				e.printStackTrace();	
		}
		return docs;
	}
	public static String tokenizeWord(String word)
	{
		return word.replaceAll("[^a-zA-Z \n]","").toLowerCase();
	}
}
