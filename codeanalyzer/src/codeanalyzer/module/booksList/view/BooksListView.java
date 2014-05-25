package codeanalyzer.module.booksList.view;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.App;
import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.module.booksList.tree.ListBookInfo;
import codeanalyzer.module.booksList.tree.ListBookInfoSelection;
import codeanalyzer.module.tree.TreeViewComponent;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class BooksListView {

	private TreeViewer viewer;

	@Inject
	public BooksListView() {
		// TODO Your code here
	}


	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_BOOK_LIST(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_BOOK_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.selected == null)
			return;

		viewer.editElement(data.selected, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_LIST(
			@UIEventTopic(Events.EVENT_UPDATE_BOOK_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.parent == null)
			return;

		// if (data.parent != null)
		viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

		// form.reflow(true);
	}

	@PreDestroy
	public void preDestroy(@Optional ListBookInfo data) {
		if (data != null) {
			PreferenceSupplier.set(PreferenceSupplier.SELECTED_BOOK,
					data.getId());
			PreferenceSupplier.save();
		}
	}

	@PostConstruct
	public void postConstruct(Composite parent, final IBookListManager blm,
			final Shell shell, EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				App.srv.bls(), 3);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				ListBookInfoSelection sel = new ListBookInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<ListBookInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				// AppManager;

				App.ctx.set(ListBookInfoSelection.class, sel);

				App.ctx.set(ListBookInfo.class,
						(ListBookInfo) selection.getFirstElement());

				App.br.post(Events.EVENT_UPDATE_BOOK_INFO, null);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				ListBookInfo selected = (ListBookInfo) selection
						.getFirstElement();
				blm.openBook(selected.getPath(), shell);
			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.booklistview.popup"));
		

	}

	@Focus
	public void OnFocus(@Active MWindow window, EPartService partService,
			EModelService model) {

		Utils.togglePart(window, model, "codeanalyzer.part.book",
				"codeanalyzer.partstack.editItem");

	}
}