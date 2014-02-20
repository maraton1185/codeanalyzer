 
package codesnippetapp.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import codesnippetapp.CodeSnippetAppConstants;

public class DeleteSnippetMenuHandler {
	@Execute
	public void execute(IEventBroker eventBroker) {
		eventBroker.send(CodeSnippetAppConstants.DELETE_SNIPPET_EVENT, null);
	}
		
}