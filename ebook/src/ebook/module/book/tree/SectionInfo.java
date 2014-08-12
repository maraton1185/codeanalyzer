package ebook.module.book.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.module.book.xml.SectionXML;
import ebook.module.db.DbOptions;
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
		IClipboard clip = App.bookClip;
		Integer con = clip.getConnectionId();
		Integer copy = clip.getCopyId();
		Integer cut = clip.getCutId();

		if (!clip.isEmpty() && con != null && copy != null && con.equals(book)
				&& copy.equals(getId()))
			return Utils.getImage("copy.png");
		else if (!clip.isEmpty() && con != null && cut != null
				&& con.equals(book) && cut.equals(getId()))
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
