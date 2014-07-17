package ebook.web.model;

import java.util.List;

public class ListServletModel {

	public String title;

	public String getTitle() {
		return title;
	}

	public List<ModelItem> parents;

	public List<ModelItem> getParents() {
		return parents;
	}

}
