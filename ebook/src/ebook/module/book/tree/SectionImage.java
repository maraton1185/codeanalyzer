package ebook.module.book.tree;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.utils.PreferenceSupplier;

public class SectionImage {

	public Integer book;

	public Integer id;

	public Integer getId() {
		return id;
	}

	public Image image;
	private String title;

	public void setTitle(String title) {
		this.title = title;

	}

	public String getTitle() {
		return title == null || title.isEmpty() ? ebook.utils.Strings
				.value("image") : title;
	}

	// public boolean expanded;
	public int sort;

	public int getSort() {
		return sort;
	}

	public String url;

	private String mime;

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getMime() {
		return mime == null ? "" : mime;
	}

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

	public Image getScaled(Composite comp) {

		Image scaled = image;
		int mWidth = comp.getBounds().width - 100;
		int width = image.getBounds().width;
		int height = image.getBounds().height;
		// if (width > mWidth)
		scaled = new Image(comp.getDisplay(), image.getImageData().scaledTo(
				(mWidth), (int) ((float) height / width * mWidth)));

		return scaled;
	}

	public BufferedImage getAwt() {

		ImageData data = image.getImageData();
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask,
					palette.greenMask, palette.blueMask);
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width,
							data.height), false, null);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					bufferedImage.setRGB(x, y, rgb.red << 16 | rgb.green << 8
							| rgb.blue);
				}
			}
			return bufferedImage;
		} else {
			RGB[] rgbs = palette.getRGBs();
			byte[] red = new byte[rgbs.length];
			byte[] green = new byte[rgbs.length];
			byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				red[i] = (byte) rgb.red;
				green[i] = (byte) rgb.green;
				blue[i] = (byte) rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red,
						green, blue, data.transparentPixel);
			} else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red,
						green, blue);
			}
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width,
							data.height), false, null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SectionImage)
			return ((SectionImage) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	public int getFormat() {
		if (getMime().equalsIgnoreCase("jpg"))
			return SWT.IMAGE_JPEG;
		else
			return SWT.IMAGE_PNG;
	}

	public static String getFilters() {
		return "*.png;*.jpg";
	}
}
