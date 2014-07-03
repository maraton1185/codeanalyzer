package ebook.module.book;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.book.BookServlet.BookServletModel;

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

	public BookServletModel getModel(String section_id) {

		try {
			Connection con = getConnection();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		BookServletModel result = new BookServletModel();
		result.id = section_id;
		return result;
	}

	public Integer getId() {

		return App.srv.bls().getBookId(getConnectionPath());
	}

	// *****************************************************************

}
