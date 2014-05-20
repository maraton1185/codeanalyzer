package codeanalyzer.books.book;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import codeanalyzer.core.components.ITreeItemInfo;

public class BookInfo implements ITreeItemInfo {

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

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;

	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isGroup() {
		return isGroup;
	}

	@Override
	public String getSuffix() {
		return path;
	}

	@Override
	public Integer getParent() {
		return parent;
	}

	public IPath getPath() {

		return path.isEmpty() ? null : new Path(path);
	}
}
