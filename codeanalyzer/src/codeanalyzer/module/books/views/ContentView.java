package codeanalyzer.module.books.views;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
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

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.BookOptions;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.module.books.tree.SectionInfoSelection;
import codeanalyzer.module.booksList.tree.ListBookInfo;
import codeanalyzer.module.tree.TreeViewComponent;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class ContentView {

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
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs,
			EModelService model, @Active MWindow bookWindow) {

		if (data.onlySectionView)
			return;

		if (book != data.book)
			return;

		if (data.parent != null)
			viewer.refresh(data.parent);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);
		// if (data.setBook == true) {
		// Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		// }

		// if (data.parent == null)
		// return;
		//
		// viewer.refresh(data.parent);
		//
		// if (data.selected != null)
		// viewer.setSelection(new StructuredSelection(data.selected), true);

	}

	@PreDestroy
	public void preDestroy(@Optional @Active SectionInfo section,
			EPartService partService, EModelService model) {
		BookOptions opt = new BookOptions();
		if (section != null)
			opt.selectedSection = section.getId();

		// List<MPartStack> stacks = model.findElements(App.app,
		// Strings.get("codeanalyzer.partstack.sections"),
		// MPartStack.class, null);

		// if (!stacks.isEmpty()) {
		//
		// opt.openSections = new ArrayList<Integer>();
		// for (MStackElement _part : stacks.get(0).getChildren()) {
		//
		// if (!(_part instanceof MPart))
		// continue;
		//
		// MPart part = (MPart) _part;
		//
		// if (!part.isVisible())
		// continue;
		// String id = part.getElementId();

		// if (id.equals(Strings
		// .get("codeanalyzer.partdescriptor.sectionView"))) {
		// SectionView view = (SectionView) part.getObject();
		// opt.openSections.add(view.getId());
		// }
		//
		// if (id.equals(Strings
		// .get("codeanalyzer.partdescriptor.sectionsBlockView"))) {
		// BlockView view = (BlockView) part.getObject();
		// opt.openSections.add(view.getId());
		// }

		// }
		//
		// }
		book.srv().saveBookOptions(opt);

		book.closeConnection();
	}

	@PostConstruct
	public void postConstruct(Composite parent, @Active final MWindow window,
			EMenuService menuService, final EHandlerService hs,
			final ECommandService cs) {

		parent.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent treeComponent = new TreeViewComponent(parent,
				book.srv(), 3);

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
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				Utils.executeHandler(hs, cs,
						Strings.get("command.id.ShowSection"));
			}
		});

		treeComponent.setSelection();

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.contentview.popup"));

	}

}