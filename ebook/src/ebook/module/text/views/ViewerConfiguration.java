package ebook.module.text.views;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.interfaces.IColorManager;
import ebook.core.interfaces.IColorManager.TOKENS;
import ebook.module.text.annotations.AnnotationHover;
import ebook.module.text.scanner.DocumentPartitionScanner;
import ebook.module.text.scanner.ScannerCode;
import ebook.module.text.scanner.ScannerComment;
import ebook.module.text.scanner.ScannerString;
import ebook.module.text.strategy.ReconcilingStrategy;
import ebook.module.text.strategy.TextDoubleClickStrategy;
import ebook.utils.Events;

public class ViewerConfiguration extends SourceViewerConfiguration {
	private RuleBasedScanner scanner;
	private RuleBasedScanner string_scanner;
	private RuleBasedScanner comment_scanner;
	IColorManager provider = pico.get(IColorManager.class);
	private ProjectionViewer viewer;
	private IAnnotationModel annotationModel;

	IPresentationReconciler fPresentationReconciler;
	IReconciler fReconciler;

	public ViewerConfiguration(ProjectionViewer viewer,
			IAnnotationModel annotationModel) {
		this.viewer = viewer;
		this.annotationModel = annotationModel;
	}

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return "___my__partitioning____";
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {

		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				DocumentPartitionScanner.STRING,
				DocumentPartitionScanner.COMMENT };
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		if (fPresentationReconciler != null)
			return fPresentationReconciler;

		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		DefaultDamagerRepairer dr1 = new DefaultDamagerRepairer(
				getStringScanner());
		reconciler.setDamager(dr1, DocumentPartitionScanner.STRING);
		reconciler.setRepairer(dr1, DocumentPartitionScanner.STRING);

		DefaultDamagerRepairer dr2 = new DefaultDamagerRepairer(
				getCommentScanner());
		reconciler.setDamager(dr2, DocumentPartitionScanner.COMMENT);
		reconciler.setRepairer(dr2, DocumentPartitionScanner.COMMENT);

		fPresentationReconciler = reconciler;
		return fPresentationReconciler;
	}

	private ITokenScanner getCommentScanner() {
		if (comment_scanner == null) {
			comment_scanner = new ScannerComment();
			comment_scanner.setDefaultReturnToken(provider
					.getToken(TOKENS.COMMENT));
		}
		return comment_scanner;
	}

	private ITokenScanner getStringScanner() {
		if (string_scanner == null) {
			string_scanner = new ScannerString();
			string_scanner.setDefaultReturnToken(provider
					.getToken(TOKENS.STRING));
		}
		return string_scanner;
	}

	protected ITokenScanner getCodeScanner() {
		if (scanner == null) {
			scanner = new ScannerCode();
			// scanner.setDefaultReturnToken(provider.getToken(TOKENS.OTHER));
		}
		return scanner;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (fReconciler != null)
			return fReconciler;

		ReconcilingStrategy strategy = new ReconcilingStrategy();

		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		fReconciler = reconciler;
		return fReconciler;
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {

		return new TextDoubleClickStrategy();
		// super.getDoubleClickStrategy(sourceViewer, contentType);
	}

	public void lightWord(String text, boolean markers) {
		((ScannerCode) getCodeScanner()).setScannerRules(text);
		((ScannerString) getStringScanner()).setScannerRules(text);
		((ScannerComment) getCommentScanner()).setScannerRules(text);

		IPresentationReconciler pr = getPresentationReconciler(null);
		pr.uninstall();
		pr.install(viewer);

		if (markers) {
			IReconciler rec = getReconciler(null);
			ReconcilingStrategy str = (ReconcilingStrategy) rec
					.getReconcilingStrategy("");
			str.setSearchText(text);
			rec.uninstall();
			rec.install(viewer);
		} else
			App.br.post(Events.EVENT_TEXT_VIEW_REMOVE_MARKERS, viewer);
	}

	public void update() {
		IPresentationReconciler pr = getPresentationReconciler(null);
		pr.uninstall();
		pr.install(viewer);
	}

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new AnnotationHover(annotationModel);
	}

}
