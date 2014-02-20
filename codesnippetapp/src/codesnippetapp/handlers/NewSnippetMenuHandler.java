 
package codesnippetapp.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import codesnippetapp.CodeSnippetAppConstants;

public class NewSnippetMenuHandler {
	@Execute
	public void execute(IEventBroker eventBroker) {
		eventBroker.send(CodeSnippetAppConstants.NEW_SNIPPET_EVENT, null);
	}
		
}