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

    private void buildIndex()
    {
        int docCount = 0;
		for(File file: fileNames)
		{
			int tokenCount = 0;
			String content = Utils.readFile(file);
			// System.out.println(file.getName());
			String[] tokens = Utils.tokenize(content);
			for(String t: tokens)
			{
				if(!index.containsKey(t))
					index.put(t, new PostingList());
				index.get(t).addDocument(docCount, tokenCount);
				tokenCount++;
			}
			docCount++;
		}
    }

    public String getContainingDocs(String token)
    {
        try
        {
            Set<Integer> result = index.get(Utils.tokenizeWord(token)).getDocs();
            return Arrays.toString(result.toArray());
        }

        catch(Exception e)
        {
            return "[ERROR] : " + e;
        }
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
        return null;
    }

    public String seachPhrase(String lhs, String rhs)
    {
        return null;
    }

    public String wordWithinNextKWords(String lhs, int k, String rhs)
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
    
            return getStringResult(new ArrayList<>(result));
        }

        catch(Exception e)
        {
            e.printStackTrace();
            return "[ERROR] : ";
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