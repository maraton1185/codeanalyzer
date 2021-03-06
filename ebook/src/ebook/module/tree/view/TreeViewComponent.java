package ebook.module.tree.view;

import java.util.Collection;
import java.util.List;

import org.eclipse.e4.ui.workbench.swt.internal.copy.FilteredTree;
import org.eclipse.e4.ui.workbench.swt.internal.copy.PatternFilter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import ebook.module.tree.EbookProposalTextCellEditor;
import ebook.module.tree.EbookTextCellEditor;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.service.ITreeService2;

public class TreeViewComponent {

	private TreeViewer viewer;
	private Tree viewerTree;
	private Composite parent;
	ITreeService2 service;
	private ITreeItemInfo root;
	private ITreeItemInfo dragSection;
	protected TreeItem selectedItem;
	private boolean groupIsBold;
	private int expandLevel;
	private IContentProposalProvider contentProposalProvider;

	public TreeViewComponent(Composite parent, ITreeService2 service,
			int expandLevel, boolean editingSupport) {

		this(parent, service, expandLevel, editingSupport, true, null);

	}

	public TreeViewComponent(Composite parent, ITreeService2 service,
			int expandLevel, boolean editingSupport, boolean groupIsBold) {

		this(parent, service, expandLevel, editingSupport, groupIsBold, null);

	}

