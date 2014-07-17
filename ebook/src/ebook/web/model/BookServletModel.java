package ebook.web.model;

import java.util.List;

public class BookServletModel {

	public boolean swtMode = false;

	public boolean isSwtMode() {
		return swtMode;
	}

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

}
