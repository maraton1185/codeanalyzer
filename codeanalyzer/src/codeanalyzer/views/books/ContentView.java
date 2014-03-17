package codeanalyzer.views.books;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Utils;

public class ContentView {

	private TreeViewer viewer;

	private final Image CELL = Utils.getImage("active.png");

	@Inject
	IBookManager bm;

	@Inject
	@Active
	BookInfo book;

	@PostConstruct
	public void postConstruct(Composite parent) {
		// this.book = book;
		// this.bm = bm;
		// book = (BookInfo) w.getTransientData().get(Const.WINDOW_CONTEXT);

		Composite treeComposite = new Composite(parent, SWT.NONE);
		TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
		treeComposite.setLayout(treeColumnLayout);

		viewer = new TreeViewer(treeComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(bm.getSections(book));

		// viewer is a JFace Viewer
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				AppManager.ss.setSelection(selection.getFirstElement());
			}
		});
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
			return bm.getChildren(book, (BookSection) parentElement).toArray();
		}

		@Override
		public Object getParent(Object element) {
			return bm.getParent(book, (BookSection) element);
		}

		@Override
		public boolean hasChildren(Object element) {
			return bm.hasChildren(book, (BookSection) element);
		}

	}

	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			BookSection section = (BookSection) element;
			text.append(section.title);
			cell.setImage(CELL);
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