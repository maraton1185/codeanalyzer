package ebook.module.book.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ebook.core.models.DbOptions;
import ebook.module.conf.xml.ContextXML;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeItemXML;

@XmlRootElement(name = "section", namespace = "ebook.module.xml")
public class SectionXML implements ITreeItemXML {

	@XmlElementWrapper(name = "images")
	public ArrayList<ImageXML> images;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "section")
	public ArrayList<SectionXML> children;

	// @XmlElementWrapper(name = "contexts")
	@XmlElement(name = "context")
	public ContextXML context;

	public int id;
	// public int sort;
	public boolean group;
	public String title;
	public String options;
	public String text = "";

	public SectionXML() {

	}

	public SectionXML(ITreeItemInfo item) {
		super();
		this.id = item.getId();
		// this.sort = item.getSort();
		this.group = item.isGroup();
		this.title = item.getTitle();
		this.options = DbOptions.save(item.getOptions());

	}

}
