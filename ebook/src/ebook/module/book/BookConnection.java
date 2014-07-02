package ebook.module.book;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.web.BookServlet.BookServletModel;

public class BookConnection extends BaseDbPathConnection {

	public BookConnection(IPath path) throws InvocationTargetException {

		super(path, new BookStructure());

	}

	public BookConnection(String name) throws InvocationTargetException {

		super(name, new BookStructure());

	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bs(this) : service;

		return service;
	}

	public BookServletModel getModel(String section_id) {

		BookServletModel result = new BookServletModel();
		result.id = section_id;
		return result;
	}

	// *****************************************************************

}
