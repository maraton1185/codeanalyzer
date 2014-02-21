 
package codeanalyzer.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import codeanalyzer.auth.SignIn;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.utils.Strings;

public class MainView {
	
	TableViewer objectsTree;
	
	@Inject
	IEventBroker br;
	
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService, EModelService modelService, MApplication application) {
		
		MUILabel element = (MUILabel)modelService.find(Strings.get("model_id_activate"), application);
		element.setLabel(pico.get(IAuthorize.class).getInfo().ShortMessage());
		
		objectsTree = new TableViewer(parent);
		
//		Label lblNewLabel = new Label(parent, SWT.NONE);
//		lblNewLabel.setText("New Label");
		
		menuService.registerContextMenu(objectsTree.getTable(), Strings.get("objectsTree_popupmenu"));

	}
	
	
	
	
}