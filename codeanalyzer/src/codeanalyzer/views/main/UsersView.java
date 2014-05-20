package codeanalyzer.views.main;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
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

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.book.BookInfoSelection;
import codeanalyzer.books.book.BookService;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.components.TreeViewComponent;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class UsersView {

	private TreeViewer viewer;

	@Inject
	public UsersView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_USERS_LIST(
			@UIEventTopic(Const.EVENT_EDIT_TITLE_USERS_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.selected == null)
			return;

		viewer.editElement(data.selected, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_USERS(
			@UIEventTopic(Const.EVENT_UPDATE_USERS) EVENT_UPDATE_TREE_DATA data) {

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

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				new BookService(), 3);

		viewer = treeComponent.getViewer();

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

}