package codeanalyzer.module.users.views;

import java.util.Iterator;

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

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.module.books.interfaces.IBookManager;
import codeanalyzer.module.tree.TreeViewComponent;
import codeanalyzer.module.users.UserInfo;
import codeanalyzer.module.users.UserInfoSelection;
import codeanalyzer.module.users.UserService;
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

	@PostConstruct
	public void postConstruct(Composite parent, final IBookManager bm,
			final Shell shell, EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				new UserService(), 2);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				UserInfoSelection sel = new UserInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<UserInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				AppManager.ctx.set(UserInfoSelection.class, sel);

				AppManager.ctx.set(UserInfo.class,
						(UserInfo) selection.getFirstElement());
			}
		});

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.userlistview.popup"));

	}

}