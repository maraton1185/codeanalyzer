package ebook.module.conf.views;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
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

import ebook.core.App;
import ebook.core.pico;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.ConfOptions;
import ebook.module.conf.tree.ContentProposalProvider;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.conf.tree.ListInfo;
import ebook.module.conf.tree.ListInfoSelection;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ICollapseView;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class ConfView implements ICollapseView {

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
	public void EVENT_UPDATE_CONF_VIEW_EDIT_TITLE(
			@UIEventTopic(Events.EVENT_UPDATE_CONF_VIEW_EDIT_TITLE) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (list != data.section)
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	MDirtyable dirty;

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@PreDestroy
	public void preDestroy() {
		if (dirty.isDirty()) {
			ListInfoSelection sel = new ListInfoSelection();
			sel.add(list);
			con.lsrv().delete(sel);
		}
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LABELS(
			@UIEventTopic(Events.EVENT_UPDATE_LABELS) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (data.parent == null)
			return;

		if (!(data.parent instanceof ContextInfo))
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

	@Inject
	@Optional
	public void EVENT_UPDATE_CONF_VIEW_EXPAND(
			@UIEventTopic(Events.EVENT_UPDATE_CONF_VIEW + "_EXPAND") EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (list != data.section)
			return;

		if (data.parent == null)
			return;

		viewer.refresh(data.parent);

		viewer.expandToLevel(data.parent, 1);

		// if (data.selected != null)
		// viewer.setSelection(new StructuredSelection(data.selected), true);

	}

	@PostConstruct
	public void postConstruct(Composite parent, final Shell shell,
			EMenuService menuService, @Active final MWindow window,
			@Active @Optional final ListInfo list) {

		this.list = list;

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		treeComponent = new TreeViewComponent(parent, con.srv(list), 1, true,
				false, new ContentProposalProvider(con.srv(list), window));

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

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				ITreeItemInfo selected = (ITreeItemInfo) selection
						.getFirstElement();

				try {
					ContextInfo item = pico
							.get(ICfServices.class)
							.build(con.srv(list).getConnection())
							.adapt(con.conf(), con.srv(list),
									(ContextInfo) selected);
					TextConnection text_con = new TextConnection(con, item, con
							.conf(), con.bmsrv());
					// if (item.isSearch()) {
					LineInfo line = new LineInfo(item.getOptions());
					text_con.setLine(line);
					// }
					App.br.post(Events.EVENT_OPEN_TEXT, text_con);
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				}

			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.model("ebook.listView.popup"));

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

	@Override
	public void CollapseAll() {
		viewer.collapseAll();
	}

	@Override
	public void ExpandAll() {
		viewer.expandToLevel(4);

	}

	@Override
	public void Expand() {
		ContextInfo item = window.getContext().get(ContextInfo.class);
		if (item != null)
			viewer.expandToLevel(item, 1);

	}

	@Override
	public void Collapse() {
		ContextInfo item = window.getContext().get(ContextInfo.class);
		if (item != null) {
			ContextInfo parent = (ContextInfo) con.srv(list).get(
					item.getParent());
			viewer.collapseToLevel(parent, 1);
		}

	}

}