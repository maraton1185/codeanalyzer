package ebook.module.text.views;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
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

import ebook.module.conf.tree.ListInfo;
import ebook.module.text.TextConnection;
import ebook.module.text.tree.BookmarkInfo;
import ebook.module.text.tree.BookmarkInfoSelection;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class BookmarkView {
	private TreeViewer viewer;
	private TreeViewComponent treeComponent;

	@Inject
	@Active
	MWindow window;
	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Inject
	@Active
	TextConnection con;

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOKMARK_VIEW_EDIT_TITLE(
			@UIEventTopic(Events.EVENT_UPDATE_BOOKMARK_VIEW_EDIT_TITLE) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.getCon().equals(data.con))
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LABELS(
			@UIEventTopic(Events.EVENT_UPDATE_LABELS) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.getCon().equals(data.con))
			return;

		if (data.parent == null)
			return;

		if (!(data.parent instanceof BookmarkInfo))
			return;

		viewer.update(data.parent, null);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOKMARK_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_BOOKMARK_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.getCon().equals(data.con))
			return;

		if (data.parent == null)
			return;

		viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

	}

	@PostConstruct
	public void postConstruct(Composite parent, final Shell shell,
			EMenuService menuService, @Active final MWindow window,
			@Active @Optional ListInfo list) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		treeComponent = new TreeViewComponent(parent, con.bmkSrv(), 3, true,
				true);
		// con.srv().setList(list);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				BookmarkInfoSelection sel = new BookmarkInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<BookmarkInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				window.getContext().set(BookmarkInfoSelection.class, sel);

				window.getContext().set(BookmarkInfo.class,
						(BookmarkInfo) selection.getFirstElement());

			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				// ListInfo list = window.getContext().get(ListInfo.class);
				// App.br.post(Events.EVENT_SHOW_CONF_LIST, null);
				// Utils.executeHandler(hs, cs, Strings.model("ListView.show"));
				// App.br.post(Events.EVENT_UPDATE_CONF_CONTEXT_PART,
				// new EVENT_UPDATE_VIEW_DATA(con, list));

			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.model("bookmarkview.popup"));

		// showSections();
	}
}
