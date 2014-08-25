package ebook.module.text.annotations;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public interface IAnnotation {

	public RGB getColor();

	public Integer getLayer();

	public String getType();

	public Image getImage();

	public String getTypeLabel();

}
