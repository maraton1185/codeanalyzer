package ebook.module.book;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.book.servlets.BookServletModel;
import ebook.module.book.servlets.BookServletModel.Parent;
import ebook.module.book.servlets.BookServletModel.Section;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.tree.ITreeItemInfo;

public class BookConnection extends BaseDbPathConnection {

	ITreeItemInfo treeItem;

	public BookConnection(IPath path, boolean check)
			throws InvocationTargetException {

		super(path, new BookStructure(), check);

	}

	public BookConnection(String name) throws InvocationTargetException {

		super(name, new BookStructure());

	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bs(this) : service;

		return service;
	}

	public ITreeItemInfo getTreeItem() {

		if (treeItem == null)
			return App.srv.bls().getBookTreeItem(getConnectionPath());
		else
			return treeItem;
	}

	// *****************************************************************

	public BookServletModel getModel(String section_id) {

		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ITreeItemInfo sec = srv().get(id);
		if (sec == null)
			return null;

		BookServletModel model = new BookServletModel();

		ITreeItemInfo bookItem = getTreeItem();
		if (bookItem == null)
			return null;

		model.host = App.getJetty().book(bookItem.getId());
		model.title = bookItem.getTitle();

		model.section = model.new Section();

		model.section.id = sec.getId();
		model.section.title = sec.getTitle();
		model.section.group = sec.isGroup();
		model.section.text = srv().getText(id);
		model.section.url = getUrl(model.host, model.section.id);

		model.sections = new ArrayList<Section>();

		List<ITreeItemInfo> list = srv().getChildren(id);

		for (ITreeItemInfo item : list) {

			Section section = model.new Section();

			section.id = item.getId();
			section.title = item.getTitle();
			section.group = item.isGroup();
			section.text = srv().getText(item.getId());
			section.images = srv().getImages(item.getId());
			Integer bigImageCSS = ((SectionInfoOptions) item.getOptions())
					.getBigImageCSS();
			section.bigImageCSS = bigImageCSS;
			section.textCSS = SectionInfoOptions.gridLength - bigImageCSS;

			section.url = getUrl(model.host, section.id);

			model.sections.add(section);
		}

		model.parents = new ArrayList<Parent>();
		ITreeItemInfo parent = srv().get(sec.getParent());
		while (parent != null) {

			Parent item = model.new Parent();
			item.title = parent.getTitle();
			item.url = getUrl(model.host, parent.getId());

			model.parents.add(0, item);

			ITreeItemInfo current = parent;
			parent = srv().get(current.getParent());
		}

		return model;
	}

	private String getUrl(String host, Integer id) {
		return host + "&id=" + id;
	}
}
