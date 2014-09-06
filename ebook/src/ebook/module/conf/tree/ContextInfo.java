package ebook.module.conf.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.xml.ContextXML;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.TreeItemInfo;
import ebook.utils.Const;
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
		ContextInfoOptions _opt = info.getOptions();
		ContextInfoOptions opt = new ContextInfoOptions();
		opt.type = _opt.type;
		opt.start_offset = _opt.start_offset;
		opt.proc = _opt.proc;
		opt.openInComparison = _opt.openInComparison;
		setOptions(opt);
		setList(info.getList());
		setId(info.getId());
		setParent(info.getParent());
		setTitle(info.getTitle());
		setSection(info.getSection());
		canOpen = info.canOpen;
		proc = info.isProc();

	}

	@Override
	public ContextInfoOptions getOptions() {
		return (ContextInfoOptions) super.getOptions();
	}

	public boolean canOpen = true;

	private boolean proc = false;

	public boolean isProc() {
		return proc;
	}

	public void setProc(boolean proc) {
		this.proc = proc;
	}

	Integer module;

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
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
			switch (getTitle()) {
			case Const.COMPARE_ADDED:
				return Utils.getImage("markers/added.png");
			case Const.COMPARE_REMOVED:
				return Utils.getImage("markers/removed.png");
			case Const.COMPARE_CHANGED:
				return Utils.getImage("markers/changed.png");
			case Const.COMPARE_EQUALS:
				return Utils.getImage("markers/equals.png");
			default:
				return Utils.getImage("markers/proposal.png");
			}

		case proc:
			return Utils.getImage("markers/proc.png");
		case comparison:
			return Utils.getImage("markers/comparison.png");
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
		opt.start_offset = element.start_offset;
		opt.proc = element.proc;
		opt.openInComparison = element.openInComparison;
		info.setOptions(opt);

		return info;

	}

}
