package ebook.module.conf.views;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
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

import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfOptions;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.conf.tree.ListInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class ContextView {

	private TreeViewer viewer;
	private TreeViewComponent treeComponent;

	@Inject
	@Active
	MWindow window;

	@Inject
	@Active
	ConfConnection con;

	ListInfo list;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONF_VIEW(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_CONF_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (list != data.section)
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LABELS(
			@UIEventTopic(Events.EVENT_UPDATE_LABELS) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		// if (list != data.section)
		// return;

		if (data.parent == null)
			return;

		viewer.update(data.parent, null);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONF_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_CONF_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (list != data.section)
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
			@Active @Optional final ListInfo list) {

		this.list = list;

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		treeComponent = new TreeViewComponent(parent, con.srv(list), 3, true,
				false);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				ContextInfoSelection sel = new ContextInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<ContextInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				window.getContext().set(ContextInfoSelection.class, sel);

				ContextInfo selected = (ContextInfo) selection
						.getFirstElement();
				window.getContext().set(ContextInfo.class, selected);

				if (selected != null)
					try {
						if (list != null) {
							list.getOptions().selectedContext = selected
									.getId();
							con.lsrv().saveOptions(list);
						} else {
							ConfOptions opt = con.srv(null).getRootOptions(
									ConfOptions.class);

							if (opt.selectedSection != selected.getId()) {
								opt.selectedSection = selected.getId();
								con.srv(null).saveRootOptions(opt);
							}
						}

					} catch (InvocationTargetException e) {

						e.printStackTrace();
					}
				// App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				// IStructuredSelection selection = (IStructuredSelection)
				// viewer
				// .getSelection();
				// ListBookInfo selected = (ListBookInfo) selection
				// .getFirstElement();
				// App.mng.blm().open(selected.getPath(), shell);
			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("ebook.listView.popup"));

		window.getContext().set(Events.CONTEXT_ACTIVE_LIST, list);
	}

	@Focus
	public void OnFocus(@Named(Events.CONTEXT_ACTIVE_LIST) ListInfo _list,
			EPartService partService, MPart part) {
		if (list == _list)
			return;

		window.getContext().set(Events.CONTEXT_ACTIVE_LIST, list);

		if (list != null) {
			ITreeItemInfo item = con.lsrv().get(list.getId());
			if (item == null) {
				partService.hidePart(part);
				return;
			}
		}

		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		viewer.setSelection(selection, true);
	}

	public Integer getId() {
		return list == null ? 0 : list.getId();
	}

}