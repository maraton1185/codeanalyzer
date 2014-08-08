package ebook.module.conf.views;

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

import ebook.module.conf.ConfConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoSelection;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class ListView {

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
	ConfConnection con;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_LIST_VIEW(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_LIST_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.equals(data.con))
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LABELS(
			@UIEventTopic(Events.EVENT_UPDATE_LABELS) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.equals(data.con))
			return;

		if (data.parent == null)
			return;

		if (!(data.parent instanceof ListInfo))
			return;

		viewer.update(data.parent, null);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LIST_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_LIST_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (!con.equals(data.con))
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

		treeComponent = new TreeViewComponent(parent, con.lsrv(), 3, true,
				false);
		// con.srv().setList(list);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				ListInfoSelection sel = new ListInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<ContextInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				window.getContext().set(ListInfoSelection.class, sel);

				window.getContext().set(ListInfo.class,
						(ListInfo) selection.getFirstElement());

			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				// ListInfo list = window.getContext().get(ListInfo.class);
				// App.br.post(Events.EVENT_SHOW_CONF_LIST, null);
				Utils.executeHandler(hs, cs, Strings.get("ListView.show"));
				// App.br.post(Events.EVENT_UPDATE_CONF_CONTEXT_PART,
				// new EVENT_UPDATE_VIEW_DATA(con, list));

			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.listview.popup"));

		// showSections();
	}

}