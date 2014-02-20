package codesnippetapp.data;

public class SnippetData {
	public String name;
	public String code = "";
	public String description = "";
	
	public SnippetData()
	{
	}
	
	public SnippetData (String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
}
