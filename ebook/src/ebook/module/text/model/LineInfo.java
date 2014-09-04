package ebook.module.text.model;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;

import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.tree.item.TreeItemInfo;

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

	public LineInfo(ContextInfoOptions opt) {
		this(opt.proc);
		if (opt.isSearch())
			isSearchJump = true;
		else
			isJump = true;

		start_offset = opt.start_offset == null ? 0 : opt.start_offset;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LineInfo)
			return ((LineInfo) obj).getTitle().equalsIgnoreCase(getTitle());
		else
			return super.equals(obj);
	}

	// смещение с начала модуля
	// public int absolute_offset;
	// смещение процедуры
	public int offset;
	// смещение от процедуры
	public int start_offset;

	public String name;
	public Boolean export;

	public Position projection;
	public Integer length;
	public ProjectionAnnotation annotation;

	public boolean isHistory = false;
	public boolean isSearchJump = false;
	public boolean isJump = false;
	public boolean isBookmark = false;
	// public String info = "";
}