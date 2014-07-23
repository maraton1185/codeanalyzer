package ebook.module.book.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.module.tree.TreeItemInfo;
import ebook.utils.Utils;

public class SectionInfo extends TreeItemInfo {

	public SectionInfo(SectionInfoOptions options) {
		super(options);
	}

	public SectionInfo() {
		super(null);
	}

	@Override
	public boolean isTitleIncrement() {
		return true;
	}

	@Override
	public SectionInfoOptions getOptions() {
		return (SectionInfoOptions) super.getOptions();
	}

	public String tag = "";

	private boolean aclEmplicit = false;

	private Integer book;

	@Override
	public void setACL() {
		aclEmplicit = App.srv.acl().hasExplicit(book, getId());
	}

	@Override
	public Image getListImage() {
		if (aclEmplicit)
			return Utils.getImage("lock.png");
		else
			return null;
	}

	public void setBookId(Integer id) {
		book = id;

	}
}
