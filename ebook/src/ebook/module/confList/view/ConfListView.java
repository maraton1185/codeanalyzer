package ebook.module.confList.view;

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

import ebook.core.App;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.tree.item.TreeItemInfoSelection;
import ebook.module.tree.view.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class ConfListView {

	private TreeViewer viewer;
	private TreeViewComponent treeComponent;

	@Inject
	public ConfListView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONF_LIST(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_CONF_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.selected == null)
			return;

		viewer.editElement(data.selected, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONF_LIST(
			@UIEventTopic(Events.EVENT_UPDATE_CONF_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.parent == null)
			return;

		// if (data.parent != null)
		viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

		// form.reflow(true);
	}

	@Inject
	@Optional
	public void EVENT_CONF_LIST_SET_SELECTION(
			@UIEventTopic(Events.EVENT_CONF_LIST_SET_SELECTION) Object data) {

		treeComponent.setSelection();
	}

	@PreDestroy
	public void preDestroy(@Optional ListConfInfo data) {
		if (data != null) {
			PreferenceSupplier.set(PreferenceSupplier.SELECTED_CONF,
					data.getId());
			PreferenceSupplier.save();
		}
	}

	@PostConstruct
	public void postConstruct(Composite parent, final Shell shell,
			EMenuService menuService) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		treeComponent = new TreeViewComponent(parent, App.srv.cl(), 3, true);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				TreeItemInfoSelection sel = new TreeItemInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<ListConfInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				// AppManager;

				App.ctx.set("confListSelection", sel);

				App.ctx.set(ListConfInfo.class,
						(ListConfInfo) selection.getFirstElement());

				App.br.post(Events.EVENT_UPDATE_CONF_INFO, null);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				ListConfInfo selected = (ListConfInfo) selection
						.getFirstElement();
				App.mng.clm().open(selected.getPath(), shell);
			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.model("ebook.popupmenu.conflist"));

	}

	@Focus
	public void OnFocus(@Active MWindow window, EPartService partService,
			EModelService model) {

		Utils.togglePart(window, model, "ebook.part.conf",
				"ebook.partstack.editItem");

	}
}