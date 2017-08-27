import java.util.*;
import java.io.*;

public class Solution 
{
	public static void main(String[] args)
	{
		File test = new File("tests");
		File[] corpus = test.listFiles();
		PositionalInvertedIndex index = new PositionalInvertedIndex(corpus);
		System.out.println(index.wordWithinNextKWords("Why", 2, "you"));
	}
}
