package ebook.module.text.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.TextConnection;
import ebook.module.text.annotations.ErrorAnnotation;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.utils.Events;
import ebook.utils.Strings;

public class TextView implements ITextOperationTarget {

	@Inject
	@Active
	MWindow window;

	@Inject
	@Active
	TextConnection con;

	ITreeItemInfo item;
	ITreeService srv;

	ProjectionViewer viewer;
	Document document;
	ViewerConfiguration viewerConfiguration;

	// private IVerticalRuler fVerticalRuler;
	// protected static final int VERTICAL_RULER_WIDTH = 12;

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_DOUBLE_CLICK(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_DOUBLE_CLICK) Object o) {

		ITextSelection textSelection = (ITextSelection) viewer
				.getSelectionProvider().getSelection();
		String _line;
		try {
			_line = document.get(textSelection.getOffset(),
					textSelection.getLength());
			viewerConfiguration.lightWord(_line);
		} catch (BadLocationException e) {

			e.printStackTrace();
		}

	}

	@Inject
	MDirtyable dirty;
	private ITreeItemInfo parentItem;

	@Persist
	public void save() {
		con.srv().saveItemText(document.get());
		dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		item = con.getItem();
		srv = con.getSrv();

		parentItem = srv.get(item.getParent());

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		int style = opt.type == BuildType.module ? SWT.READ_ONLY : SWT.NONE;

		ViewerSupport support = new ViewerSupport();
		viewer = support.getViewer(parent, style);

		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		document = support.getDocument();

		String text = con.srv().getItemText();
		if (text == null)
			text = Strings.msg("TextView.errorGetText");
		document.set(text);

		document.addDocumentListener(new IDocumentListener() {
			@Override
			public void documentChanged(DocumentEvent event) {
				dirty.setDirty(true);
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
		});

		viewerConfiguration = new ViewerConfiguration(viewer,
				support.getAnnotationModel());

		viewer.configure(viewerConfiguration);

		ErrorAnnotation errorAnnotation = new ErrorAnnotation(
				"Learn how to spell \"text!\"");

		support.addAnnotation(errorAnnotation, new Position(120, 5));
		//
		// InfoAnnotation infoAnnotation = new InfoAnnotation(
		// "Learn how to spell \"text!\"");
		//
		// support.addAnnotation(infoAnnotation, new Position(240, 5));

		ProjectionAnnotation annotation = new ProjectionAnnotation(false);
		support.addProjection(annotation, new Position(0, 400));

	}

	@Focus
	public void OnFocus(@Active @Optional ITreeItemInfo parent) {
		if (parent == parentItem)
			return;

		parentItem = srv.get(item.getParent());
		if (parentItem != null) {
			ContextInfoOptions opt1 = (ContextInfoOptions) parentItem
					.getOptions();
			if (opt1.type == BuildType.module)
				window.getContext().set(ITreeItemInfo.class, parentItem);
			else
				window.getContext().set(ITreeItemInfo.class, null);
		} else
			window.getContext().set(ITreeItemInfo.class, null);

	}

	@Override
	public boolean canDoOperation(int operation) {
		return viewer.canDoOperation(operation);
	}

	@Override
	public void doOperation(int operation) {
		viewer.doOperation(operation);

	}

}
