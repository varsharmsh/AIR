public class WikiDocument
{
	
	String url;
	StringBuilder content;
	int docNumber;
	public WikiDocument(int docNumber,String url, StringBuilder content)
	{
		this.docNumber = docNumber;
		this.url = url;
		this.content = content;
	}
	@Override
	public String toString() {
		return "WikiDocument [url=" + url + ", content=" + content + ", docNumber=" + docNumber + "]";
	}
	
}