import org.eclipse.swt.widgets.Shell; 
import org.eclipse.swt.widgets.Display; 
import org.eclipse.swt.widgets.Composite; 
import org.eclipse.swt.widgets.Canvas; 
import org.eclipse.swt.SWT; 
import org.eclipse.swt.graphics.*; 
import org.eclipse.swt.layout.FillLayout; 
import org.eclipse.jface.text.source.*; 
import org.eclipse.jface.text.*; 
import org.eclipse.jface.text.presentation.IPresentationReconciler; 
import org.eclipse.jface.text.presentation.PresentationReconciler; 
import org.eclipse.core.resources.IMarker; 

import java.util.Iterator; 
import java.util.ArrayList; 

public class Main { 
	
// error identifiers, images and colors 
	public static String ERROR_TYPE = "error.type"; 
	public static Image ERROR_IMAGE; 
	public static final RGB ERROR_RGB = new RGB(255, 0, 0); 
	
// annotation model 
	private AnnotationModel fAnnotationModel = new AnnotationModel(); 
	
	public static void main(String [] args) { 
		new Main(); 
	} 
	
	public Main() { 
		Display display = new Display(); 
		Shell parent = new Shell(display, SWT.SHELL_TRIM); 
		parent.setText("Annotation Test"); 
		parent.setLayout(new FillLayout()); 
		
		buildCodePage(parent); 
		
		parent.setSize(400, 400); 
		parent.open(); 
		
		while (!parent.isDisposed()) { 
			if (!display.readAndDispatch()) display.sleep(); 
		} 
		display.dispose(); 
	} 
	