	public TreeViewComponent(Composite parent, ITreeService2 service,
			int expandLevel, boolean editingSupport, boolean groupIsBold,
			IContentProposalProvider contentProposalProvider) {

		this.service = service;
		this.parent = parent;
		this.groupIsBold = groupIsBold;
		this.expandLevel = expandLevel;
		this.contentProposalProvider = contentProposalProvider;

		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION, filter, true);
		// tree.setQuickSelectionMode(true);
		tree.setInitialText("����� ��� ������");
		viewer = tree.getViewer();
		// viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL |
		// SWT.V_SCROLL
		// | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		viewerTree = (Tree) viewer.getControl();
		viewerTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedItem = (TreeItem) e.item;
			}
		});

		updateInput();
		// List<ITreeItemInfo> input = service.getRoot();
		// root = input.size() == 0 ? null : input.get(0);
		// viewer.setAutoExpandLevel(expandLevel);
		// viewer.setInput(input);

		if (editingSupport) {
			editingSupport();

			dragAndDropSupport();
		}
	}

	// public void setInput() {
	// List<ITreeItemInfo> input = service.getRoot();
	// root = input.size() == 0 ? null : input.get(0);
	// // viewer.setAutoExpandLevel(expandLevel);
	// viewer.setInput(input);
	// }

	public void setLabelProvider(StyledCellLabelProvider prov) {
		viewer.setLabelProvider(prov);
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
			return ((Collection<ITreeItemInfo>) inputElement).toArray();
			// return ((Collection<BookInfo>) inputElement).toArray();
			// return (BookSection[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// return dbManager.getChildren(((BookInfo) parentElement).id)
			// .toArray();
			return service.getChildren(((ITreeItemInfo) parentElement).getId())
					.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return service.get(((ITreeItemInfo) element).getParent());
		}

		@Override
		public boolean hasChildren(Object element) {
			return service.hasChildren(((ITreeItemInfo) element).getId());
		}
	}

	class TreeLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			String name = (String) element;
			return name;
		}
	}

	class ViewLabelProvider extends StyledCellLabelProvider implements
			ILabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			ITreeItemInfo item = (ITreeItemInfo) element;

			if (item.getTitle() != null) {
				if (item.isGroup() && groupIsBold || !groupIsBold
						&& !item.isGroup()) {

					FontData fontDatas[] = parent.getFont().getFontData();
					FontData data = fontDatas[0];
					int height = data.getHeight();
					height = (int) (height - 0.2 * height);
					final Font font = new Font(Display.getCurrent(),
							data.getName(), height, SWT.BOLD);

					text.append(item.getTitle(), new Styler() {
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.font = font;
						}
					});

				} else
					text.append(item.getTitle());
				;

				// else {
				if (!item.getSuffix().isEmpty()) {

					text.append(" : " + item.getSuffix(), new Styler() {
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.foreground = Display.getCurrent()
									.getSystemColor(SWT.COLOR_DARK_GRAY);
						}
					});
				}
				// text.append(" " + section.id);
				// }

			}

			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			cell.setImage(item.getListImage());
			super.update(cell);

		}

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			ITreeItemInfo item = (ITreeItemInfo) element;
			return item.getTitle();
		}
	}

	private void editingSupport() {
		viewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				return ((ITreeItemInfo) element).getTitle() + "";
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TreeItem item = (TreeItem) element;
				ITreeItemInfo object = (ITreeItemInfo) item.getData();
				object.setTitle(value.toString());
				service.saveTitle(object);
				viewer.update(item.getData(), null);
			}

		});
		viewer.setColumnProperties(new String[] { "column1" });
		if (contentProposalProvider != null)
			viewer.setCellEditors(new CellEditor[] { new EbookProposalTextCellEditor(
					contentProposalProvider, viewer.getTree()) });
		else
			viewer.setCellEditors(new CellEditor[] { new EbookTextCellEditor(
					viewer.getTree()) });

		// TreeViewerFocusCellManager focusCellManager = new
		// TreeViewerFocusCellManager(
		// viewer, new FocusCellOwnerDrawHighlighter(viewer));
		// TreeViewerFocusCellManager focusCellManager;
		// if (editOnEnter)
		// focusCellManager = new TreeViewerFocusCellManager(viewer,
		// new FocusCellOwnerDrawHighlighter(viewer));
		// else
		// focusCellManager = null;

		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						// || event.eventType ==
						// ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.SPACE)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TreeViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

	}

	private void dragAndDropSupport() {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE;

		viewer.addDropSupport(operations, types, new ViewerDropAdapter(viewer) {

			ITreeItemInfo target;
			int location = 0;

			@Override
			public boolean performDrop(Object data) {

				if (dragSection == null || (target == null))
					return false;

				if (target == dragSection) {
					return false;
				}

				if (target == root)
					location = 0;

				TreePath[] init_path = ((TreeSelection) viewer.getSelection())
						.getPathsFor(dragSection);

				viewer.setSelection(new StructuredSelection(target));

				TreePath[] paths = ((TreeSelection) viewer.getSelection())
						.getPathsFor(target);

				if (paths.length != 0 && init_path.length != 0
						&& paths[0].startsWith(init_path[0], null))
					return false;

				Boolean result = false;

				switch (location) {
				case 1:
					// "Dropped before the target ";
					result = service.setBefore(dragSection, target);
					break;
				case 2:
					// "Dropped after the target ";
					result = service.setAfter(dragSection, target);
					break;
				// case 3:
				// // "Dropped on the target ";
				// book.sections().setParent(section, target);
				//
				// break;
				// case 4:
				// Dropped into nothing
				default:
					result = service.setParent(dragSection, target);
					break;
				}

				if (result)
					selectedItem.dispose();

				return false;
			}

			@Override
			public boolean validateDrop(Object target, int operation,
					TransferData transferType) {
				if (target == null)
					return true;

				if (dragSection == target)
					return false;

				return true;// target != null;
			}

			@Override
			public void drop(DropTargetEvent event) {

				location = this.determineLocation(event);
				target = (ITreeItemInfo) determineTarget(event);
				target = target == null ? root : target;

				super.drop(event);
			}
		});

		viewer.addDragSupport(operations, types, new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				dragSection = (ITreeItemInfo) selection.getFirstElement();

				event.doit = dragSection != root;

				// TreeSelection

			}

			@Override
			public void dragSetData(DragSourceEvent event) {

				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = dragSection.getId().toString();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// System.out.println("Finshed Drag");

			}
		});
	}

	public TreeViewer getViewer() {

		return viewer;
	}

	public void setSelection() {
		ITreeItemInfo selected = service.getSelected();
		if (selected != null)
			viewer.setSelection(new StructuredSelection(selected), true);

	}

	public void updateInput() {
		List<ITreeItemInfo> input = service.getRoot();
		root = input.size() == 0 ? null : input.get(0);
		viewer.setAutoExpandLevel(expandLevel);
		viewer.setInput(input);

	}
}
