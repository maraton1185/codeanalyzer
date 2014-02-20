package ru.configviewer.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import ru.configviewer.utils.Const;

public class EditorConfiguration extends SourceViewerConfiguration {

//	private Editor editor;
	private RuleBasedScanner scanner;
//	private RuleBasedScanner string_scanner;
//	private RuleBasedScanner comment_scanner;
//	IColorManager provider = pico.get(IColorManager.class);
	
	
	public EditorConfiguration(Editor editor) {
//		this.editor = editor;
	}
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return Const.PARTITIONING;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		
		return new String[] { 
				IDocument.DEFAULT_CONTENT_TYPE};
	}
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	
		return reconciler;
	}
	
	protected ITokenScanner getScanner() {
		if (scanner == null) {
			scanner = new RuleBasedScanner();
			//scanner.setDefaultReturnToken(provider.getToken(TOKENS.OTHER));
		}
		return scanner;
	}
	
//	public IReconciler getReconciler(ISourceViewer sourceViewer)
//    {
//        ReconcilingStrategy strategy = new ReconcilingStrategy(editor);
//                
//        MonoReconciler reconciler = new MonoReconciler(strategy, false);
//        
//        return reconciler;
//    }
    
}
