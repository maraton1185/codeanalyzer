package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ebook.utils.Utils;

public class ErrorAnnotation extends Annotation implements IAnnotation {

	static final String type = "error.type";

	public ErrorAnnotation() {
		super(type, true, "");
	}

	public ErrorAnnotation(String text) {
		super(type, true, text);

	}

	@Override
	public RGB getColor() {

		return new RGB(255, 0, 0);
	}

	@Override
	public Integer getLayer() {

		return 30;
	}

	@Override
	public Image getImage() {

		return Utils.getImage("markers/module.png");
	}

	@Override
	public String getTypeLabel() {

		return "Error";
	}

}