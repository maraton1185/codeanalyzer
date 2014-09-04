package ebook.module.book.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.module.book.xml.SectionXML;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.TreeItemInfo;
import ebook.utils.Utils;

public class SectionInfo extends TreeItemInfo {

	private boolean titleIncrement = true;

	public void setTitleIncrement(boolean titleIncrement) {
		this.titleIncrement = titleIncrement;
	}

	public SectionInfo(SectionInfoOptions options) {
		super(options);
	}

	public SectionInfo() {
		super(null);
	}

	@Override
	public boolean isTitleIncrement() {
		return titleIncrement;
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
		IClipboard clip = App.bookClip;

		if (clip.isCopy(book, getId()))
			return Utils.getImage("copy.png");
		else if (clip.isCut(book, getId()))
			return Utils.getImage("cut.png");
		else if (aclEmplicit)
			return Utils.getImage("lock.png");
		else
			return null;
	}

	@Override
	public String getSuffix() {
		return getOptions().getContextName();
	}

	public void setBookId(Integer id) {
		book = id;

	}

	public static ITreeItemInfo fromXML(SectionXML element) {

		SectionInfo info = new SectionInfo();
		info.setTitle(element.title);
		info.setGroup(element.group);
		info.setOptions(DbOptions.load(SectionInfoOptions.class,
				element.options));
		info.setTitleIncrement(false);

		return info;

	}
}
