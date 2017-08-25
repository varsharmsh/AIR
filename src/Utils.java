import java.io.File;
import java.util.Scanner;

public class Utils 
{
	public static String readFile(String fileName)
	{
		String content = "";		
		try
		{
			content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return content;
	}
	public static String[] tokenize(String content)
	{
		return content.replaceAll("[^a-zA-Z \n]","").toLowerCase().split("\\s+");
	}
}
