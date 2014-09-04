package ebook.module.conf.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.item.ITreeItemXML;

@XmlRootElement(name = "context", namespace = "ebook.module.xml")
public class ContextXML implements ITreeItemXML {

	public static final String filename = "data";

	// @XmlElementWrapper(name = "images")
	// public ArrayList<ImageXML> images;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "context")
	public ArrayList<ContextXML> children = new ArrayList<ContextXML>();

	public int id;
	// public int sort;
	public boolean group;
	public String title;
	public String options;
	public Integer proc;
	public String text = "";
	public boolean root = false;

	@XmlElementWrapper(name = "path")
	@XmlElement(name = "item")
	public List<String> path;

	// public String text = "";

	public ContextXML() {

	}

	public ContextXML(ITreeItemInfo item) {
		this(item, true);
	}

	public ContextXML(ITreeItemInfo item, boolean options) {
		super();
		this.id = item.getId();
		// this.sort = item.getSort();
		this.group = item.isGroup();
		this.title = item.getTitle();
		this.root = item.isRoot();

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		if (!options)
			opt.conf = "";
		this.options = DbOptions.save(opt);

	}

}
