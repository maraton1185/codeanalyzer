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
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
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
import ebook.module.book.BookOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoSelection;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.TreeViewComponent;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class SectionsView {

	private TreeViewer viewer;

	@Inject
	@Active
	BookConnection book;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_EDIT_TITLE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (book != data.book)
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_LABELS_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_LABELS_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (book != data.book)
			return;

		if (data.parent == null)
			return;

		viewer.update(data.parent, null);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs,
			EModelService model, @Active MWindow bookWindow) {

		if (book != data.book)
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

		rolesVisible();

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				book.srv(), 3, true);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

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

				App.br.post(Events.EVENT_UPDATE_SECTION_INFO, null);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				SectionInfo section = window.getContext()
						.get(SectionInfo.class);
				if (section.isGroup()) {
					Utils.executeHandler(hs, cs,
							Strings.get("command.id.ShowSection"));
					App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
							new EVENT_UPDATE_VIEW_DATA(book, section, section));
				} else {
					SectionInfo selected = (SectionInfo) book.srv().get(
							section.getParent());

					selected.tag = section.getId().toString();

					window.getContext().set(SectionInfo.class, selected);

					Utils.executeHandler(hs, cs,
							Strings.get("command.id.ShowSection"));

					App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
							new EVENT_UPDATE_VIEW_DATA(book, selected, section));
				}
			}
		});

		showSections(window, hs, cs);

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.contentview.popup"));

	}

	private void rolesVisible() {
		ListBookInfoOptions opt = (ListBookInfoOptions) book.getTreeItem()
				.getOptions();

		if (opt == null)
			return;

		if (opt.ACL)
			return;

		App.br.post(Events.EVENT_HIDE_BOOK_ROLES, null);

	}

	private void showSections(MWindow window, EHandlerService hs,
			ECommandService cs) {

		if (!App.getJetty().isStarted())
			return;

		BookOptions opt = (BookOptions) book.srv().getRootOptions();
		for (Integer i : opt.openSections) {

			final SectionInfo section = (SectionInfo) book.srv().get(i);
			if (section == null)
				continue;

			window.getContext().set(SectionInfo.class, section);

			Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		}

		if (opt.openSections == null || opt.openSections.isEmpty()) {

			List<ITreeItemInfo> input = book.srv().getRoot();
			if (input.isEmpty()) {
				return;
			}
			int section_id = input.get(0).getId();

			final SectionInfo section = (SectionInfo) book.srv()
					.get(section_id);
			if (section == null)
				return;

			window.getContext().set(SectionInfo.class, section);

			Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));

		}

	}

}