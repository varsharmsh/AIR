import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class Solution
{
    public static void main(String[] args)
    {
        InvertedIndex index = new InvertedIndex("wikipedia-sentences.csv");
        System.out.println("Usage: word1, word2\n word1, word2, k");
        while(true)
        {
            Scanner in = new Scanner(System.in);
            String input = in.next();

            try
            {
                String[] stuff = input.split(",");
                if(stuff.length == 2)
                    System.out.println(index.Intersection(stuff[0], stuff[1]));
                
                else if(stuff.length == 3)
                System.out.println(index.Intersection(stuff[0], stuff[1], Integer.parseInt(stuff[2])));
            }

            catch(Exception e)
            {
                System.out.println("Use ,, or ,,,");
            }
        }
    }
}