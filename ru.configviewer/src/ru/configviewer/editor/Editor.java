package ru.configviewer.editor;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import ru.configviewer.core.IEditorFactory;
import ru.configviewer.core.pico;

public class Editor extends TextEditor {

	IEditorFactory factory = pico.get(IEditorFactory.class);
	
	public final static String ID = "ru.configviewer.editor";
	public final static String CONTEXT_ID = "ru.configviewer.editor.context";
	
	public Editor() {
		super();
		setSourceViewerConfiguration(new EditorConfiguration(this));
		setDocumentProvider(new DocumentProvider());
		setEditorContextMenuId(CONTEXT_ID);
		makeActions();

	}

	private void makeActions() {
		// TODO Auto-generated method stub
		
	}
	
	public void dispose() {
		IEditorInput input = getEditorInput();
		if(input instanceof EditorInput)
			factory.deleteInput((EditorInput)input);
		
		super.dispose();		
	}
	
	@Override
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new EditorSourceViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles, this);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		// do any custom stuff
		
		return viewer;
	}

	public void update() {
		setInput(getEditorInput());
		
	}
}
