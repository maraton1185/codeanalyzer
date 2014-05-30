package ebook.module.books.tree;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class SectionImage {

	public Integer id;
	public Image image;
	public String title;
	public boolean expanded;
	public int sort;

	public SectionImage(Image image, String title, boolean opened) {
		super();
		this.image = image;
		this.title = title;
		this.expanded = opened;
	}

	public SectionImage() {
	}

	public Image getScaled(Device display, SectionInfoOptions options) {

		Image scaled = image;
		int mWidth = options.scaledImageWidth;
		int width = image.getBounds().width;
		int height = image.getBounds().height;
		if (width > mWidth)
			scaled = new Image(display, image.getImageData().scaledTo((mWidth),
					(int) ((float) height / width * mWidth)));

		return scaled;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SectionImage)
			return ((SectionImage) obj).id.equals(id);
		else
			return super.equals(obj);
	}
}
