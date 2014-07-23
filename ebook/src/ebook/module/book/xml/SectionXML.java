package ebook.module.book.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "section", namespace = "ebook.module.xml")
public class SectionXML {

	ArrayList<SectionXML> child;
	String title;

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
