package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ebook.utils.Utils;

public class BookmarkAnnotation extends Annotation implements IAnnotation {

	static final String type = "bookmark.type";

	public BookmarkAnnotation(String text) {
		super(type, true, text);

	}

	public BookmarkAnnotation() {
		super(type, true, "");

	}

	@Override
	public RGB getColor() {

		return new RGB(255, 171, 0);
	}

	@Override
	public Integer getLayer() {

		return 20;
	}

	@Override
	public Image getImage() {

		return Utils.getImage("bookmark.png");
	}

	@Override
	public String getTypeLabel() {
		return "Закладки";
	}
}