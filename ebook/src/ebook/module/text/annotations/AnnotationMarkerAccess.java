package ebook.module.text.annotations;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationAccessExtension;
import org.eclipse.jface.text.source.IAnnotationPresentation;
import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class AnnotationMarkerAccess implements IAnnotationAccess,
		IAnnotationAccessExtension {
	@Override
	public Object getType(Annotation annotation) {
		return annotation.getType();
	}

	@Override
	public boolean isMultiLine(Annotation annotation) {
		return true;
	}

	@Override
	public boolean isTemporary(Annotation annotation) {
		return !annotation.isPersistent();
	}

	@Override
	public String getTypeLabel(Annotation annotation) {
		if (annotation instanceof IAnnotation)
			return ((IAnnotation) annotation).getTypeLabel();

		return null;
	}

	@Override
	public int getLayer(Annotation annotation) {
		if (annotation instanceof IAnnotation)
			return ((IAnnotation) annotation).getLayer();

		return 0;
	}

	@Override
	public void paint(Annotation annotation, GC gc, Canvas canvas,
			Rectangle bounds) {
		if (annotation instanceof IAnnotationPresentation) {
			IAnnotationPresentation presentation = (IAnnotationPresentation) annotation;
			presentation.paint(gc, canvas, bounds);
			return;
		}

		ImageUtilities.drawImage(((IAnnotation) annotation).getImage(), gc,
				canvas, bounds, SWT.CENTER, SWT.TOP);
	}

	@Override
	public boolean isPaintable(Annotation annotation) {
		if (annotation instanceof IAnnotation)
			return ((IAnnotation) annotation).getImage() != null;

		return false;
	}

	@Override
	public boolean isSubtype(Object annotationType, Object potentialSupertype) {
		if (annotationType.equals(potentialSupertype))
			return true;

		return false;

	}

	@Override
	public Object[] getSupertypes(Object annotationType) {
		return new Object[0];
	}
}
