import java.util.*;

public class PostingList 
{
	Map<Integer, List<Integer>> docs;

	public PostingList() 
	{
		docs = new TreeMap<Integer, List<Integer>> ();
	}

	public void addDocument(int docId, int position)
	{
		if(!docs.containsKey(docId))
		{
			docs.put(docId, new ArrayList<Integer>());
		}
		docs.get(docId).add(position);
	}
	public boolean search(int docID, int pos)
	{
		List<Integer> toSearch = docs.get(docID);
		return toSearch.contains(pos);
	}
	@Override
	public String toString() 
	{
		return docs.toString();
	}

	public Set<Integer> getDocs()
	{
		return docs.keySet();
	}	

	public List<Integer> getPositions(int docId)
	{
		if(!docs.containsKey(docId))
			return null;
			
		return docs.get(docId);
	}
}