package ebook.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.tree.ITreeItemInfo;
import ebook.web.controllers.BookController;
import ebook.web.model.BookModel;

public class BookServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	// private static String template = "scrolling-nav";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher error_view = request
				.getRequestDispatcher("/bookNotFind.jsp");

		String book_id = request.getParameter("book");
		if (book_id == null) {
			error_view.forward(request, response);
			return;
		}

		BookConnection book = App.srv.bls().getBook(book_id);
		if (book == null) {
			error_view.forward(request, response);
			return;
		}

		String section_id = request.getParameter("id");
		if (section_id == null) {

			List<ITreeItemInfo> input = book.srv().getRoot();
			if (input.isEmpty()) {
				error_view.forward(request, response);
				return;
			}
			section_id = input.get(0).getId().toString();
		}

		BookModel model = new BookController(book).getModel(section_id);
		if (model == null) {
			error_view.forward(request, response);
			return;
		}

		String isSwt = request.getParameter("swt");
		if (isSwt != null) {
			model.swtMode = true;
		}
		// model.id = id;
		request.setAttribute("model", model);
		request.setAttribute("root", "tmpl/book/");

		RequestDispatcher view = request
				.getRequestDispatcher("/tmpl/book/index.jsp");

		view.forward(request, response);

	}
}