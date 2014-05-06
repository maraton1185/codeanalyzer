package codeanalyzer.book;

import org.eclipse.swt.graphics.Image;

public class BookSectionImage {

	public Integer id;
	public Image image;
	public String title;
	public boolean expanded;
	public int sort;

	public BookSectionImage(Image image, String title, boolean opened) {
		super();
		this.image = image;
		this.title = title;
		this.expanded = opened;
	}

	public BookSectionImage() {
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookSectionImage)
			return ((BookSectionImage) obj).id.equals(id);
		else
			return super.equals(obj);
	}
}
