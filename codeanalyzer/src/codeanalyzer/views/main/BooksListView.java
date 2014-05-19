package codeanalyzer.views.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.books.book.BookService;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.components.TreeViewComponent;
import codeanalyzer.core.model.BookInfo;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class BooksListView {

	// FormToolkit toolkit;
	// ScrolledForm form;
	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;
	private TreeViewer viewer;

	// private IDbService dbManager = pico.get(IDbService.class);

	@Inject
	public BooksListView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_BOOK_LIST(
			@UIEventTopic(Const.EVENT_EDIT_TITLE_BOOK_LIST) EVENT_UPDATE_BOOK_LIST_DATA data) {

		if (data.selected == null)
			return;

		viewer.editElement(data.selected, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_LIST(
			@UIEventTopic(Const.EVENT_UPDATE_BOOK_LIST) EVENT_UPDATE_BOOK_LIST_DATA data) {

		if (data.parent != null)
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
				new BookService());

		viewer = booksList.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				AppManager.ctx.set(BookInfo.class,
						(BookInfo) selection.getFirstElement());
			}
		});

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.booklistview.popup"));

	}
}