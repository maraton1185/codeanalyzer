package codeanalyzer.module.books.views;

import java.util.Iterator;

import javax.annotation.PostConstruct;
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

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.module.books.interfaces.IBookManager;
import codeanalyzer.module.books.list.BookInfo;
import codeanalyzer.module.books.list.BookInfoSelection;
import codeanalyzer.module.books.list.BookService;
import codeanalyzer.module.tree.TreeViewComponent;
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

	@PostConstruct
	public void postConstruct(Composite parent, final IBookManager bm,
			final Shell shell, EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent booksList = new TreeViewComponent(parent,
				new BookService(), 3);

		viewer = booksList.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				BookInfoSelection sel = new BookInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<BookInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				AppManager.ctx.set(BookInfoSelection.class, sel);

				AppManager.ctx.set(BookInfo.class,
						(BookInfo) selection.getFirstElement());
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				BookInfo selected = (BookInfo) selection.getFirstElement();
				bm.openBook(selected.getPath(), shell);
			}
		});

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