	private void buildCodePage(Composite parent) { 
		ERROR_IMAGE = new Image(Display.getDefault(), 
			"d://blua/img/error_obj.gif"); 
		
		IAnnotationAccess fAnnotationAccess = new AnnotationMarkerAccess(); 
		
		ColorCache cc = new ColorCache(); 
		
// rulers 
		CompositeRuler fCompositeRuler = new CompositeRuler(); 
		OverviewRuler fOverviewRuler = new OverviewRuler(fAnnotationAccess, 
			12, cc); 
		AnnotationRulerColumn annotationRuler = new 
		AnnotationRulerColumn(fAnnotationModel, 16, fAnnotationAccess); 
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
		fOverviewRuler.setAnnotationTypeColor(ERROR_TYPE, new 
			Color(Display.getDefault(), ERROR_RGB)); 
		
// source viewer 
		SourceViewer sv = new SourceViewer(parent, fCompositeRuler, 
			fOverviewRuler, true, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL); 
		Document document = new Document(); 
		sv.setDocument(document, fAnnotationModel); 
		
// hover manager that shows text when we hover 
		AnnotationBarHoverManager fAnnotationHoverManager = new 
		AnnotationBarHoverManager(fCompositeRuler, sv, new AnnotationHover(), new 
			AnnotationConfiguration()); 
		fAnnotationHoverManager.install(annotationRuler.getControl()); 
		
// to paint the annotations 
		AnnotationPainter ap = new AnnotationPainter(sv, fAnnotationAccess); 
		ap.addAnnotationType(ERROR_TYPE); 
		ap.setAnnotationTypeColor(ERROR_TYPE, new 
			Color(Display.getDefault(), ERROR_RGB)); 
		
// this will draw the squigglies under the text 
		sv.addPainter(ap); 
		
		sv.configure(new CodeViewerConfiguration(cc)); 
		
// some misspelled text 
		document.set("Here's some texst so that we have somewhere to show an 
			error"); 
		
// add an annotation 
		ErrorAnnotation errorAnnotation = new ErrorAnnotation(1, "Learn how 
			to spell \"text!\""); 
		
// lets underline the word "texst" 
		fAnnotationModel.addAnnotation(errorAnnotation, new Position(12, 
			5)); 
	} 
	
	class AnnotationConfiguration implements IInformationControlCreator { 
		public IInformationControl createInformationControl(Shell shell) { 
			return new DefaultInformationControl(shell); 
		} 
	} 
	
	class ColorCache implements ISharedTextColors { 
		public Color getColor(RGB rgb) { 
			return new Color(Display.getDefault(), rgb); 
		} 
		
		public void dispose() { 
		} 
	} 
	
// santa's little helper 
	class AnnotationMarkerAccess implements IAnnotationAccess, 
	IAnnotationAccessExtension { 
		public Object getType(Annotation annotation) { 
			return annotation.getType(); 
		} 
		
		public boolean isMultiLine(Annotation annotation) { 
			return true; 
		} 
		
		public boolean isTemporary(Annotation annotation) { 
			return !annotation.isPersistent(); 
		} 
		
		public String getTypeLabel(Annotation annotation) { 
			if (annotation instanceof ErrorAnnotation) 
				return "Errors"; 
			
			return null; 
		} 
		
		public int getLayer(Annotation annotation) { 
			if (annotation instanceof ErrorAnnotation) 
				return ((ErrorAnnotation)annotation).getLayer(); 
			
			return 0; 
		} 
		
		public void paint(Annotation annotation, GC gc, Canvas canvas, 
			Rectangle bounds) { 
			ImageUtilities.drawImage(((ErrorAnnotation)annotation).getImage(), 
				gc, canvas, bounds, SWT.CENTER, SWT.TOP); 
		} 
		
		public boolean isPaintable(Annotation annotation) { 
			if (annotation instanceof ErrorAnnotation) 
				return ((ErrorAnnotation)annotation).getImage() != null; 
			
			return false; 
		} 
		
		public boolean isSubtype(Object annotationType, Object 
			potentialSupertype) { 
			if (annotationType.equals(potentialSupertype)) 
				return true; 
			
			return false; 
			
		} 
		
		public Object[] getSupertypes(Object annotationType) { 
			return new Object[0]; 
		} 
	} 
	
// source viewer configuration 
	class CodeViewerConfiguration extends SourceViewerConfiguration { 
		private ColorCache manager; 
		
		public CodeViewerConfiguration(ColorCache manager) { 
			this.manager = manager; 
		} 
		
		public IPresentationReconciler 
		getPresentationReconciler(ISourceViewer sourceViewer) { 
			PresentationReconciler reconciler = new 
			PresentationReconciler(); 
			return reconciler; 
		} 
		
		public IAnnotationHover getAnnotationHover(ISourceViewer 
			sourceViewer) { 
			return new AnnotationHover(); 
		} 
	} 
	
// annotation hover manager 
	class AnnotationHover implements IAnnotationHover, ITextHover { 
		public String getHoverInfo(ISourceViewer sourceViewer, int 
			lineNumber) { 
			Iterator ite = fAnnotationModel.getAnnotationIterator(); 
			
			ArrayList all = new ArrayList(); 
			
			while (ite.hasNext()) { 
				Annotation a = (Annotation) ite.next(); 
				if (a instanceof ErrorAnnotation) { 
					all.add(((ErrorAnnotation)a).getText()); 
				} 
			} 
			
			StringBuffer total = new StringBuffer(); 
			for (int x = 0; x String str = (String) all.get(x); 
				total.append(" " + str + (x == (all.size()-1) ? "" : "\n")); 
			} 
			
			return total.toString(); 
		} 
		
		public String getHoverInfo(ITextViewer textViewer, IRegion 
			hoverRegion) { 
			return null; 
		} 
		
		public IRegion getHoverRegion(ITextViewer textViewer, int offset) { 
			return null; 
		} 
	} 
	
// one error annotation 
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
		
		public String getText() { 
			return text; 
		} 
		
		public Image getImage() { 
			return ERROR_IMAGE; 
		} 
		
		public int getLayer() { 
			return 3; 
		} 
		
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
} 
