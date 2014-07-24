package ebook.module.book.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IBookClipboard;
import ebook.core.models.DbOptions;
import ebook.module.book.xml.SectionXML;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeItemInfo;
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
		IBookClipboard clip = App.clip;
		Integer cBook = clip.getBookId();
		Integer cCopy = clip.getCopyId();
		Integer cCut = clip.getCutId();

		if (!clip.isEmpty() && cBook != null && cCopy != null
				&& cBook.equals(book) && cCopy.equals(getId()))
			return Utils.getImage("copy.png");
		else if (!clip.isEmpty() && cBook != null && cCut != null
				&& cBook.equals(book) && cCut.equals(getId()))
			return Utils.getImage("cut.png");
		else if (aclEmplicit)
			return Utils.getImage("lock.png");
		else
			return null;
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
