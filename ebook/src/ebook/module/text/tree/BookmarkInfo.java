package ebook.module.text.tree;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.db.DbOptions;
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
			item = new ContextInfo(DbOptions.load(ContextInfoOptions.class,
					opt.item_opt));
			item.setId(opt.item_id);
			item.setTitle(opt.item_title);
		}

		return item;
	}

	LineInfo line;

	public LineInfo getLine() {
		if (line == null) {
			// BookmarkInfoOptions opt = getOptions();
			line = new LineInfo(_proc);
			line.start_offset = _offset;
			line.isBookmark = true;
			// line.info = opt.info;
		}

		return line;
	}

	@Override
	public String getSuffix() {
		String tag = getOptions().info;
		return tag == null ? "" : tag;
	}

	public int _id;
	public String _proc;
	public int _offset;

}
