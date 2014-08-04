package ebook.module.conf.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.core.models.DbOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeItemInfo;
import ebook.utils.Utils;

public class ContextInfo extends TreeItemInfo {

	public ContextInfo(ContextInfoOptions options) {
		super(options);
	}

	public ContextInfo() {
		super(null);
	}

	@Override
	public ContextInfoOptions getOptions() {
		return (ContextInfoOptions) super.getOptions();
	}

	private int section = 0;

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;

	}

	private Integer conf;

	public void setConfId(Integer id) {
		conf = id;

	}

	@Override
	public Image getListImage() {
		IClipboard clip = App.contextClip;
		Integer con = clip.getConnectionId();
		Integer copy = clip.getCopyId();
		Integer cut = clip.getCutId();

		if (!clip.isEmpty() && con != null && copy != null && con.equals(conf)
				&& copy.equals(getId()))
			return Utils.getImage("copy.png");
		else if (!clip.isEmpty() && con != null && cut != null
				&& con.equals(conf) && cut.equals(getId()))
			return Utils.getImage("cut.png");
		// else if (aclEmplicit)
		// return Utils.getImage("lock.png");
		else
			return null;
	}

	public static ITreeItemInfo fromXML(ContextXML element) {

		ContextInfo info = new ContextInfo();
		info.setTitle(element.title);
		info.setGroup(element.group);
		info.setOptions(DbOptions.load(ContextInfoOptions.class,
				element.options));
		// info.setTitleIncrement(false);

		return info;

	}

}
