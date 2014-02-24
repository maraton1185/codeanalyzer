 
package codeanalyzer.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class MainView {
	
	TableViewer objectsTree;
	
	@Inject	IEventBroker br;
	@Inject UISynchronize sync;
	@Inject EModelService modelService;
	@Inject MApplication application;
//	Thread updateStatusThread;
	
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		
		br.send(Const.EVENT_UPDATE_STATUS, null);

		objectsTree = new TableViewer(parent);
		menuService.registerContextMenu(objectsTree.getTable(), Strings.get("objectsTree_popupmenu"));

	}

//	@PreDestroy
//	public void preDestroy() {
//
//	}
	
	@Inject @Optional
	public void  showStatus(@UIEventTopic(Const.EVENT_UPDATE_STATUS) Object o) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String status = pico.get(IAuthorize.class).getInfo().ShortMessage();
				sync.asyncExec(new Runnable() {					
					@Override
					public void run() {
						MHandledToolItem element = (MHandledToolItem)modelService.find(Strings.get("model_id_activate"), application);
						element.setLabel(status);
						element.setVisible(true);
					}
				});		
			}
		}).start();
	}
		
	
	
	
}
