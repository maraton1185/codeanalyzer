package codeanalyzer.views.books;

import java.sql.SQLException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class ContentView {

	private TreeViewer viewer;

	private final Image CELL = Utils.getImage("active.png");

	@Inject
	IBookManager bm;

	@Inject
	@Active
	BookInfo book;

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW) Object o,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BookSection section) {
		// if (section == null)
		// viewer.setInput(bm.getSections(book));
		// else
		viewer.refresh(section);

		viewer.setExpandedState(section, true);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		// this.book = book;
		// this.bm = bm;
		// book = (BookInfo) w.getTransientData().get(Const.WINDOW_CONTEXT);

		try {
			bm.sections().setBook(book);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {

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
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(bm.sections().get());

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
			return bm.sections().getChildren((BookSection) parentElement)
					.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return bm.sections().getParent((BookSection) element);
		}

		@Override
		public boolean hasChildren(Object element) {
			return bm.sections().hasChildren((BookSection) element);
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