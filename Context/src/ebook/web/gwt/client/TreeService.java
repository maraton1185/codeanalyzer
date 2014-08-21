package ebook.web.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("tree")
public interface TreeService extends RemoteService {
	List<ContextTreeItem> getChild(ContextTreeItem node)
			throws IllegalArgumentException;

	String getText(ContextTreeItem item) throws IllegalArgumentException;
}
