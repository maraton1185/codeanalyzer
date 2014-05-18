package codeanalyzer.views.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
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
			final Shell shell) {

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

		// ImageHyperlink link;

		// toolkit = new FormToolkit(parent.getDisplay());
		// form = toolkit.createScrolledForm(parent);
		// ColumnLayout layout = new ColumnLayout();
		// layout.maxNumColumns = 2;
		// form.getBody().setLayout(layout);
		//
		// form.setText(Strings.get("appTitle"));
		//
		// bookSection = toolkit.createSection(form.getBody(),
		// // Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE |
		// // Section.EXPANDED
		// Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		//
		// bookSection.setText("Список книг");
		// bookSectionClient = toolkit.createComposite(bookSection);
		// bookSectionClient.setLayout(new GridLayout());
		// bookSectionHandler = new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// bm.openBook((CurrentBookInfo) e.getHref(), shell);
		// AppManager.br.post(Const.EVENT_SHOW_BOOK, null);
		//
		// super.linkActivated(e);
		// }
		//
		// };
		// bookSection.addExpansionListener(new ExpansionAdapter() {
		// @Override
		// public void expansionStateChanged(ExpansionEvent e) {
		// if (!e.getState())
		// for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
		// .getChildren()) {
		// ctrl.dispose();
		// }
		// else {
		// Utils.fillBooks(bookSectionClient, toolkit, shell,
		// bookSectionHandler);
		//
		// bookSection.setClient(bookSectionClient);
		// }
		// form.reflow(true);
		// }
		// });
		//
		// Utils.fillBooks(bookSectionClient, toolkit, shell,
		// bookSectionHandler);
		// bookSection.setClient(bookSectionClient);
	}
}