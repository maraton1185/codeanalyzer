package ebook.module.conf.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.xml.ContextXML;
import ebook.module.db.DbOptions;
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

	private boolean root = false;

	@Override
	public boolean isRoot() {
		return root;
	}

	@Override
	public void setRoot() {
		root = true;
	}

	private Integer conf;

	public void setConfId(Integer id) {
		conf = id;

	}

	private int list = 0;

	public int getList() {
		return list;
	}

	public void setList(int list) {
		this.list = list;

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

		ContextInfoOptions opt = getOptions();
		if (opt.type == null)
			return null;
		switch (opt.type) {
		case text:
			return Utils.getImage("text.png");
		case object:
			return Utils.getImage("object.png");
		case proc:
			return Utils.getImage("proc.png");
		case root:
			return Utils.getImage("root.png");
		default:
			return null;
		}

	}

	@Override
	public String getSuffix() {
		String tag = getOptions().conf;
		return tag == null ? "" : tag;
	}

	public static ITreeItemInfo fromXML(ContextXML element) {

		ContextInfo info = new ContextInfo();
		info.setTitle(element.title);
		info.setGroup(element.group);
		if (element.root)
			info.setRoot();
		info.setOptions(DbOptions.load(ContextInfoOptions.class,
				element.options));
		// info.setTitleIncrement(false);

		return info;

	}

	public static ITreeItemInfo fromBuild(BuildInfo element) {

		ContextInfo info = new ContextInfo();
		info.setTitle(element.title);
		info.setGroup(element.group);
		// if (element.root)
		// info.setRoot();
		// info.setOptions(DbOptions.load(ContextInfoOptions.class,
		// element.options));
		// // info.setTitleIncrement(false);

		return info;

	}

}
