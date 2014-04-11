package codeanalyzer.views.books;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;
import codeanalyzer.utils.Strings;

public class ContentView {

	private TreeViewer viewer;

	// private final Image CELL = Utils.getImage("active.png");

	@Inject
	@Active
	BookInfo book;

	private BookSection root;

	private BookSection dragSection;
	protected TreeItem selectedItem;

	// protected TreeItem selectedItem;

	@Inject
	@Optional
	public void EVENT_EDIT_TITLE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_EDIT_TITLE_CONTENT_VIEW) EVENT_UPDATE_CONTENT_VIEW_DATA data) {

		if (book != data.book)
			return;

		if (data.parent == null)
			return;

		viewer.editElement(data.parent, 0);

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_CONTENT_VIEW_DATA data) {

		if (book != data.book)
			return;

		if (data.parent != null)
			viewer.refresh(data.parent);

		viewer.setSelection(new StructuredSelection(data.selected));
		// viewer.setExpandedState(section, true);
	}

	@PreDestroy
	public void preDestroy(@Active BookSection section) {
		if (section != null)
			book.sections().saveSelectedSelection(section);
		book.closeConnection();
	}

	@PostConstruct
	public void postConstruct(Composite parent,
			@Active final IEclipseContext ctx, EMenuService menuService) {
		// this.book = book;
		// this.bm = bm;
		// book = (BookInfo) w.getTransientData().get(Const.WINDOW_CONTEXT);

		try {
			book.sections().setBook(book);

		} catch (IllegalAccessException e) {

			e.printStackTrace();
			FormToolkit toolkit = new FormToolkit(parent.getDisplay());
			ScrolledForm form = toolkit.createScrolledForm(parent);
			form.setText(Strings.get("error.openBook"));
			return;
		}

		Composite treeComposite = new Composite(parent, SWT.NONE);
		TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeColumnLayout);

		viewer = new TreeViewer(treeComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		List<BookSection> input = book.sections().getRoot();
		root = input.size() == 0 ? null : input.get(0);
		viewer.setInput(input);

		// viewer is a JFace Viewer
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				// ss.setSelection(selection.getFirstElement());
				ctx.set(BookSection.class,
						(BookSection) selection.getFirstElement());
			}
		});

		Tree tree = (Tree) viewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedItem = (TreeItem) e.item;
			}
		});

		menuService.registerContextMenu(viewer.getControl(),
				Strings.get("model.id.contentview.popup"));

		editingSupport();

		dragAndDropSupport();
	}

	private void dragAndDropSupport() {

		// Transfer[] types = new Transfer[] { LocalSelectionTransfer
		// .getTransfer() };
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE;

		viewer.addDragSupport(operations, types, new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				dragSection = (BookSection) selection.getFirstElement();

				event.doit = dragSection != root;

				// TreeSelection

			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// IStructuredSelection selection = (IStructuredSelection)
				// viewer
				// .getSelection();
				//
				// if (LocalSelectionTransfer.getTransfer().isSupportedType(
				// event.dataType)) {
				// LocalSelectionTransfer.getTransfer()
				// .setSelection(selection);
				// }

				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = dragSection.id.toString();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// System.out.println("Finshed Drag");

			}
		});

		viewer.addDropSupport(operations, types, new ViewerDropAdapter(viewer) {

			BookSection target;
			int location = 0;

			@Override
			public boolean validateDrop(Object target, int operation,
					TransferData transferType) {
				if (target == null)
					return true;

				if (dragSection == target)
					return false;

				// setSelection(new TreeSelection((TreePath) target));
				// TreeItem[] items = tree.getSelection();

				// System.out.println(((BookSection) target).title);
				return true;// target != null;
			}

			// @Override
			// public void dragOver(DropTargetEvent event) {
			// // event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
			// }

			@Override
			public void drop(DropTargetEvent event) {

				// if (LocalSelectionTransfer.getTransfer().isSupportedType(
				// event.currentDataType)) {
				// IStructuredSelection selection = (IStructuredSelection)
				// LocalSelectionTransfer
				// .getTransfer().getSelection();
				//
				// section = (BookSection) selection.getFirstElement();
				// // System.out.println(firstElement.title);
				// }

				location = this.determineLocation(event);
				target = (BookSection) determineTarget(event);
				target = target == null ? root : target;

				// TreeItem targetItem = (TreeItem) event.item;

				// TreeSelection
				// TreePath
				// viewer.reveal(elementOrTreePath);
				// viewer.getTree(). getSelection()[0].dispose();
				//
				// TreeItem parent =
				//
				// new TreeItem((TreeItem) event.item, SWT.NONE, )
				// setSelection(new TreeSelection((TreePath) element));
				super.drop(event);
			}

			@Override
			public boolean performDrop(Object data) {

				if (dragSection == null || (target == null))
					return false;

				if (target == dragSection) {
					return false;
				}

				if (target == root)
					location = 0;

				TreePath[] inti_path = ((TreeSelection) viewer.getSelection())
						.getPathsFor(dragSection);

				viewer.setSelection(new StructuredSelection(target));

				TreePath[] paths = ((TreeSelection) viewer.getSelection())
						.getPathsFor(target);

				if (paths[0].startsWith(inti_path[0], null))
					return false;

				// selectedItem.dispose();
				// new TreeItem((TreeItem) event.item, SWT.NONE)
				// new TreeSelection((TreePath) section );
				// viewer.getTree().setSelection();

				switch (location) {
				case 1:
					// "Dropped before the target ";
					book.sections().setBefore(dragSection, target);
					break;
				case 2:
					// "Dropped after the target ";
					book.sections().setAfter(dragSection, target);
					break;
				// case 3:
				// // "Dropped on the target ";
				// book.sections().setParent(section, target);
				//
				// break;
				// case 4:
				// Dropped into nothing
				default:
					book.sections().setParent(dragSection, target);
					break;
				}

				System.out.println(dragSection.title + " to " + target.title);

				return false;
			}
		});

	}

	private void editingSupport() {
		viewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				return ((BookSection) element).title + "";
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TreeItem item = (TreeItem) element;
				BookSection section = (BookSection) item.getData();
				section.title = value.toString();
				book.sections().saveSection(section);
				viewer.update(item.getData(), null);
			}

		});
		viewer.setColumnProperties(new String[] { "column1" });
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(viewer
				.getTree()) });

		// TreeViewerFocusCellManager focusCellManager = new
		// TreeViewerFocusCellManager(
		// viewer, new FocusCellOwnerDrawHighlighter(viewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						// || event.eventType ==
						// ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TreeViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

	}

	class ViewContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((Collection<BookSection>) inputElement).toArray();
			// return (BookSection[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return book.sections().getChildren((BookSection) parentElement)
					.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return book.sections().getParent((BookSection) element);
		}

		@Override
		public boolean hasChildren(Object element) {
			return book.sections().hasChildren((BookSection) element);
		}
	}

	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			BookSection section = (BookSection) element;
			if (section.title != null)
				text.append(section.title);
			// cell.setImage(CELL);
			// if (file.isDirectory()) {
			// text.append(getFileName(file));
			// cell.setImage(image);
			// String[] files = file.list();
			// if (files != null) {
			// text.append(" (" + files.length + ") ",
			// StyledString.COUNTER_STYLER);
			// }
			// } else {
			// text.append(getFileName(file));
			// }
			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);

		}
	}

}