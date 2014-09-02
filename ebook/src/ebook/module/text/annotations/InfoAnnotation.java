package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ebook.module.text.views.ViewerSupport;
import ebook.utils.Utils;

public class InfoAnnotation extends Annotation implements IAnnotation {

	static final String type = "info.type";

	public InfoAnnotation(ViewerSupport viewerSupport) {
		super(type, true, "");
		viewerSupport.removeMarkers(this);

	}

	public InfoAnnotation(String text) {
		super(type, true, text);

	}

	public InfoAnnotation() {
		super(type, true, "");

	}

	@Override
	public RGB getColor() {

		return new RGB(0, 0, 255);
	}

	@Override
	public Integer getLayer() {

		return 20;
	}

	@Override
	public Image getImage() {

		return Utils.getImage("markers/object.png");
	}

	@Override
	public String getTypeLabel() {
		return "Информация";
	}
}