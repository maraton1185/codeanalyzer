package ebook.module.text.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationBarHoverManager;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ebook.core.pico;
import ebook.core.interfaces.IColorManager;
import ebook.module.text.TextConnection;
import ebook.module.text.annotations.AnnotationConfiguration;
import ebook.module.text.annotations.AnnotationHover;
import ebook.module.text.annotations.AnnotationMarkerAccess;
import ebook.module.text.annotations.AnnotationStyles;
import ebook.module.text.annotations.ErrorAnnotation;
import ebook.module.text.annotations.IAnnotation;
import ebook.module.text.annotations.InfoAnnotation;
import ebook.module.text.annotations.SearchAnnotation;
import ebook.module.text.model.LineInfo;
import ebook.module.text.scanner.DocumentPartitionScanner;
import ebook.utils.Const;
import ebook.utils.Events.EVENT_TEXT_DATA;

public class ViewerSupport {

	IAnnotationModel fAnnotationModel;
	CompositeRuler fCompositeRuler;
	OverviewRuler fOverviewRuler;
	AnnotationRulerColumn annotationRuler;
	IAnnotationAccess fAnnotationAccess;
	ProjectionViewer fSourceViewer;
	Document document;
	ProjectionSupport projectionSupport;
	IColorManager cc = pico.get(IColorManager.class);

	IAnnotation[] annotations = new IAnnotation[] { new ErrorAnnotation(),
			new InfoAnnotation(), new SearchAnnotation() };

	List<ProjectionAnnotation> projections = new ArrayList<ProjectionAnnotation>();
	List<Annotation> markers = new ArrayList<Annotation>();
	private ArrayList<LineInfo> model;
	private ArrayList<Position> model_markers;
	TextConnection con;

