package ebook.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ebook.web.gwt.client.ContextTreeItem;
import ebook.web.gwt.client.TreeService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TreeServiceImpl extends RemoteServiceServlet implements
		TreeService {

	@Override
	public List<ContextTreeItem> getChild(ContextTreeItem node)
			throws IllegalArgumentException {
		// Verify that the input is valid.
		// if (!input.isEmpty()) {
		// // If the input is not valid, throw an IllegalArgumentException back
		// // to
		// // the client.
		// throw new IllegalArgumentException("empty input");
		// }

		List<ContextTreeItem> result = new ArrayList<ContextTreeItem>();
		ContextTreeItem item = new ContextTreeItem();
		if (node.getId() == null)
			item.setTitle("Контекст");

		item.setId(0);
		result.add(item);
		// String serverInfo = getServletContext().getServerInfo();
		// String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		//
		// // Escape data from the client to avoid cross-site script
		// // vulnerabilities.
		// input = escapeHtml(input);
		// userAgent = escapeHtml(userAgent);

		return result;
	}

	@Override
	public String getText(ContextTreeItem item) throws IllegalArgumentException {
		return "got it";
	}

	// @Override
	// public Boolean isGroup(String msg) throws IllegalArgumentException {
	// return true;
	// }

	// /**
	// * Escape an html string. Escaping data received from the client helps to
	// * prevent cross-site script vulnerabilities.
	// *
	// * @param html
	// * the html string to escape
	// * @return the escaped string
	// */
	// private String escapeHtml(String html) {
	// if (html == null) {
	// return null;
	// }
	// return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
	// .replaceAll(">", "&gt;");
	// }
}
