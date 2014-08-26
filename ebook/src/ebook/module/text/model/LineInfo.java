package ebook.module.text.model;

import org.eclipse.jface.text.Position;

import ebook.module.tree.TreeItemInfo;

public class LineInfo extends TreeItemInfo {

	public LineInfo(String title) {
		this();
		setTitle(title);

	}

	public LineInfo() {
		super(null);
		setId(0);
		setGroup(false);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LineInfo)
			return ((LineInfo) obj).getTitle().equalsIgnoreCase(getTitle());
		else
			return super.equals(obj);
	}

	public int line;
	public int offset;
	public String name;
	public Boolean export;
	// public String title;
	// public BuildInfo data;
	// public LineInfo parent;
	public Position projection;
	public Integer length;

}