	public ViewerSupport(TextConnection con) {

		this.con = con;

		fAnnotationModel = new ProjectionAnnotationModel();

		fAnnotationAccess = new AnnotationMarkerAccess();

		// cc = new ColorCache();

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
		fSourceViewer = new ProjectionViewer(parent, fCompositeRuler,
				fOverviewRuler, true, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
						| style);

		AnnotationBarHoverManager fAnnotationHoverManager = new AnnotationBarHoverManager(
				fCompositeRuler, fSourceViewer, new AnnotationHover(
						fAnnotationModel), new AnnotationConfiguration());
		fAnnotationHoverManager.install(annotationRuler.getControl());

		AnnotationPainter painter = new AnnotationPainter(fSourceViewer,
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
		fSourceViewer.addPainter(painter);
		fSourceViewer.addTextPresentationListener(painter);

		return fSourceViewer;
	}

	public Document getDocument() {
		document = new Document();

		IDocumentPartitioner partitioner = new FastPartitioner(
				new DocumentPartitionScanner(), new String[] {
						DocumentPartitionScanner.STRING,
						DocumentPartitionScanner.COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);

		fSourceViewer.setDocument(document, fAnnotationModel);

		// fAnnotationModel = viewer.getVisualAnnotationModel();
		projectionSupport = new ProjectionSupport(fSourceViewer,
				fAnnotationAccess, cc);
		// projectionSupport
		// .addSummarizableAnnotationType(ProjectionAnnotation.TYPE);
		projectionSupport.install();
		fSourceViewer.enableProjection();

		return document;
	}

	private void addProjection(ProjectionAnnotation annotation,
			Position position) {
		fSourceViewer.getProjectionAnnotationModel().addAnnotation(annotation,
				position);
		projections.add(annotation);
	}

	private void addAnnotation(Annotation annotation, Position position) {

		fAnnotationModel.addAnnotation(annotation, position);
		markers.add(annotation);

	}

	public void removeMarkers() {
		for (Annotation marker : markers) {
			fAnnotationModel.removeAnnotation(marker);
		}
		markers.clear();
	}

	private void removeFolding() {
		fSourceViewer.getProjectionAnnotationModel().removeAllAnnotations();
		projections.clear();
	}

	protected void adjustHighlightRange(int offset, int length) {
		if (fSourceViewer == null)
			return;

		if (fSourceViewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension = fSourceViewer;
			extension.exposeModelRange(new Region(offset, length));
		} else if (!isVisible(fSourceViewer, offset, length)) {
			fSourceViewer.resetVisibleRegion();
		}
	}

	protected static final boolean isVisible(ISourceViewer viewer, int offset,
			int length) {
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
			IRegion overlap = extension.modelRange2WidgetRange(new Region(
					offset, length));
			return overlap != null;
		}
		return viewer.overlapsWithVisibleRegion(offset, length);
	}

	public LineInfo getCurrentProjectionName(int offset) {
		int h = 0;

		// System.out.println(" - " + offset);
		if (projections.isEmpty())
			return new LineInfo(Const.STRING_VARS_TITLE);

		Position p = fSourceViewer.getProjectionAnnotationModel().getPosition(
				projections.get(0));
		if (p == null)
			return null;
		if (offset < p.offset)
			return new LineInfo(Const.STRING_VARS_TITLE);

		for (ProjectionAnnotation item : projections) {
			p = fSourceViewer.getProjectionAnnotationModel().getPosition(item);

			if (p == null)
				continue;

			String[] data = item.getText().split(":");
			int l = Integer.parseInt(data[1]);

			LineInfo result = new LineInfo(data[0]);
			result.annotation = item;

			if (item.isCollapsed())
				h += (p.length - l - 1);

			// System.out.println(p.offset + ":" + p.length + ":" + h + ":"
			// + data[0]);

			if (offset + h < p.offset) {
				// System.out.println(data[0]);
				return result;
			}
			if ((offset + h) > p.offset && (offset + h < p.offset + p.length)) {
				// System.out.println(data[0]);
				return result;
			}
		}

		return null;
	}

	public void setSelection(LineInfo info) {

		if (info == null)
			return;
		StyledText widget = fSourceViewer.getTextWidget();
		widget.setRedraw(false);
		{
			try {
				IRegion region = document
						.getLineInformationOfOffset(info.offset);

				int revealStart = region.getOffset();
				int revealLength = region.getLength();

				if (info.isJump) {
					revealStart = region.getOffset() + info.start_offset;
					revealLength = 0;
					InfoAnnotation marker = new InfoAnnotation();
					addAnnotation(marker, new Position(revealStart));
				}
				// selection = new TextSelection(document, region.getOffset(),
				// region.getLength());

				adjustHighlightRange(revealStart, revealLength);
				fSourceViewer.revealRange(revealStart, revealLength);

				fSourceViewer.setSelectedRange(revealStart, revealLength);

				con.setLine(null);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		widget.setRedraw(true);

	}

	public LineInfo getSelection(LineInfo lineInfo) {

		if (lineInfo == null || model == null)
			return null;
		for (LineInfo info : model) {

			if (info.getTitle().equalsIgnoreCase(lineInfo.getTitle())) {
				LineInfo result = new LineInfo(lineInfo.getTitle());
				result.offset = info.offset;
				if (lineInfo.isJump) {
					result.start_offset = lineInfo.start_offset
							- info.start_offset;
					result.isJump = true;
				}
				return result;
			}

		}
		// for (ProjectionAnnotation item : projections) {
		// Position p = fSourceViewer.getProjectionAnnotationModel()
		// .getPosition(item);
		//
		// if (p == null)
		// continue;
		//
		// String[] data = item.getText().split(":");
		// if (data[0].equalsIgnoreCase(lineInfo.getTitle())) {
		// LineInfo result = new LineInfo(lineInfo.getTitle());
		// result.offset = p.offset;
		// result.start_offset = lineInfo.start_offset;
		// return result;
		// }
		//
		// }

		return null;
	}

	public void setModel(EVENT_TEXT_DATA data) {
		this.model = data.model;
		this.model_markers = data.markers;
	}

	public void setFolding() {
		if (model == null)
			return;

		removeFolding();
		for (LineInfo info : model) {

			if (info.projection == null)
				continue;

			ProjectionAnnotation annotation = new ProjectionAnnotation(false);
			annotation.setText(info.getTitle() + ":" + info.length.toString());
			addProjection(annotation, info.projection);

		}

	}

	public void setMarkers() {
		removeMarkers();

		for (Position p : model_markers) {

			SearchAnnotation info = new SearchAnnotation();
			addAnnotation(info, p);

		}

	}
}
