package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ebook.utils.Utils;

public class SearchAnnotation extends Annotation implements IAnnotation {

	static final String type = "search.type";

	public SearchAnnotation() {
		super(type, true, "");
	}

	public SearchAnnotation(String text) {
		super(type, true, text);

	}

	@Override
	public RGB getColor() {

		return new RGB(158, 158, 158);
	}

	@Override
	public Integer getLayer() {

		return 10;
	}

	@Override
	public Image getImage() {

		return Utils.getImage("search_result.png");
	}

	@Override
	public String getTypeLabel() {
		return "Поиск текста";
	}
}