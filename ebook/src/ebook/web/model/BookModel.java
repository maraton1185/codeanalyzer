package ebook.web.model;

import java.util.List;

public class BookModel {

	public Section section;

	public Section getSection() {
		return section;
	}

	public List<Section> sections;

	public List<Section> getSections() {
		return sections;
	}

	public String title;

	public String getTitle() {
		return title;
	}

	public List<ModelItem> parents;

	public List<ModelItem> getParents() {
		return parents;
	}

	public String url;

	public String getUrl() {
		return url;
	}

}
