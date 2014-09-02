package ebook.module.text.model;

import ebook.module.conf.tree.ContextInfo;

public class HistoryItem {

	public HistoryItem(ContextInfo item, LineInfo line) {
		super();
		this.item = item;
		this.line = line;
		this.line.isHistory = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HistoryItem) {
			HistoryItem _obj = (HistoryItem) obj;
			return _obj.getItem().equals(item)
					&& _obj.getLine().getTitle()
							.equalsIgnoreCase(line.getTitle());
		} else
			return super.equals(obj);
	}

	ContextInfo item;

	public ContextInfo getItem() {
		return item;
	}

	public LineInfo getLine() {
		return line;
	}

	LineInfo line;
}
