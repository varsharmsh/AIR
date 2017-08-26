import java.util.*;
public class Solution 
{
	final static String[] fileNames = {"Frogs that rode a Snake.txt","The Greedy Cobra and the King of Frogs.txt","The Old Man, his Young Wife and The Thief.txt",
			"The Snake and The Foolish Frogs.txt","The Tale of Two Snakes.txt","The War of Crows and Owls.txt","The Wedding of the Mouse.txt"};
	static Map<String,PostingList> index = new HashMap<String,PostingList>();
	public static void buildIndex()
	{
		int docCount = 0;
		for(String name: fileNames)
		{
			int tokenCount = 0;
			String content = Utils.readFile("tests/"+name);
			System.out.println(name);
			String[] tokens = Utils.tokenize(content);
			for(String t: tokens)
			{
				System.out.println(t);
				if(!index.containsKey(t))
					index.put(t, new PostingList());
				index.get(t).addDocument(docCount, tokenCount);
				tokenCount++;
			}
			docCount++;
		}
		System.out.println(index);
	}
	public static void main(String[] args)
	{
		buildIndex();
		//System.out.println(index.get("old"));
	}
}
