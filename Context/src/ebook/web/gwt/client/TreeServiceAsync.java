package ebook.web.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TreeServiceAsync {
	void getChild(ContextTreeItem input,
			AsyncCallback<List<ContextTreeItem>> callback)
			throws IllegalArgumentException;

	void getText(ContextTreeItem item, AsyncCallback<String> asyncCallback)
			throws IllegalArgumentException;

}
