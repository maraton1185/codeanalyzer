package ebook.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.service.ContextService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.web.gwt.client.ContextTreeItem;
import ebook.web.gwt.client.TreeService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TreeServiceImpl extends RemoteServiceServlet implements
		TreeService {

	@Override
	public List<ContextTreeItem> getChild(String book_id, String section_id,
			ContextTreeItem node) throws IllegalArgumentException {

		ContextService srv = checkArguments(book_id, section_id);

		List<ITreeItemInfo> list;
		if (node.getId() == null)
			list = srv.getRoot();
		else
			list = srv.getChildren(node.getId());

		List<ContextTreeItem> result = new ArrayList<ContextTreeItem>();

		for (ITreeItemInfo item : list) {

			ContextTreeItem treeItem = new ContextTreeItem();
			treeItem.setId(item.getId());
			treeItem.setTitle(item.getTitle());
			treeItem.setLeaf(!srv.hasChildren(item.getId()));
			result.add(treeItem);
		}
		return result;
	}

	private ContextService checkArguments(String book_id, String section_id)
			throws IllegalArgumentException {

		if (book_id == null) {
			throw new IllegalArgumentException("Не задана книга.");
		}

		if (section_id == null) {
			throw new IllegalArgumentException("Не задана секция книги.");
		}

		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Не задана секция книги.");
		}

		BookConnection book = App.srv.bl().getBook(book_id);
		if (book == null) {
			throw new IllegalArgumentException("Книга не найдена.");
		}
		SectionInfo section = (SectionInfo) book.srv().get(id);
		if (section == null)
			throw new IllegalArgumentException("Не найдена секция книги.");

		return book.ctxsrv(section);

	}

	@Override
	public String getText(String book_id, String section_id,
			ContextTreeItem node) throws IllegalArgumentException {

		ContextService srv = checkArguments(book_id, section_id);

		String text = "";

		ContextInfo item = (ContextInfo) srv.get(node.getId());

		ContextInfoOptions opt = item.getOptions();
		if (opt.type == BuildType.module) {
			List<ITreeItemInfo> list = srv.getChildren(item.getId());

			StringBuilder result = new StringBuilder();

			for (ITreeItemInfo info : list) {

				String _text = srv.getText(info.getId());

				result.append(_text);
			}
			text = result.toString();
		} else

			text = srv.getText(node.getId());

		if (!text.isEmpty())
			return getHtml(text);
		else
			return "";
	}

	private String getHtml(String html) {
		if (html == null) {
			return null;
		}
		html = html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");

		return "<pre><code class=\"1c\">" + html
		// + html.replaceAll("\n", "<br>").replaceAll("\t",
		// "<span style=\"padding: 0px 10px;\">&nbsp;</span>")
				+ "</code><pre>";
	}
}
