package ebook.module.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import ebook.core.pico;
import ebook.core.interfaces.IColorManager;
import ebook.core.interfaces.IColorManager.TOKENS;
import ebook.module.text.model.DocumentPartitionScanner;
import ebook.module.text.model.ReconcilingStrategy;
import ebook.module.text.model.ScannerCode;
import ebook.module.text.model.ScannerComment;
import ebook.module.text.model.ScannerString;
import ebook.module.text.model.TextDoubleClickStrategy;

public class EditorConfiguration extends SourceViewerConfiguration {
	private RuleBasedScanner scanner;
	private RuleBasedScanner string_scanner;
	private RuleBasedScanner comment_scanner;
	IColorManager provider = pico.get(IColorManager.class);

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

		return reconciler;
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
		ReconcilingStrategy strategy = new ReconcilingStrategy();

		MonoReconciler reconciler = new MonoReconciler(strategy, false);

		return reconciler;
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {

		return new TextDoubleClickStrategy();
		// super.getDoubleClickStrategy(sourceViewer, contentType);
	}

	public void lightWord(String text) {
		((ScannerCode) getCodeScanner()).setScannerRules(text);
		((ScannerString) getStringScanner()).setScannerRules(text);
		((ScannerComment) getCommentScanner()).setScannerRules(text);

		// getPresentationReconciler(null).install(editor.getViewer());
	}
}
