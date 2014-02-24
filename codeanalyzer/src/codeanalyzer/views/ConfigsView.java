 
package codeanalyzer.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class ConfigsView {
	

	IDbManager dbMng = pico.get(IDbManager.class);
	
	private final Image ACTIVE = getImage("active.png");
	private final Image NONACTIVE = getImage("nonactive.png");
//	private final Image UNCHECKED = getImage("unchecked.png");
	
	private TableViewer viewer;

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
	public void postConstruct(Composite parent, final IEclipseContext ctx) {
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
		      | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		createColumns();

		// make lines and header visible
		final Table table = viewer.getTable();
//		table.setHeaderVisible(true);
		table.setLinesVisible(true); 

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		viewer.setInput(dbMng.getList()); 
		
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
//				IStructuredSelection selection = (IStructuredSelection) viewer
//						.getSelection();
//				Object firstElement = selection.getFirstElement();
				
				// do something with it
			}
		});
	}

	private void createColumns() {

		TableViewerColumn colRole = new TableViewerColumn(viewer, SWT.NONE);
		colRole.getColumn().setWidth(30);
		colRole.getColumn().setText("");
		colRole.setLabelProvider(new ColumnLabelProvider() {
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

		TableViewerColumn colDesc = new TableViewerColumn(viewer, SWT.NONE);
		colDesc.getColumn().setWidth(150);
//		colDesc.getColumn().setText("Описание");
		colDesc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// Person p =
				return ((IDb) element).status();
			}
		});


	}
	
	// helper method to load the images
	// ensure to dispose the images in your @PreDestroy method
	private static Image getImage(String file) {

	    // assume that the current class is called View.java
	  Bundle bundle = FrameworkUtil.getBundle(ConfigsView.class);
	  URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
	  ImageDescriptor image = ImageDescriptor.createFromURL(url);
	  return image.createImage();

	} 
	
	
	
	
}