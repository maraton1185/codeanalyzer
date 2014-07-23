package ebook.module.book.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "ebook.module.xml")
public class SectionXML1 {

	private ArrayList<SectionXML> child;

	private String title;

	// **********************************************

	@XmlElementWrapper(name = "wrapper")
	@XmlElement(name = "section")
	public ArrayList<SectionXML> getChild() {
		return child;
	}

	public void setChild(ArrayList<SectionXML> child) {
		this.child = child;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
