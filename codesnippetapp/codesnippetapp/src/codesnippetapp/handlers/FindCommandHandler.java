 
package codesnippetapp.handlers;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codesnippetapp.data.SnippetData;
import codesnippetapp.views.SearchDialog;

public class FindCommandHandler {
	@Execute 
	public void execute(Shell shell, SearchDialog dlg, 
			IEclipseContext ctx,
			EPartService partService, 
			EModelService modelService, 
			MApplication application) {
//		MessageDialog.openInformation(shell, 
//		        "Find Command", "Find command executed");
//		dlg.open();
//		SearchDialog dlg = ContextInjectionFactory.make(SearchDialog.class, ctx);
		dlg.open();
		
		List<SnippetData> searchResults = dlg.getSearchResult();
		if(searchResults==null||searchResults.size()==0)
		{
			MessageDialog.openInformation(shell, "No match found", "No match found for the search criteria");
			return;
		}
		
		IEclipseContext newPartContext = ctx.createChild("search_result_context");
//		MPart findResultPart = org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory.INSTANCE.createPart();
		MPart findResultPart = partService.createPart("codesnippetapp.partdescriptor.0");
//		findResultPart.setContributionURI("bundleclass://codesnippetapp/codesnippetapp.views.SearchResultsView");
		findResultPart.setContext(newPartContext);
//		findResultPart.setCloseable(true);
//		findResultPart.setLabel("Search results");
		List<MPartStack> partStackList = modelService.findElements(application, "codesnippetapp.partstack.0", MPartStack.class, null);
		if(partStackList!=null)
		{
			MPartStack partStack = partStackList.get(0);
			partStack.setVisible(true);
			partStack.getChildren().add(findResultPart);
			partService.activate(findResultPart, true);
		}
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}