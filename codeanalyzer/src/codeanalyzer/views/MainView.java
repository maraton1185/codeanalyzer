 
package codeanalyzer.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import codeanalyzer.utils.Strings;

public class MainView {
	
//	@Inject
//	EModelService modelService;
//	@Inject
//	MApplication application;
	
	@Inject
	public MainView(EModelService modelService, MApplication application) {
		
		MUILabel element = (MUILabel)modelService.find(Strings.get("model_id_activate"), application);
		
		element.setLabel("label");
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("New Label");

	}
	
	
	
	
}