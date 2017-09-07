import java.io.*;
import java.util.*;
public class Utils
{
    public static HashMap<String,StringBuilder> readCSV(File file)
	{
		HashMap<String,StringBuilder> docs = new HashMap<String,StringBuilder>();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			String line="";
			while ((line = br.readLine()) != null) 
			{
				//some lines don't contain \t
				 if(!line.contains("\t"))
				 	line.replaceFirst(" ", "\t");
		     	 String url = line.split("\t")[0];
				 String content = line.split("\t")[1];
				 count++;
				 if(docs.containsKey(url))
					 docs.get(url).append(" " + content);
				 else
					 docs.put(url,new StringBuilder(content));
            }
			 br.close();	
		}
		catch(Exception e)
		{
				e.printStackTrace();	
		}
		return docs;
    }
    public static void appendFile(File file, String content)
    {
        try
        {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(content);
            out.close();
        } 
        catch (IOException e) {
           e.printStackTrace();
        }
    }
    public static String tokenizeWord(String word)
	{
		return word.replaceAll("[^a-zA-Z \n]","").toLowerCase();
    }
    public static String[] tokenize(String content)
	{
		return tokenizeWord(content).split("\\s+");
	}
}
