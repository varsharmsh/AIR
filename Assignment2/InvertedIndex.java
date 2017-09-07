import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.*;
import java.nio.charset.Charset;
public class InvertedIndex
{
    HashMap<String, ArrayList<Short>> index;
    public InvertedIndex(String csv)
    {
        index =  new HashMap<String,ArrayList<Short>>();
        buildIndex(csv);
    }
    public  void buildIndex(String csvName)
    {
        File inp = new File(csvName);
        File docIds = new File("docId.txt");
        HashMap<String,StringBuilder> docs = Utils.readCSV(inp);
        Iterator docIterator = docs.entrySet().iterator();
        short docCount = 0;
        while(docIterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)docIterator.next();
            String url = (String)entry.getKey();
            Utils.appendFile(docIds, docCount + ":" + url);
            String content = entry.getValue().toString();
            String tokens[]  = Utils.tokenize(content);
            for(int i = 0; i < tokens.length;i++)
            {
                if(!index.containsKey(tokens[i]))
                    index.put(tokens[i], new ArrayList<Short>());
                ArrayList<Short> temp = index.get(tokens[i]);
                if(temp.size() == 0 || temp.get(temp.size()-1)!=docCount)
                    temp.add(docCount);
            }
            docCount++;
            docIterator.remove();
        }
      //System.out.println(index.get("economist").size()); 
    }
}