package ebook.web.model;

import java.util.List;

public class ListModel {

	public String title;

	public String getTitle() {
		return title;
	}

	public List<ModelItem> parents;

	public List<ModelItem> getParents() {
		return parents;
	}

	public List<Book> books;

	public List<Book> getBooks() {
		return books;
	}

	// public String url;
	//
	// public String getUrl() {
	// return url;
	// }

	// public String aboutUrl;
	//
	// public String getAboutUrl() {
	// return aboutUrl;
	// }

	// public String brand;
	//
	// public String getBrand() {
	// return brand;
	// }
}
