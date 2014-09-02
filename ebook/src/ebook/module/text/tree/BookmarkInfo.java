package ebook.module.text.tree;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.TreeItemInfo;

public class BookmarkInfo extends TreeItemInfo {

	public BookmarkInfo(BookmarkInfoOptions options) {
		super(options);
	}

	@Override
	public BookmarkInfoOptions getOptions() {

		return (BookmarkInfoOptions) super.getOptions();
	}

	public BookmarkInfo() {
		super(null);
	}

	@Override
	public boolean isTitleIncrement() {
		return isGroup();
	}

	ContextInfo item;

	public ContextInfo getItem() {

		if (item == null) {
			BookmarkInfoOptions opt = getOptions();
			item = new ContextInfo();
			item.setId(opt.item);
		}

		return item;
	}

	LineInfo line;

	public LineInfo getLine() {
		if (line == null) {
			BookmarkInfoOptions opt = getOptions();
			line = new LineInfo(opt.title);
			line.start_offset = opt.start_offset;
			line.isBookmark = true;
		}

		return line;
	}

	@Override
	public String getSuffix() {
		String tag = getOptions().info;
		return tag == null ? "" : tag;
	}
}
