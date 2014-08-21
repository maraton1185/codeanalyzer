package ebook.web.model;

import java.util.List;

import ebook.module.book.tree.SectionImage;

public class Section {

	public Integer bigImageCSS;

	public Integer getBigImageCSS() {
		return bigImageCSS;
	}

	public Integer textCSS;

	public Integer getTextCSS() {
		return textCSS;
	}

	public Integer id;

	public Integer getId() {
		return id;
	}

	public String context;

	public String getContext() {
		return context;
	}

	public String text;

	public String getText() {
		return text;
	}

	public String title;

	public String getTitle() {
		return title;
	}

	public String url;

	public String getUrl() {
		return url;
	}

	public boolean group;

	public boolean isGroup() {
		return group;
	}

	public List<SectionImage> images;

	public List<SectionImage> getImages() {
		return images;
	}

}
