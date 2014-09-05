package ebook.module.text.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
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
import ebook.module.text.annotations.BookmarkAnnotation;
import ebook.module.text.annotations.ErrorAnnotation;
import ebook.module.text.annotations.IAnnotation;
import ebook.module.text.annotations.InfoAnnotation;
import ebook.module.text.annotations.SearchAnnotation;
import ebook.module.text.model.LineInfo;
import ebook.module.text.scanner.DocumentPartitionScanner;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Const;
import ebook.utils.Events.EVENT_TEXT_DATA;

public class ViewerSupport {

	IAnnotationModel fAnnotationModel;
	CompositeRuler fCompositeRuler;
	OverviewRuler fOverviewRuler;
	AnnotationRulerColumn annotationRuler;
	IAnnotationAccess fAnnotationAccess;
	ProjectionViewer fSourceViewer;
	Document fDocument;
	ProjectionSupport projectionSupport;
	IColorManager cc = pico.get(IColorManager.class);

	IAnnotation[] annotations = new IAnnotation[] { new ErrorAnnotation(),
			new InfoAnnotation(), new SearchAnnotation(),
			new BookmarkAnnotation() };

	List<ProjectionAnnotation> projections = new ArrayList<ProjectionAnnotation>();
	List<Annotation> markers = new ArrayList<Annotation>();
	private ArrayList<ITreeItemInfo> model;
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
		fDocument = new Document();

		IDocumentPartitioner partitioner = new FastPartitioner(
				new DocumentPartitionScanner(), new String[] {
						DocumentPartitionScanner.STRING,
						DocumentPartitionScanner.COMMENT });
		partitioner.connect(fDocument);
		fDocument.setDocumentPartitioner(partitioner);

		fSourceViewer.setDocument(fDocument, fAnnotationModel);

		// fAnnotationModel = viewer.getVisualAnnotationModel();
		projectionSupport = new ProjectionSupport(fSourceViewer,
				fAnnotationAccess, cc);
		// projectionSupport
		// .addSummarizableAnnotationType(ProjectionAnnotation.TYPE);
		projectionSupport.install();
		fSourceViewer.enableProjection();

