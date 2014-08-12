package ebook.module.book.views;

import java.lang.reflect.InvocationTargetException;
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

import ebook.module.book.BookConnection;
import ebook.module.book.ContextService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoSelection;
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
	BookConnection con;

	@Inject
	@Active
	@Optional
	SectionInfo section;

	private ContextService service;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONTEXT_VIEW(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_CONTEXT_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (!section.equals(data.section))
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_INFO) Object o,
			@Active @Optional SectionInfo data, final EHandlerService hs,
			final ECommandService cs) {
		if (data == null || service == null || treeComponent == null) {
			return;
		}

		service.setSection(section);
		treeComponent.updateInput();
		treeComponent.setSelection();

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTEXT_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_CONTEXT_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
			return;

		if (!section.equals(data.section))
			return;

		if (data.parent == null)
			return;

		// if (data.parent != null)
		viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

		// form.reflow(true);
	}

	@PostConstruct
	public void postConstruct(Composite parent, final Shell shell,
			EMenuService menuService, @Active final MWindow window) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		if (section == null) {
			section = new SectionInfo();
			section.setId(0);
		}
		service = con.ctxsrv(section);
		treeComponent = new TreeViewComponent(parent, service, 3, true, false);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				ContextInfoSelection sel = new ContextInfoSelection();
				@SuppressWarnings("unchecked")
				Iterator<SectionInfo> iterator = selection.iterator();
				while (iterator.hasNext())
					sel.add(iterator.next());

				window.getContext().set(ContextInfoSelection.class, sel);

				ContextInfo selected = (ContextInfo) selection
						.getFirstElement();
				window.getContext().set(ContextInfo.class, selected);

				if (selected != null)
					try {
						section.getOptions().selectedContext = selected.getId();

						con.srv().saveOptions(section);
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
				Strings.get("ebook.popupmenu.bookcontext"));

	}

}