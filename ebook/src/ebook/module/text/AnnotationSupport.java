package ebook.module.text;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationBarHoverManager;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationAccessExtension;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ebook.utils.Utils;

public class AnnotationSupport {

	public static String ERROR_TYPE = "error.type";
	public static Image ERROR_IMAGE = Utils.getImage("markers/module.png");
	public static final RGB ERROR_RGB = new RGB(255, 0, 0);

	private AnnotationModel fAnnotationModel = new AnnotationModel();

	CompositeRuler fCompositeRuler;
	OverviewRuler fOverviewRuler;
	AnnotationRulerColumn annotationRuler;
	IAnnotationAccess fAnnotationAccess;

	public AnnotationSupport() {

		fAnnotationAccess = new AnnotationMarkerAccess();

		ColorCache cc = new ColorCache();

		// rulers
		fCompositeRuler = new CompositeRuler();
		fOverviewRuler = new OverviewRuler(fAnnotationAccess, 12, cc);
		annotationRuler = new AnnotationRulerColumn(fAnnotationModel, 16,
				fAnnotationAccess);
		fCompositeRuler.setModel(fAnnotationModel);
		fOverviewRuler.setModel(fAnnotationModel);

		// annotation ruler is decorating our composite ruler
		fCompositeRuler.addDecorator(0, annotationRuler);

		// add what types are show on the different rulers
		annotationRuler.addAnnotationType(ERROR_TYPE);
		fOverviewRuler.addAnnotationType(ERROR_TYPE);
		fOverviewRuler.addHeaderAnnotationType(ERROR_TYPE);
		// set what layer this type is on
		fOverviewRuler.setAnnotationTypeLayer(ERROR_TYPE, 3);
		// set what color is used on the overview ruler for the type
		fOverviewRuler.setAnnotationTypeColor(ERROR_TYPE,
				new Color(Display.getDefault(), ERROR_RGB));

	}

	public void init(ProjectionViewer sv) {
		AnnotationBarHoverManager fAnnotationHoverManager = new AnnotationBarHoverManager(
				fCompositeRuler, sv, new AnnotationHover(),
				new AnnotationConfiguration());
		fAnnotationHoverManager.install(annotationRuler.getControl());

		AnnotationPainter ap = new AnnotationPainter(sv, fAnnotationAccess);
		ap.addAnnotationType(ERROR_TYPE);
		ap.setAnnotationTypeColor(ERROR_TYPE, new Color(Display.getDefault(),
				ERROR_RGB));

		sv.addPainter(ap);

		// add an annotation
		ErrorAnnotation errorAnnotation = new ErrorAnnotation(1,
				"Learn how to spell \"text!\"");

		// lets underline the word "texst"
		fAnnotationModel.addAnnotation(errorAnnotation, new Position(12, 5));
	}

	class AnnotationConfiguration implements IInformationControlCreator {
		@Override
		public IInformationControl createInformationControl(Shell shell) {
			return new DefaultInformationControl(shell);
		}
	}

	class ColorCache implements ISharedTextColors {
		@Override
		public Color getColor(RGB rgb) {
			return new Color(Display.getDefault(), rgb);
		}

		@Override
		public void dispose() {
		}
	}

	class AnnotationMarkerAccess implements IAnnotationAccess,
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
			if (annotation instanceof ErrorAnnotation)
				return "Errors";

			return null;
		}

		@Override
		public int getLayer(Annotation annotation) {
			if (annotation instanceof ErrorAnnotation)
				return ((ErrorAnnotation) annotation).getLayer();

			return 0;
		}

		@Override
		public void paint(Annotation annotation, GC gc, Canvas canvas,
				Rectangle bounds) {
			ImageUtilities.drawImage(((ErrorAnnotation) annotation).getImage(),
					gc, canvas, bounds, SWT.CENTER, SWT.TOP);
		}

		@Override
		public boolean isPaintable(Annotation annotation) {
			if (annotation instanceof ErrorAnnotation)
				return ((ErrorAnnotation) annotation).getImage() != null;

			return false;
		}

		@Override
		public boolean isSubtype(Object annotationType,
				Object potentialSupertype) {
			if (annotationType.equals(potentialSupertype))
				return true;

			return false;

		}

		@Override
		public Object[] getSupertypes(Object annotationType) {
			return new Object[0];
		}
	}

	class ErrorAnnotation extends Annotation {
		private IMarker marker;
		private String text;
		private int line;
		private Position position;

		public ErrorAnnotation(IMarker marker) {
			this.marker = marker;
		}

		public ErrorAnnotation(int line, String text) {
			super(ERROR_TYPE, true, null);
			this.marker = null;
			this.line = line;
			this.text = text;
		}

		public IMarker getMarker() {
			return marker;
		}

		public int getLine() {
			return line;
		}

		@Override
		public String getText() {
			return text;
		}

		public Image getImage() {
			return ERROR_IMAGE;
		}

		public int getLayer() {
			return 3;
		}

		@Override
		public String getType() {
			return ERROR_TYPE;
		}

		public Position getPosition() {
			return position;
		}

		public void setPosition(Position position) {
			this.position = position;
		}
	}

	class AnnotationHover implements IAnnotationHover, ITextHover {
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

			for (int x = 0; x < all.size() - 1; x++) {
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

	public IVerticalRuler getCompositeRuler() {
		return fCompositeRuler;
	}

	public IOverviewRuler getOverviewRuler() {
		return fOverviewRuler;
	}
}
