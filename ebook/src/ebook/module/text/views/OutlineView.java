package ebook.module.text.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.text.service.OutlineService;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_TEXT_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OutlineView {

	private TreeViewer viewer;
	private TreeViewComponent treeComponent;
	private OutlineService service;

	@Inject
	@Active
	TextConnection con;
	private IDocument document;

	@Inject
	@Active
	MWindow window;
	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	boolean doSelection = false;

	@Inject
	@Optional
	public void EVENT_UPDATE_OUTLINE_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_OUTLINE_VIEW) EVENT_TEXT_DATA data) {

		if (data.con != con)
			return;
		if (service == null || treeComponent == null || data.document == null
				|| data.model == null) {
			return;
		}

		this.document = data.document;
		service.setModel(data.model);
		doSelection = false;
		treeComponent.updateInput();
		treeComponent.setSelection();
		doSelection = true;
	}

	@Inject
	@Optional
	public void EVENT_OUTLINE_VIEW_SET_CURRENT(
			@UIEventTopic(Events.EVENT_OUTLINE_VIEW_SET_CURRENT) EVENT_TEXT_DATA data) {

		if (data.con != con)
			return;

		if (data.document != document)
			return;
		if (service == null || treeComponent == null || data.selected == null) {
			return;
		}
		doSelection = false;
		service.setSelection(data.selected);
		treeComponent.setSelection();
		doSelection = true;

	}

	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		service = new OutlineService();
		treeComponent = new TreeViewComponent(parent, service, 3, false);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!doSelection)
					return;
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				LineInfo selected = (LineInfo) selection.getFirstElement();
				App.br.post(Events.EVENT_TEXT_VIEW_OUTLINE_SELECTED,
						new EVENT_TEXT_DATA(con, document, selected));
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				LineInfo selected = (LineInfo) selection.getFirstElement();

				window.getContext().set(Events.TEXT_VIEW_ACTIVE_PROCEDURE,
						selected);
				Utils.executeHandler(hs, cs, Strings.model("TextView.goToProc"));
			}
		});

		treeComponent.setSelection();

		// menuService.registerContextMenu(viewer.getControl(),
		// Strings.model("ebook.popupmenu.conflist"));
	}
}
