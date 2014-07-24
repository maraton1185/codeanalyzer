package ebook.module.book.xml;

import javax.xml.bind.annotation.XmlRootElement;

import ebook.module.book.tree.SectionImage;

@XmlRootElement(name = "image")
public class ImageXML {

	public static final String filename = "image";

	public ImageXML() {

	}

	public ImageXML(SectionImage image) {
		this.id = image.getId();
		// this.sort = image.getSort();
		this.title = image.getTitle();
	}

	public int id;
	// public int sort;
	public String title;

}
