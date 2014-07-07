package ebook.module.book;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.book.servlets.BookServlet.BookServletModel;
import ebook.module.book.servlets.BookServlet.BookServletModel.Section;
import ebook.module.tree.ITreeItemInfo;

public class BookConnection extends BaseDbPathConnection {

	int id;

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

	public Integer getId() {

		return App.srv.bls().getBookId(getConnectionPath());
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

		BookServletModel result = new BookServletModel();
		result.book = getId();
		result.section = id;
		result.sections = new ArrayList<Section>();

		List<ITreeItemInfo> list = srv().getChildren(id);

		for (ITreeItemInfo item : list) {

			Section section = result.new Section();

			section.title = item.getTitle();
			section.isGroup = item.isGroup();
			section.text = srv().getText(id);
			section.images = srv().getImages(id);

			result.sections.add(section);
		}

		return result;
	}

}