		return fDocument;
	}

	private void addProjection(ProjectionAnnotation annotation, LineInfo info) {
		fSourceViewer.getProjectionAnnotationModel().addAnnotation(annotation,
				info.projection);
		info.annotation = annotation;
		projections.add(annotation);
	}

	public void addAnnotation(Annotation annotation, Position position) {

		fAnnotationModel.addAnnotation(annotation, position);
		markers.add(annotation);

	}

	public void removeMarkers(@SuppressWarnings("rawtypes") Class cls) {
		List<Annotation> _markers = new ArrayList<Annotation>();
		for (Annotation marker : markers) {

			if (cls == null)
				fAnnotationModel.removeAnnotation(marker);
			else if (marker.getClass() == cls) {
				fAnnotationModel.removeAnnotation(marker);
				_markers.add(marker);

			}
		}
		if (cls == null)
			markers.clear();

		for (Annotation annotation : _markers) {
			int i = markers.indexOf(annotation);
			if (i >= 0)
				markers.remove(i);
		}
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

	public LineInfo getSelection(LineInfo lineInfo) {

		if (lineInfo == null || model == null)
			return null;
		for (ITreeItemInfo _info : model) {
			LineInfo info = (LineInfo) _info;
			if (info.getTitle().equalsIgnoreCase(lineInfo.getTitle())) {
				LineInfo result = new LineInfo(lineInfo.getTitle());
				result.offset = info.offset;
				// result.absolute_offset = lineInfo.absolute_offset;
				if (lineInfo.isSearchJump) {
					result.start_offset = lineInfo.start_offset
							- info.start_offset;
					result.isJump = true;
				}
				if (lineInfo.isJump) {
					result.start_offset = lineInfo.start_offset;
					result.isSearchJump = true;
				}
				if (lineInfo.isBookmark) {
					result.start_offset = lineInfo.start_offset;
					result.isBookmark = true;
				}
				if (lineInfo.isHistory) {
					result.start_offset = lineInfo.start_offset;
					result.isHistory = true;
				}
				return result;
			}

		}

		return null;
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
			result.start_offset = (offset + h) - p.offset;

			if (offset + h < p.offset) {
				// setAbsoluteOffset(item, result, offset + h);
				return result;
			}
			if ((offset + h) > p.offset && (offset + h < p.offset + p.length)) {
				// setAbsoluteOffset(item, result, offset + h);
				return result;
			}
		}

		return null;
	}

	// private void setAbsoluteOffset(ProjectionAnnotation item, LineInfo
	// result,
	// int h) {
	// try {
	// if (!item.isCollapsed()) {
	// IRegion reg = fDocument.getLineInformationOfOffset(h);
	// result.absolute_offset = reg.getOffset();
	// }
	// } catch (BadLocationException e) {
	// e.printStackTrace();
	// }
	//
	// }

	public void setSelection(LineInfo info) {

		if (info == null)
			return;

		List<Integer> reveals = addAnnotation(info);
		if (reveals.isEmpty())
			return;

		int revealStart = reveals.get(0);
		int revealLength = reveals.get(1);
		setOffsetSelection(revealStart, revealLength);

	}

	private void setOffsetSelection(int revealStart, int revealLength) {
		StyledText widget = fSourceViewer.getTextWidget();
		widget.setRedraw(false);

		adjustHighlightRange(revealStart, revealLength);
		fSourceViewer.revealRange(revealStart, revealLength);

		fSourceViewer.setSelectedRange(revealStart, revealLength);

		fSourceViewer.getTextWidget().setCaretOffset(revealStart + 1);

		con.setLine(null);

		widget.setRedraw(true);
		// widget.setRedraw(true);

	}

	public void setModel(EVENT_TEXT_DATA data) {
		this.model = data.model;
		this.model_markers = data.markers;
	}

	public void setFolding() {
		if (model == null)
			return;

		removeFolding();
		for (ITreeItemInfo _info : model) {
			LineInfo info = (LineInfo) _info;
			if (info.projection == null)
				continue;

			ProjectionAnnotation annotation = new ProjectionAnnotation(false);
			annotation.setText(info.getTitle() + ":" + info.length.toString());
			addProjection(annotation, info);

		}

	}

	public void setMarkers() {
		removeMarkers(null);

		for (Position p : model_markers) {

			SearchAnnotation info = new SearchAnnotation();
			addAnnotation(info, p);

		}

	}

	public List<Integer> addAnnotation(LineInfo info) {
		List<Integer> result = new ArrayList<Integer>();
		if (info == null) {
			return result;
		}

		try {

			IRegion region = fDocument.getLineInformationOfOffset(info.offset);

			int revealStart = region.getOffset();
			int revealLength = region.getLength();

			if (info.isJump || info.isSearchJump || info.isHistory) {
				revealStart = region.getOffset() + info.start_offset;
				revealLength = 0;
				InfoAnnotation marker = new InfoAnnotation(this);
				addAnnotation(marker, new Position(revealStart));

			} else if (info.isBookmark) {
				revealStart = region.getOffset() + info.start_offset;
				revealLength = 1;
				if (getBookmark(revealStart) == null) {
					BookmarkAnnotation marker = new BookmarkAnnotation();
					addAnnotation(marker, new Position(revealStart));
				}

			}
			result.add(revealStart);
			result.add(revealLength);

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return result;

	}

	public Annotation getBookmark(int offset) {
		for (Annotation marker : markers) {

			if (!(marker instanceof BookmarkAnnotation))
				continue;
			Position p = fAnnotationModel.getPosition(marker);
			if (p == null)
				continue;
			if (p.offset == offset)
				return marker;

		}
		return null;
	}

	public void removeBookmark(Annotation bmk) {
		fAnnotationModel.removeAnnotation(bmk);
		int i = markers.indexOf(bmk);
		if (i >= 0)
			markers.remove(i);

	}

	public void nextMarker(Integer start) {

		try {

			// int offset = viewer.getTextWidget().getCaretOffset();
			int offset;
			int line;
			IRegion reg;
			if (start == null) {
				ITextSelection textSelection = (ITextSelection) fSourceViewer
						.getSelectionProvider().getSelection();
				line = fDocument.getLineOfOffset(textSelection.getOffset());
				reg = fDocument.getLineInformation(line);
				offset = reg.getOffset();
			} else
				offset = start;

			// System.out.println("=====================");
			Annotation[] ann = getSortedMarkers(true);

			Integer nextMarker = null;
			for (Annotation marker : ann) {
				Position p = fAnnotationModel.getPosition(marker);
				if (p == null)
					continue;

				line = fDocument.getLineOfOffset(p.offset);
				reg = fDocument.getLineInformation(line);
				int p_offset = reg.getOffset();
				// System.out.println(p_offset);

				if (offset < p_offset) {
					// System.out.println(" - - " + offset + " : " + p_offset);
					nextMarker = p_offset;

					break;
				}
			}

			if (nextMarker == null) {
				if (!markers.isEmpty())
					nextMarker(Integer.MIN_VALUE);
				return;

			}

			setOffsetSelection(nextMarker, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Annotation[] getSortedMarkers(final boolean ascending) {
		Annotation[] ann = new Annotation[markers.size()];
		markers.toArray(ann);
		Arrays.sort(ann, new Comparator<Annotation>() {
			@Override
			public int compare(Annotation arg0, Annotation arg1) {
				Position p1 = fAnnotationModel.getPosition(arg0);
				if (p1 == null)
					return 0;

				Position p2 = fAnnotationModel.getPosition(arg1);
				if (p2 == null)
					return 0;

				return ascending ? p1.offset - p2.offset : p2.offset
						- p1.offset;
			}
		});
		return ann;
	}

	public void previousMarker(Integer start) {
		try {

			// int offset = viewer.getTextWidget().getCaretOffset();
			int offset;
			int line;
			IRegion reg;
			if (start == null) {
				ITextSelection textSelection = (ITextSelection) fSourceViewer
						.getSelectionProvider().getSelection();
				line = fDocument.getLineOfOffset(textSelection.getOffset());
				reg = fDocument.getLineInformation(line);
				offset = reg.getOffset();
			} else
				offset = start;

			// System.out.println("=====================");
			Annotation[] ann = getSortedMarkers(false);

			Integer nextMarker = null;
			for (Annotation marker : ann) {
				Position p = fAnnotationModel.getPosition(marker);
				if (p == null)
					continue;

				line = fDocument.getLineOfOffset(p.offset);
				reg = fDocument.getLineInformation(line);
				int p_offset = reg.getOffset();
				// System.out.println(p_offset);

				if (offset > p_offset) {
					// System.out.println(" - - " + offset + " : " + p_offset);
					nextMarker = p_offset;

					break;
				}
			}

			if (nextMarker == null) {
				if (!markers.isEmpty())
					previousMarker(Integer.MAX_VALUE);
				return;

			}

			setOffsetSelection(nextMarker, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
