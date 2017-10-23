import java.awt.List;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.nio.file.*;
import java.nio.charset.Charset;

public class InvertedIndex
{
    private HashMap<String, LinkedList<Integer>> index;
    private String fileName;

    public InvertedIndex(String csv)
    {
        fileName = csv;
        index =  new HashMap<String, LinkedList<Integer>>();
        buildIndex(csv);
    }

    public  void buildIndex(String csvName)
    {
        File inp = new File(csvName);
        int i = 0;

        try
		{
			BufferedReader br = new BufferedReader(new FileReader(inp));
			String line = "";
			
			while ((line = br.readLine()) != null) 
			{   
                String tokens[]  = Utils.tokenize(line);
                String url = tokens[0];
                Integer docId = new Integer(i);

				for(int j = 0; j < tokens.length; ++j)
				{
                    LinkedList<Integer> temp;
					if(!index.containsKey(tokens[j]))
					{
                        temp = new LinkedList<Integer>();
                        temp.add(docId);
                        index.put(tokens[j], temp);
                        continue;
                    }	
                    
                    temp = index.get(tokens[j]);
					if(temp.size() > 0 && temp.get(temp.size() - 1) != docId.intValue())
					{   
                        temp.add(docId);
                    }	
                }
                ++i;
            }
			br.close();	
        }
        
		catch(Exception e)
		{
            e.printStackTrace();	
		}
    }

    private String getUrl(int num)
    {
        try
		{
            File inp = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(inp));
            String line = "";
            
			for(int i = 0; i <= num; ++i) 
			{
                line = br.readLine();
            }
            String tokens[]  = Utils.tokenize(line);
            return tokens[0];
        }

        catch(Exception e)
        {
            e.printStackTrace();
            return "[ERROR]";
        }
    }

    public String Intersection(String lhs, String rhs)
    {
        try
        {
            lhs = Utils.tokenizeWord(lhs);
            rhs = Utils.tokenizeWord(rhs);
            
            LinkedList<Integer> docIds = performIntersection(index.get(lhs), index.get(rhs));
            
            return docIds.toString();
        }

        catch(Exception e)
        {
            return "[Error] : " + e;
        }
    }

    public String Intersection(String lhs, String rhs, int k)
    {
        try
        {
            lhs = Utils.tokenizeWord(lhs);
            rhs = Utils.tokenizeWord(rhs);
            
            LinkedList<Integer> docIds = performIntersection(index.get(lhs), index.get(rhs));

            k = k < docIds.size() ? k : docIds.size();

            String result = "";
            for(int i = 0; i < k; ++i)
            {
                result += (docIds.get(i) + " " + getUrl(docIds.get(i)) + "\n"); 
            }
            return result;
        }

        catch(Exception e)
        {
            return "[Error] : " + e;
        }
    }

    private LinkedList<Integer> performIntersection(LinkedList<Integer> p, LinkedList<Integer> q)
    {
        int pIndex = 0;
        int qIndex = 0;
        LinkedList<Integer> result = new LinkedList<Integer>();

        while(pIndex < p.size() && qIndex < q.size())
        {
            int pVal = p.get(pIndex);
            int qVal = q.get(qIndex);

            if(pVal == qVal)
            {
                result.add(p.get(pIndex));
                ++pIndex;
                ++qIndex;
            }

            else if(pVal < qVal)
                ++pIndex;
            else
                ++qIndex;
        }
        return result;
    }
}