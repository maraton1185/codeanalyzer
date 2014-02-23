 
package codeanalyzer.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.utils.Strings;

public class MainView {
	
	private static final String UPDATE ="update";
	
	TableViewer objectsTree;
	
	@Inject	IEventBroker br;
	@Inject UISynchronize sync;
	@Inject EModelService modelService;
	@Inject MApplication application;
	
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
	
		
//		showStatus();
		
		MUILabel element = (MUILabel)modelService.find(Strings.get("model_id_activate"), application);
		element.setLabel(pico.get(IAuthorize.class).getInfo().ShortMessage());
		
		objectsTree = new TableViewer(parent);
		
//		Label lblNewLabel = new Label(parent, SWT.NONE);
//		lblNewLabel.setText("New Label");
		
		menuService.registerContextMenu(objectsTree.getTable(), Strings.get("objectsTree_popupmenu"));

	}

	@Inject @Optional
	public void  getEvent(@UIEventTopic(UPDATE) String status) {
		MUILabel element = (MUILabel)modelService.find(Strings.get("model_id_activate"), application);
		element.setLabel(status);
	}
	
	private void showStatus() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				String status = pico.get(IAuthorize.class).getInfo().ShortMessage();
				
				br.send(UPDATE, status);				
			}
		}).start(); 	

	}
	
	
	
	
}
