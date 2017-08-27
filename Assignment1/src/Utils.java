import java.io.File;
import java.util.Scanner;

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

	public static String tokenizeWord(String word)
	{
		return word.replaceAll("[^a-zA-Z \n]","").toLowerCase();
	}
}
