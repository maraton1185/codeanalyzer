package ebook.module.text.views;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationBarHoverManager;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ebook.module.text.annotations.AnnotationConfiguration;
import ebook.module.text.annotations.AnnotationHover;
import ebook.module.text.annotations.AnnotationMarkerAccess;
import ebook.module.text.annotations.AnnotationStyles;
import ebook.module.text.annotations.ColorCache;
import ebook.module.text.annotations.ErrorAnnotation;
import ebook.module.text.annotations.IAnnotation;
import ebook.module.text.annotations.InfoAnnotation;
import ebook.module.text.scanner.DocumentPartitionScanner;

public class ViewerSupport {

	IAnnotationModel fAnnotationModel;
	CompositeRuler fCompositeRuler;
	OverviewRuler fOverviewRuler;
	AnnotationRulerColumn annotationRuler;
	IAnnotationAccess fAnnotationAccess;
	ProjectionViewer viewer;
	ProjectionSupport projectionSupport;
	ColorCache cc;

	IAnnotation[] annotations = new IAnnotation[] { new ErrorAnnotation(),
			new InfoAnnotation() };

	public ViewerSupport() {

		fAnnotationModel = new ProjectionAnnotationModel();

		fAnnotationAccess = new AnnotationMarkerAccess();

		cc = new ColorCache();

		// rulers
		fCompositeRuler = new CompositeRuler();
		fOverviewRuler = new OverviewRuler(fAnnotationAccess, 12, cc);
		annotationRuler = new AnnotationRulerColumn(fAnnotationModel, 16,
				fAnnotationAccess);
		fCompositeRuler.setModel(fAnnotationModel);
		fOverviewRuler.setModel(fAnnotationModel);

		// annotation ruler is decorating our composite ruler
		fCompositeRuler.addDecorator(0, annotationRuler);

		for (IAnnotation an : annotations) {

			annotationRuler.addAnnotationType(an.getType());
			fOverviewRuler.addAnnotationType(an.getType());
			fOverviewRuler.addHeaderAnnotationType(an.getType());
			fOverviewRuler.setAnnotationTypeLayer(an.getType(), an.getLayer());
			fOverviewRuler.setAnnotationTypeColor(an.getType(), new Color(
					Display.getDefault(), an.getColor()));
		}

	}

	public IAnnotationModel getAnnotationModel() {
		return fAnnotationModel;
	}

	public ProjectionViewer getViewer(Composite parent, int style) {
		viewer = new ProjectionViewer(parent, fCompositeRuler, fOverviewRuler,
				true, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | style);

		AnnotationBarHoverManager fAnnotationHoverManager = new AnnotationBarHoverManager(
				fCompositeRuler, viewer, new AnnotationHover(fAnnotationModel),
				new AnnotationConfiguration());
		fAnnotationHoverManager.install(annotationRuler.getControl());

		AnnotationPainter painter = new AnnotationPainter(viewer,
				fAnnotationAccess);

		painter.addDrawingStrategy(AnnotationStyles.STYLE_NONE,
				AnnotationStyles.fgNullStrategy);

		painter.addTextStyleStrategy(AnnotationStyles.STYLE_SQUIGGLES,
				AnnotationStyles.fgSquigglesStrategy);
		painter.addTextStyleStrategy(AnnotationStyles.STYLE_PROBLEM_UNDERLINE,
				AnnotationStyles.fgProblemUnderlineStrategy);
		painter.addTextStyleStrategy(AnnotationStyles.STYLE_BOX,
				AnnotationStyles.fgBoxStrategy);
		painter.addTextStyleStrategy(AnnotationStyles.STYLE_DASHED_BOX,
				AnnotationStyles.fgDashedBoxStrategy);
		painter.addTextStyleStrategy(AnnotationStyles.STYLE_UNDERLINE,
				AnnotationStyles.fgUnderlineStrategy);

		for (IAnnotation an : annotations) {
			painter.addAnnotationType(an.getType(),
					AnnotationStyles.STYLE_PROBLEM_UNDERLINE);
			painter.addAnnotationType(an.getType(),
					AnnotationStyles.STYLE_UNDERLINE);

			painter.setAnnotationTypeColor(an.getType(),
					new Color(Display.getDefault(), an.getColor()));
		}
		viewer.addPainter(painter);
		viewer.addTextPresentationListener(painter);

		return viewer;
	}

	public Document getDocument() {
		Document document = new Document();

		IDocumentPartitioner partitioner = new FastPartitioner(
				new DocumentPartitionScanner(), new String[] {
						DocumentPartitionScanner.STRING,
						DocumentPartitionScanner.COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);

		viewer.setDocument(document, fAnnotationModel);

		// fAnnotationModel = viewer.getVisualAnnotationModel();
		projectionSupport = new ProjectionSupport(viewer, fAnnotationAccess, cc);
		// projectionSupport
		// .addSummarizableAnnotationType(ProjectionAnnotation.TYPE);
		projectionSupport.install();
		viewer.enableProjection();

		return document;
	}

	public void addProjection(Annotation annotation, Position position) {
		viewer.getProjectionAnnotationModel().addAnnotation(annotation,
				position);

	}

	public void addAnnotation(Annotation annotation, Position position) {

		fAnnotationModel.addAnnotation(annotation, position);

	}

	public void removeFolding() {
		viewer.getProjectionAnnotationModel().removeAllAnnotations();

	}
}
