package ebook.module.book.servlets;

import java.util.List;

import ebook.module.book.tree.SectionImage;

public class BookServletModel {

	public class Section {

		// public Section(Integer id, String text, String title, boolean group,
		// List<SectionImage> images) {
		// super();
		// this.id = id;
		// this.text = text;
		// this.title = title;
		// this.group = group;
		// this.images = images;
		// }

		public Integer id;
		public String text;
		public String title;
		public boolean group;
		public List<SectionImage> images;

		public Integer getId() {
			return id;
		}

		public String getText() {
			return text;
		}

		public String getTitle() {
			return title;
		}

		public boolean isGroup() {
			return group;
		}

		public List<SectionImage> getImages() {
			return images;
		}

	}

	// public BookServletModel(Section section, List<Section> sections) {
	// super();
	// this.section = section;
	// this.sections = sections;
	// }
	public boolean swtMode = false;

	public boolean isSwtMode() {
		return swtMode;
	}

	// int book;
	public Section section;

	public List<Section> sections;

	public Section getSection() {
		return section;
	}

	public List<Section> getSections() {
		return sections;
	}

}
