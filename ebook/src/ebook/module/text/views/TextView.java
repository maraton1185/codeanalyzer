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
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.conf.tree.ContextInfo;
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
	Object activated;
	// ITreeService srv;

	ProjectionViewer viewer;
	Document document;
	ViewerConfiguration viewerConfiguration;
	ViewerSupport support;

	private ArrayList<LineInfo> model;
	private boolean updateActiveProcedure = true;
	private boolean updateSelected = false;

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_FIND_TEXT(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_FIND_TEXT) ContextInfo item,
			Shell shell) {

		if (!this.item.equals(item))
			return;

		ITextSelection textSelection = (ITextSelection) viewer
				.getSelectionProvider().getSelection();

		try {
			String line = document.get(textSelection.getOffset(),
					textSelection.getLength());

			FindDialog dlg = new FindDialog(shell);
			dlg.setText(line);
			dlg.open();

			// if (_line.isEmpty())
			// return;
			// con.srv().buildText(item, _line, shell);
			// viewerConfiguration.lightWord(_line);
		} catch (BadLocationException e) {

			e.printStackTrace();
		}

	}

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_BUILD_TEXT(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_BUILD_TEXT) ContextInfo item,
			Shell shell) {

		if (!this.item.equals(item))
			return;

		ITextSelection textSelection = (ITextSelection) viewer
				.getSelectionProvider().getSelection();
		String _line;
		try {
			_line = document.get(textSelection.getOffset(),
					textSelection.getLength());
			if (_line.isEmpty())
				return;
			con.srv().buildText(item, _line, shell);
			// viewerConfiguration.lightWord(_line);
		} catch (BadLocationException e) {

			e.printStackTrace();
		}

	}

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_DOUBLE_CLICK(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_DOUBLE_CLICK) ITextViewer text) {

		if (text != viewer)
			return;

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
		if (updateSelected)
			support.setSelection(support.getProjectionByName(con.getLine()));

		updateSelected = false;

		if (!con.getItem().equals(item))
			return;
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
	@Optional
	public void EVENT_TEXT_VIEW_UPDATE(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_UPDATE) EVENT_TEXT_DATA data) {
		if (data.parent == null)
			return;
		if (!data.parent.equals(item))
			return;
		updateText();
	}

	public void updateText() {

		String text = con.srv().getItemText(item);
		if (text == null)
			text = Strings.msg("TextView.errorGetText");

		updateSelected = true;
		updateActiveProcedure = false;
		document.set(text);
		updateActiveProcedure = true;
		dirty.setDirty(false);
	}

	@Inject
	MDirtyable dirty;

	// private ContextInfo parentItem;

	@Persist
	public void save() {
		con.srv().saveItemText(document.get());
		dirty.setDirty(false);
		App.br.post(Events.EVENT_TEXT_VIEW_UPDATE,
				new EVENT_TEXT_DATA(con.getModule()));
	}

	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {

		support = new ViewerSupport();

		item = con.getItem();
		activated = new Object();
		support.setSelection(support.getProjectionByName(con.getLine()));

		final boolean readOnly = con.srv().readOnly(item);

		int style = readOnly ? SWT.READ_ONLY : SWT.NONE;
		viewer = support.getViewer(parent, style);

		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.getTextWidget().addCaretListener(new CaretListener() {
			@Override
			public void caretMoved(CaretEvent event) {

				if (!updateActiveProcedure)
					return;
				int offset = event.caretOffset;
				LineInfo selected = support.getCurrentProjectionName(offset);

				window.getContext().set(Events.TEXT_VIEW_ACTIVE_PROCEDURE,
						selected);
				App.br.post(Events.EVENT_OUTLINE_VIEW_SET_CURRENT,
						new EVENT_TEXT_DATA(con, document, selected));

			}
		});

		document = support.getDocument();

		String text = con.srv().getItemText(item);
		// if (text == null)
		// text = ""Strings.msg("TextView.errorGetText");
		updateActiveProcedure = false;
		document.set(text == null ? "" : text);
		updateActiveProcedure = true;

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

		if (con.getActivated().equals(activated))
			return;

		con.setItem(item);
		con.setActivated(activated);

		updateSelected = true;
		support.setSelection(support.getProjectionByName(con.getLine()));

		if (model != null)
			App.br.post(Events.EVENT_UPDATE_OUTLINE_VIEW, new EVENT_TEXT_DATA(
					con, document, model));

	}

	@Override
	public boolean canDoOperation(int operation) {
		return viewer.canDoOperation(operation);
	}

	@Override
	public void doOperation(int operation) {
		viewer.doOperation(operation);

	}

	public void CollapseAll() {
		viewer.getProjectionAnnotationModel().collapseAll(0,
				document.getLength());
	}

	public void Collapse() {
		LineInfo selected = (LineInfo) window.getContext().get(
				Events.TEXT_VIEW_ACTIVE_PROCEDURE);
		if (selected != null)
			viewer.getProjectionAnnotationModel().collapse(selected.annotation);

	}

	public void ExpandAll() {
		viewer.getProjectionAnnotationModel()
				.expandAll(0, document.getLength());

	}

	public void Expand() {
		LineInfo selected = (LineInfo) window.getContext().get(
				Events.TEXT_VIEW_ACTIVE_PROCEDURE);
		if (selected != null)
			viewer.getProjectionAnnotationModel().expand(selected.annotation);

	}

}
