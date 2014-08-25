package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ebook.utils.Utils;

public class InfoAnnotation extends Annotation implements IAnnotation {

	static final String type = "info.type";

	public InfoAnnotation() {
		super(type, true, "");
	}

	public InfoAnnotation(String text) {
		super(type, true, text);

	}

	@Override
	public RGB getColor() {

		return new RGB(0, 0, 255);
	}

	@Override
	public Integer getLayer() {

		return 2;
	}

	@Override
	public Image getImage() {

		return Utils.getImage("markers/object.png");
	}

	@Override
	public String getTypeLabel() {
		return "Info";
	}
}