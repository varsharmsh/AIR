import java.util.*;
import java.io.File;

class PositionalInvertedIndex
{
    private Map<String, PostingList> index;
    private File[] fileNames;

    PositionalInvertedIndex(File[] fileNames)
    {
        this.fileNames = fileNames;
        index = new HashMap<String, PostingList>();
        buildIndex();
    }
    public String phraseIntersection(String lhs, String rhs)
    {
    	List<Integer> phrase1 = searchPhraseHelper(lhs);
    	List<Integer> phrase2 = searchPhraseHelper(rhs);
    	return getStringResult(performIntersection(phrase1,phrase2));
    }
    private void buildIndex()
    {
		for(File file: fileNames)
		{
			HashMap<String,WikiDocument> docs = Utils.readCSV(file);			
			for(Map.Entry<String,WikiDocument> entry: docs.entrySet())
			{
				int tokenCount = 0;
				String[] tokens = Utils.tokenize(entry.getValue().content.toString());
				for(String t: tokens)
				{
					if(!index.containsKey(t))
						index.put(t, new PostingList());
					index.get(t).addDocument(entry.getValue().docNumber, tokenCount);
					tokenCount++;
				}
			}
		}
		System.out.println(index.get("economist"));
    }

    public String getContainingDocs(String token)
    {
    	try
    	{
    		return getStringResult(getContainingDocsHelper(token));
    	}
    	catch(Exception e)
        {
            return "[ERROR] : " + e;
        }
    	
    }
    public List<Integer> getContainingDocsHelper(String token)
    {
  
            Set<Integer> result = index.get(Utils.tokenizeWord(token)).getDocs();
            return new ArrayList(result);                   
    }

    public String unionOfDocs(String lhs, String rhs)
    {
        lhs = Utils.tokenizeWord(lhs);
        rhs = Utils.tokenizeWord(rhs);
        Set<Integer> docIds = new HashSet<Integer>();
        Set<Integer> p = index.get(lhs).getDocs();
        Set<Integer> q = index.get(rhs).getDocs();
        docIds.addAll(p);
        docIds.addAll(q);
        return getStringResult(new ArrayList<Integer>(docIds));
    }

    public String intersectingDocs(String lhs, String rhs)
    {
        try
        {
            lhs = Utils.tokenizeWord(lhs);
            rhs = Utils.tokenizeWord(rhs);
            List<Integer> p = new ArrayList<Integer>(index.get(lhs).getDocs());
            List<Integer> q = new ArrayList<Integer>(index.get(rhs).getDocs());
            
            List<Integer> docIds = performIntersection(p, q);
            
            return getStringResult(docIds);
        }

        catch(Exception e)
        {
            return "[Error] : " + e;
        }
    }

    public String searchPhrase(String phrase)
    {    
    	try
    	{
    		return getStringResult(searchPhraseHelper(phrase));
    	}
    	catch(Exception e)
    	{
    		 return "[ERROR] : " + e;
    	}
    }
    private boolean checkPhrase(String[] words, int docId)
    {
    	try
    	{
    		//System.out.println("In document " + docId);
    		List<Integer> startPos = index.get(words[0]).docs.get(docId);
    		//System.out.println(startPos);
    		for(int pos:startPos)
    		{
    			int temp = pos;
    			int i;
    			for(i = 1; i< words.length; i++)
    			{
    				if(!index.get(words[i]).search(docId,temp+1))
    				{
    					//System.out.println("false for " + words[i] + temp);
    					break;
    				}
    				temp++;
    			}
    			if(i== words.length)
    				return true;
    		}
    		return false;
    		    				
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    	
    	
    }
    public List<Integer> searchPhraseHelper(String phrase)
    {
    	String[] words = Utils.tokenize(phrase);
    	List<Integer> allDocs = null;
    	List<Integer> phraseDocs = new ArrayList<Integer>();
    	for(int i = 0 ; i< words.length;i++)
    	{
    		if(allDocs==null)
    			allDocs = getContainingDocsHelper(words[i]);
    		else
    			allDocs = performIntersection(allDocs,getContainingDocsHelper(words[i]));
    	} 
    	//System.out.println(allDocs);
    	for(int i:allDocs)
    	{
    		if(checkPhrase(words,i))
    			phraseDocs.add(i);
    			
    	}
    	return phraseDocs;
    }
    public String wordWithinNextKWords(String lhs, int k, String rhs)
    {
        return getStringResult(wordWithinNextKWordsHelper(lhs, k, rhs));
    }

    public List<Integer> wordWithinNextKWordsHelper(String lhs, int k, String rhs)
    {
        try
        {
            lhs = Utils.tokenizeWord(lhs);
            rhs = Utils.tokenizeWord(rhs);
            List<Integer> p = new ArrayList<Integer>(index.get(lhs).getDocs());
            List<Integer> q = new ArrayList<Integer>(index.get(rhs).getDocs());
            Set<Integer> result = new HashSet<Integer>();
            int pIndex = 0;
            int qIndex = 0;

            while(pIndex < p.size() && qIndex < q.size())
            {
                int pDocId = p.get(pIndex);
                int qDocId = q.get(qIndex);
    
                if(pDocId == qDocId)
                {
                    List<Integer> pPos = index.get(lhs).getPositions(pDocId);
                    List<Integer> qPos = index.get(rhs).getPositions(qDocId);
                    List<Integer> intermediate = new ArrayList<Integer>();

                    int pPosIndex = 0;
                    int qPosIndex = 0;

                    while(pPos != null && pPosIndex < pPos.size())
                    {
                        while(qPos != null && qPosIndex < qPos.size())
                        {
                            double dif = Math.abs(pPos.get(pPosIndex) - qPos.get(qPosIndex));
                            if(dif <= k)
                            {
                                intermediate.add(pPosIndex);
                            }
    
                            else if(qPosIndex > pPosIndex)
                                break;
    
                            while(intermediate.size() > 0)
                            {
                                int diff = Math.abs(intermediate.get(0) - pPosIndex);
                                if(diff > k)
                                    intermediate.remove(0);
                                else 
                                    break;
                            }

                            if(intermediate.size() > 0)
                                result.add(pDocId);
    
                            ++qPosIndex;
                        }
                        ++pPosIndex;
                    }
    
                    ++qIndex;
                    ++pIndex;
                }
    
                else if(pDocId < qDocId)
                    ++pIndex;
                
                else
                    ++qIndex;
            }   
    
            return new ArrayList<>(result);
        }

        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }

    private List<Integer> performIntersection(List<Integer> p, List<Integer> q)
    {
        int pIndex = 0;
        int qIndex = 0;
        List<Integer> result = new ArrayList<Integer>();

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

    private String getStringResult(List<Integer> docIds)
    {
        String result = "";
        for (int docId : docIds) 
        {
            result += docId + 1 + ": " + fileNames[docId].getName() + "\n";    
        }
        return result;
    }
}