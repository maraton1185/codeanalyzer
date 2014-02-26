 
package codeanalyzer.views;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class ConfigsView {
	

	IDbManager dbMng = pico.get(IDbManager.class);
	
	private final Image ACTIVE = Utils.getImage("active.png");
	private final Image NONACTIVE = Utils.getImage("nonactive.png");
	private final Image LOADED = Utils.getImage("loaded_with_table.png");
	private final Image NOT_LOADED = Utils.getImage("not_loaded.png");
	
	private TableViewer viewer;

	@Inject IEclipseContext ctx;
	
	@Inject
	public ConfigsView() {
		//TODO Your code here
	}
	
	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	@Inject @Optional
	public void  updateList(@UIEventTopic(Const.EVENT_UPDATE_CONFIG_LIST) Object o) {		
		viewer.refresh();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService, final EHandlerService hService, final ECommandService comService) {
		
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		
		viewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.H_SCROLL
		      | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		
		createColumns(tableColumnLayout);
		
		
		// make lines and header visible
		final Table table = viewer.getTable();
//		table.setHeaderVisible(true);
		table.setLinesVisible(true); 
		
		
		menuService.registerContextMenu(table, Strings.get("model.id.configList.popup"));
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		viewer.setInput(dbMng.getList()); 
		
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				Object selected = selection.getFirstElement();
				if (selected != null) {
					IDb db = (IDb) selection.getFirstElement();
					ctx.set(Const.CONTEXT_SELECTED_DB, db);
				} else
					ctx.set(Const.CONTEXT_SELECTED_DB, null);				
			}
		});
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				hService.executeHandler(comService.createCommand(Strings.get("command.id.ConfigListSetActive"), Collections.EMPTY_MAP));
			}
		});
		
	}

	private void createColumns(TableColumnLayout tableColumnLayout) {

		TableViewerColumn column;
		
		column = new TableViewerColumn(viewer, SWT.NONE);
//		column.getColumn().setText("");
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null; // no string representation, we only want to
								// display the image
			}
			@Override
			public Image getImage(Object element) {
				if (dbMng.getActive()== (IDb) element) {
					return ACTIVE;
				}
				if (dbMng.getNonActive()== (IDb) element) {
					return NONACTIVE;
				}
				return null;
			}
		});

		tableColumnLayout.setColumnData(column.getColumn(), 
		      new ColumnPixelData(30));
//				new ColumnWeightData(20, 30, true)); 
		
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// Person p =
				return ((IDb) element).getName();// status();
			}
		});

		tableColumnLayout.setColumnData(column.getColumn(), 
					new ColumnWeightData(50, 150, true));
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null; // no string representation, we only want to
								// display the image
			}
			@Override
			public Image getImage(Object element) {
				IDb info = (IDb) element;
				switch (info.getState()) {
				case Loaded:
					return LOADED;
				case notLoaded:
					return NOT_LOADED;
//				case LoadedWithLinkTable:
//					return NOT_LOADED;
				default:
					return NOT_LOADED;
				}				
			}
		});

		tableColumnLayout.setColumnData(column.getColumn(), 
		      new ColumnPixelData(20));
//				new ColumnWeightData(20, 30, true));

	}
	

	
	
	
	
}