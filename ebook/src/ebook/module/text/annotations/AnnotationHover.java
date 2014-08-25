package ebook.module.text.annotations;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;

public class AnnotationHover implements IAnnotationHover, ITextHover {

	private IAnnotationModel fAnnotationModel;

	public AnnotationHover(IAnnotationModel fAnnotationModel2) {
		this.fAnnotationModel = fAnnotationModel2;
	}

	@Override
	public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		Iterator ite = fAnnotationModel.getAnnotationIterator();

		ArrayList all = new ArrayList();

		while (ite.hasNext()) {
			Annotation a = (Annotation) ite.next();
			if (a instanceof ErrorAnnotation) {
				all.add(((ErrorAnnotation) a).getText());
			}
		}

		StringBuffer total = new StringBuffer();

		for (int x = 0; x < all.size(); x++) {
			String str = (String) all.get(x);
			total.append(" " + str + (x == (all.size() - 1) ? "" : "\n"));
		}

		return total.toString();
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return null;
	}
}
