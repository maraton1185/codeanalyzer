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

	public ContextInfo(ContextInfo info) {
		super(null);
		ContextInfoOptions opt = new ContextInfoOptions();
		opt.type = info.getOptions().type;
		setOptions(opt);
		setList(info.getList());
		setId(info.getId());
		setParent(info.getParent());
		setTitle(info.getTitle());
		setSection(info.getSection());

	}

	@Override
	public ContextInfoOptions getOptions() {
		return (ContextInfoOptions) super.getOptions();
	}

	private boolean proc = false;

	public boolean isProc() {
		return proc;
	}

	public void setProc() {
		this.proc = true;
	}

	// private List<String> path;
	//
	// public List<String> getPath() {
	// return path;
	// }
	//
	// public boolean hasPath() {
	// return path != null && !path.isEmpty();
	// }
	//
	// public void setPath(List<String> path) {
	// this.path = path;
	// }

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
		return root || getParent() == 0;
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

		if (clip.isCopy(conf, getId()))
			return Utils.getImage("copy.png");
		else if (clip.isCut(conf, getId()))
			return Utils.getImage("cut.png");

		ContextInfoOptions opt = getOptions();
		// if (opt.hasText != null && opt.hasText)
		// return Utils.getImage("markers/text.png");

		if (opt.type == null)
			return null;
		switch (opt.type) {
		case text:
			return Utils.getImage("markers/text.png");
		case object:
			return Utils.getImage("markers/object.png");
		case module:
			return Utils.getImage("markers/module.png");
		case root:
			return Utils.getImage("markers/root.png");
		case proposal:
			return Utils.getImage("markers/proposal.png");
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
		ContextInfoOptions opt = new ContextInfoOptions();
		opt.type = element.type;
		info.setOptions(opt);

		return info;

	}

}
