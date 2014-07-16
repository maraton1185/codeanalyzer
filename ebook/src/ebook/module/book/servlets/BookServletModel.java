package ebook.module.book.servlets;

import java.util.List;

import ebook.module.book.tree.SectionImage;

public class BookServletModel {

	public String host;

	public String getHost() {
		return host;
	}

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

	public class Section {

		public Integer bigImageCSS;
		public Integer textCSS;

		public Integer getTextCSS() {
			return textCSS;
		}

		public Integer getBigImageCSS() {
			return bigImageCSS;
		}

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
}
