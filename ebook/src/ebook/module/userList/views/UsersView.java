package ebook.module.userList.views;

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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.tree.TreeItemInfoSelection;
import ebook.module.tree.TreeViewComponent;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class UsersView {

	private TreeViewer viewer;
	private TreeViewComponent treeComponent;

	@Inject
	public UsersView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_USER_LIST_SET_SELECTION(
			@UIEventTopic(Events.EVENT_USER_LIST_SET_SELECTION) Object data) {

		treeComponent.setSelection();
	}

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_USERS_LIST(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_USERS_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.selected == null)
			return;

		viewer.editElement(data.selected, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_USERS(
			@UIEventTopic(Events.EVENT_UPDATE_USERS) EVENT_UPDATE_TREE_DATA data) {

		if (data.parent == null)
			return;

		// if (data.parent != null)
		viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

		// form.reflow(true);
	}

	@PreDestroy
	public void preDestroy(@Optional UserInfo data) {
		if (data != null) {
			PreferenceSupplier.set(PreferenceSupplier.SELECTED_USER,
					data.getId());
			PreferenceSupplier.save();
		}
	}

	@PostConstruct
	public void postConstruct(Composite parent, final Shell shell,
			EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		treeComponent = new TreeViewComponent(parent, App.srv.us(), 2, true);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				TreeItemInfoSelection sel = new TreeItemInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<UserInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				App.ctx.set("userListSelection", sel);

				App.ctx.set(UserInfo.class,
						(UserInfo) selection.getFirstElement());

				App.br.post(Events.EVENT_UPDATE_USER_INFO, null);
			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.userlistview.popup"));

	}

	@Focus
	public void OnFocus(@Active MWindow window, EPartService partService,
			EModelService model) {

		Utils.togglePart(window, model, "ebook.part.user",
				"ebook.partstack.editItem");

	}
}