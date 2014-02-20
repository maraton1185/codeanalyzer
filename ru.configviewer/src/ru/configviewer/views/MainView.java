package ru.configviewer.views;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import ru.configviewer.Application;
import ru.configviewer.core.IEditorFactory;
import ru.configviewer.core.IService;
import ru.configviewer.core.LineInfo;
import ru.configviewer.core.pico;
import ru.configviewer.utils.Const;
import ru.configviewer.views.core.OutlineContentProvider;

public class MainView extends ViewPart {

	public static final String ID = "ru.configviewer.views.main"; //$NON-NLS-1$
	private Text connection;
	private TreeViewer outline;
	private Text textField;
	private TreeFilter filter = new TreeFilter();
	
	
	private static class OutlineLabelProvider extends StyledCellLabelProvider {
		
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
//			final IColorManager color = pico.get(IColorManager.class);
//			cell.setImage(ResourceManager.getPluginImage("ru.codeAnalyzer", "icons/markers/codeanalyzer-proc.png"));
			
			if (element instanceof LineInfo) {
				final LineInfo info = (LineInfo) element;
				text.append(info.title);
			}
			
			cell.setText(text.toString());
		    cell.setStyleRanges(text.getStyleRanges());
		    super.update(cell);
		}
	}

	public MainView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		Button button;
		
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		connection = new Text(composite, SWT.BORDER);
		FormData fd_connection = new FormData();
		fd_connection.left = new FormAttachment(0);
		fd_connection.bottom = new FormAttachment(0, 19);
		fd_connection.top = new FormAttachment(0);
		connection.setLayoutData(fd_connection);
		
		button = new Button(composite, SWT.FLAT);
		fd_connection.right = new FormAttachment(button, -1);
		FormData fd_button = new FormData();
		fd_button.bottom = new FormAttachment(0, 19);
		fd_button.top = new FormAttachment(0);
		fd_button.right = new FormAttachment(100);
		button.setLayoutData(fd_button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				connect();			
			}
		});
		
		button.setText("Соединить");
		
		textField = new Text(composite, SWT.BORDER);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.character=='\r')
					doSearch();
			}
		});
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(0);
		fd_text.right = new FormAttachment(100, -69);
		fd_text.top = new FormAttachment(connection, 2);
		textField.setLayoutData(fd_text);
		
		Button btnNewButton = new Button(composite, SWT.FLAT);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearFilter();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(textField, 19);
		fd_btnNewButton.top = new FormAttachment(textField, 0, SWT.TOP);
		fd_btnNewButton.left = new FormAttachment(button, -68);
		fd_btnNewButton.right = new FormAttachment(button, 0, SWT.RIGHT);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Очистить");
		
		
		outline = new TreeViewer(sashForm, SWT.BORDER);
		Tree tree = outline.getTree();
		sashForm.setWeights(new int[] {42, 424});
		
		outline.setLabelProvider(new OutlineLabelProvider());
		outline.setContentProvider(new OutlineContentProvider(this));

		createActions();
		initializeToolBar();
		initializeMenu();
		
		initContents();
		
		hookDoubleClickAction();
	}

	protected void clearFilter() {
		outline.removeFilter(filter);
		
	}

	protected void doSearch() {
		String text = textField.getText();
		filter.setText(text);
		if (text.isEmpty())
			outline.removeFilter(filter);
		else
			outline.addFilter(filter);
		
	}

	private void hookDoubleClickAction() {
		outline.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				outlineDblClick();				
			}
		});		
	}

	protected void outlineDblClick() {
		StructuredSelection selection = (StructuredSelection)outline.getSelection();
		if (selection.getFirstElement() instanceof LineInfo) {
			LineInfo line = (LineInfo)selection.getFirstElement();
			pico.get(IEditorFactory.class).open(line);
		}
		
	}

	private void initContents() {
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Application.PLUGIN_ID);
		connection.setText(preferences.get(Const.CONNECTION, "atlant\\projector"));
		
	}

	private void setValues() {
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Application.PLUGIN_ID);
		preferences.put(Const.CONNECTION, connection.getText());
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	protected void connect() {
		setValues();
		List<LineInfo> list = pico.get(IService.class).getLines(connection.getText());
		outline.setInput(list);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
