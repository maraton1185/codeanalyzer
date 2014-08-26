package ebook.module.text.views;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_TEXT_DATA;
import ebook.utils.Strings;

public class TextView implements ITextOperationTarget {

	@Inject
	@Active
	MWindow window;

	@Inject
	@Active
	TextConnection con;

	ContextInfo item;
	// ITreeService srv;

	ProjectionViewer viewer;
	Document document;
	ViewerConfiguration viewerConfiguration;
	ViewerSupport support;

	private ArrayList<LineInfo> model;

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
	@Optional
	public void EVENT_UPDATE_TEXT_MODEL(
			@UIEventTopic(Events.EVENT_UPDATE_TEXT_MODEL) EVENT_TEXT_DATA data) {

		if (data.document != document)
			return;

		support.removeFolding();
		for (LineInfo info : data.model) {

			if (info.projection == null)
				continue;

			ProjectionAnnotation annotation = new ProjectionAnnotation(false);
			annotation.setText(info.getTitle() + ":" + info.length.toString());
			support.addProjection(annotation, info.projection);

		}
		this.model = data.model;

		App.br.post(Events.EVENT_UPDATE_OUTLINE_VIEW, new EVENT_TEXT_DATA(con,
				document, model));
	}

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_OUTLINE_SELECTED(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_OUTLINE_SELECTED) EVENT_TEXT_DATA data) {

		if (data.document != document)
			return;
		if (data.selected == null)
			return;

		support.setSelection(data.selected);
	}

	@Inject
	MDirtyable dirty;
	private ContextInfo parentItem;

	@Persist
	public void save() {
		con.srv().saveItemText(document.get());
		dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		item = con.getItem();
		con.srv().copyItemPath();

		parentItem = con.srv().get(item.getParent());

		ContextInfoOptions opt = item.getOptions();
		final boolean readOnly = opt != null && opt.type == BuildType.module;
		int style = readOnly ? SWT.READ_ONLY : SWT.NONE;

		// int style = SWT.NONE;
		support = new ViewerSupport();
		viewer = support.getViewer(parent, style);

		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.getTextWidget().addCaretListener(new CaretListener() {
			@Override
			public void caretMoved(CaretEvent event) {

				int offset = event.caretOffset;
				LineInfo selected = support.getCurrentProjectionName(offset);

				window.getContext().set(Events.TEXT_VIEW_ACTIVE_PROCEDURE,
						readOnly ? selected : null);
				App.br.post(Events.EVENT_OUTLINE_VIEW_SET_CURRENT,
						new EVENT_TEXT_DATA(con, document, selected));

			}
		});

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

		menuService.registerContextMenu(viewer.getTextWidget(),
				Strings.model("ebook.popupmenu.TextView"));

		// ErrorAnnotation errorAnnotation = new ErrorAnnotation(
		// "Learn how to spell \"text!\"");
		//
		// support.addAnnotation(errorAnnotation, new Position(120, 5));
		//
		// InfoAnnotation infoAnnotation = new InfoAnnotation(
		// "Learn how to spell \"text!\"");
		//
		// support.addAnnotation(infoAnnotation, new Position(240, 5));

		// ProjectionAnnotation annotation = new ProjectionAnnotation(false);
		// support.addProjection(annotation, new Position(0, 400));

	}

	@Focus
	public void OnFocus() {
		if (con.getParent() == parentItem)
			return;

		if (model != null)
			App.br.post(Events.EVENT_UPDATE_OUTLINE_VIEW, new EVENT_TEXT_DATA(
					con, document, model));

		con.setItem(item);

		parentItem = con.srv().get(item.getParent());
		if (parentItem != null) {
			ContextInfoOptions opt1 = parentItem.getOptions();
			if (opt1.type == BuildType.module)
				con.setParent(parentItem);
			else
				con.setParent(null);
		} else
			con.setParent(null);

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
