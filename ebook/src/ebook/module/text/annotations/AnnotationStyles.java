package ebook.module.text.annotations;

import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.jface.text.source.AnnotationPainter.ITextStyleStrategy;
import org.eclipse.swt.SWT;

public class AnnotationStyles {

	public static final String STYLE_NONE = "NONE"; //$NON-NLS-1$
	public static final String STYLE_SQUIGGLES = "SQUIGGLES"; //$NON-NLS-1$
	public static final String STYLE_PROBLEM_UNDERLINE = "PROBLEM_UNDERLINE"; //$NON-NLS-1$
	public static final String STYLE_BOX = "BOX"; //$NON-NLS-1$
	public static final String STYLE_DASHED_BOX = "DASHED_BOX"; //$NON-NLS-1$
	public static final String STYLE_UNDERLINE = "UNDERLINE"; //$NON-NLS-1$

	public static ITextStyleStrategy fgBoxStrategy = new AnnotationPainter.BoxStrategy(
			SWT.BORDER_SOLID);
	public static ITextStyleStrategy fgDashedBoxStrategy = new AnnotationPainter.BoxStrategy(
			SWT.BORDER_DASH);
	public static IDrawingStrategy fgNullStrategy = new AnnotationPainter.NullStrategy();
	public static ITextStyleStrategy fgUnderlineStrategy = new AnnotationPainter.UnderlineStrategy(
			SWT.UNDERLINE_SINGLE);
	public static ITextStyleStrategy fgSquigglesStrategy = new AnnotationPainter.UnderlineStrategy(
			SWT.UNDERLINE_SQUIGGLE);
	public static ITextStyleStrategy fgProblemUnderlineStrategy = new AnnotationPainter.UnderlineStrategy(
			SWT.UNDERLINE_ERROR);

}
