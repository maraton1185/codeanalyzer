package ebook.module.book.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ebook.core.models.DbOptions;
import ebook.module.tree.ITreeItemInfo;

@XmlRootElement(name = "section", namespace = "ebook.module.xml")
public class SectionXML {

	public static final String filename = "data";

	@XmlElementWrapper(name = "images")
	public ArrayList<ImageXML> images;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "section")
	public ArrayList<SectionXML> children;

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
