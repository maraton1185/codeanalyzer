package codeanalyzer.book;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import codeanalyzer.views.books.ISectionBlockComposite;

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

	public Image getScaled(Device display) {

		Image scaled = image;
		int mWidth = ISectionBlockComposite.groupWidth - 30;
		int width = image.getBounds().width;
		int height = image.getBounds().height;
		if (width > mWidth)
			scaled = new Image(display, image.getImageData().scaledTo((mWidth),
					(int) ((float) height / width * mWidth)));

		return scaled;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookSectionImage)
			return ((BookSectionImage) obj).id.equals(id);
		else
			return super.equals(obj);
	}
}
