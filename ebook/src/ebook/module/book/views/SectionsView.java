package ebook.module.book.views;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
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

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.model.BookOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoSelection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.view.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class SectionsView {

	private TreeViewer viewer;

	@Inject
	@Active
	BookConnection con;

	private MWindow window;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (con != data.con)
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

		if (data.parent == null)
			return;

		if (!(data.parent instanceof SectionInfo))
			return;

		viewer.update(data.parent, null);

	}

	@Inject
	@Optional
	public void EVENT_SET_SECTION_CONTEXT(
			@UIEventTopic(Events.EVENT_SET_SECTION_CONTEXT) Object data) {

		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();

		SectionInfoSelection sel = new SectionInfoSelection();
		@SuppressWarnings("unchecked")
		Iterator<ListBookInfo> iterator = selection.iterator();
		while (iterator.hasNext())
			sel.add(iterator.next());

		window.getContext().set(SectionInfoSelection.class, sel);

		window.getContext().set(SectionInfo.class,
				(SectionInfo) selection.getFirstElement());

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs,
			EModelService model, @Active MWindow bookWindow) {

		if (con != data.con)
			return;

		if (data.parent != null)
			viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

	}

	@PostConstruct
	public void postConstruct(Composite parent, @Active final MWindow window,
			EMenuService menuService, final EHandlerService hs,
			final ECommandService cs) {

		this.window = window;

		panelVisible();

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				con.srv(), 2, true);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				EVENT_SET_SECTION_CONTEXT(null);
				// IStructuredSelection selection = (IStructuredSelection)
				// viewer
				// .getSelection();
				//
				// SectionInfoSelection sel = new SectionInfoSelection();
				// @SuppressWarnings("unchecked")
				// Iterator<ListBookInfo> iterator = selection.iterator();
				// while (iterator.hasNext())
				// sel.add(iterator.next());
				//
				// window.getContext().set(SectionInfoSelection.class, sel);
				//
				// window.getContext().set(SectionInfo.class,
				// (SectionInfo) selection.getFirstElement());

				App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				// SectionInfo section = window.getContext()
				// .get(SectionInfo.class);
				// if (section.isGroup()) {
				Utils.executeHandler(hs, cs,
						Strings.model("command.id.ShowSection"));
				// App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
				// new EVENT_UPDATE_VIEW_DATA(con, section, section));
				// } else {
				// SectionInfo selected = (SectionInfo) con.srv().get(
				// section.getParent());
				//
				// selected.tag = section.getId().toString();
				//
				// window.getContext().set(SectionInfo.class, selected);
				//
				// Utils.executeHandler(hs, cs,
				// Strings.get("command.id.ShowSection"));
				//
				// App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
				// new EVENT_UPDATE_VIEW_DATA(con, selected, section));
				// }
			}
		});

		showSections(window, hs, cs);

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.model("model.id.contentview.popup"));

	}

	private void panelVisible() {
		ListBookInfoOptions opt = (ListBookInfoOptions) con.getTreeItem()
				.getOptions();

		if (opt == null)
			return;

		if (!opt.ACL && !opt.Context) {
			App.br.post(Events.EVENT_HIDE_BOOK_PANEL, null);
			return;
		}

		if (!opt.ACL)
			App.br.post(Events.EVENT_HIDE_BOOK_ROLES, null);

		if (!opt.Context)
			App.br.post(Events.EVENT_HIDE_BOOK_CONTEXT, null);

	}

	@Inject
	@Optional
	public void EVENT_HIDE_BOOK_ROLES(
			@UIEventTopic(Events.EVENT_HIDE_BOOK_ROLES) Object o,
			EPartService partService, EModelService model,
			@Active MWindow window) {

		String partID = Strings.model("part.SectionRolesView");

		List<MPart> parts = model.findElements(window, partID, MPart.class,
				null);

		MPart part;

		if (!parts.isEmpty()) {
			part = parts.get(0);
			partService.hidePart(part);
		}
	}

	@Inject
	@Optional
	public void EVENT_HIDE_BOOK_CONTEXT(
			@UIEventTopic(Events.EVENT_HIDE_BOOK_CONTEXT) Object o,
			EPartService partService, EModelService model,
			@Active MWindow window) {

		String partID = Strings.model("part.SectionContextView");

		List<MPart> parts = model.findElements(window, partID, MPart.class,
				null);

		MPart part;

		if (!parts.isEmpty()) {
			part = parts.get(0);
			partService.hidePart(part);
		}
	}

	@Inject
	@Optional
	public void EVENT_HIDE_BOOK_PANEL(
			@UIEventTopic(Events.EVENT_HIDE_BOOK_PANEL) Object o,
			EPartService partService, EModelService model,
			@Active MWindow window) {

		String partID = Strings.model("part.SectionPanel");

		List<MPartStack> parts = model.findElements(window, partID,
				MPartStack.class, null);

		if (!parts.isEmpty()) {
			MPartStack part = parts.get(0);
			part.setVisible(false);
			// partService.hidePart(part);
		}
	}

	private void showSections(MWindow window, EHandlerService hs,
			ECommandService cs) {

		if (!App.getJetty().isStarted())
			return;

		BookOptions opt = con.srv().getRootOptions(BookOptions.class);
		for (Integer i : opt.openSections) {

			final SectionInfo section = (SectionInfo) con.srv().get(i);
			if (section == null)
				continue;

			window.getContext().set(SectionInfo.class, section);

			Utils.executeHandler(hs, cs,
					Strings.model("command.id.ShowSection"));
		}

		// if (opt.openSections == null || opt.openSections.isEmpty()) {
		//
		// List<ITreeItemInfo> input = con.srv().getRoot();
		// if (input.isEmpty()) {
		// return;
		// }
		// int section_id = input.get(0).getId();
		//
		// final SectionInfo section = (SectionInfo) con.srv().get(section_id);
		// if (section == null)
		// return;
		//
		// window.getContext().set(SectionInfo.class, section);
		//
		// Utils.executeHandler(hs, cs,
		// Strings.model("command.id.ShowSection"));
		//
		// }

	}

}