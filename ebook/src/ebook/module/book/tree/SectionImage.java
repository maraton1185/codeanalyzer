package ebook.module.book.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.utils.PreferenceSupplier;

public class SectionImage {

	public Integer book;

	public Integer id;

	public Integer getId() {
		return id;
	}

	public Image image;
	public String title;

	public String getTitle() {
		return title;
	}

	// public boolean expanded;
	public int sort;

	public int getSort() {
		return sort;
	}

	public String url;

	public String mime;

	public String getUrl() {
		return App.getJetty().bookImage(book, id);
	}

	public SectionImage() {
	}

	public Image getScaled(Device display, SectionInfoOptions options) {

		Image scaled = image;
		int mWidth = PreferenceSupplier.getInt(PreferenceSupplier.IMAGE_WIDTH);// options.scaledImageWidth;
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

	public String getMime() {
		return mime == null ? "" : mime;
	}

	public int getFormat() {
		if (getMime().equalsIgnoreCase("jpeg"))
			return SWT.IMAGE_JPEG;
		else
			return SWT.IMAGE_PNG;
	}

}
