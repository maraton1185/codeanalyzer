package codeanalyzer.core.model;

public class BookInfo {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookInfo)
			return ((BookInfo) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	public String title;
	public boolean isGroup;
	public String path;
	public Integer id;
	public int parent;

}
