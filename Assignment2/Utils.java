import java.io.*;
import java.util.*;
public class Utils
{	
    public static String tokenizeWord(String word)
	{
		return word.replaceAll("[^a-zA-Z \n\t]","").toLowerCase();
    }
    public static String[] tokenize(String content)
	{
		return tokenizeWord(content).split("\\s+");
	}
}
