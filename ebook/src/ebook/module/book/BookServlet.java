package ebook.module.book;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;

public class BookServlet extends HttpServlet {

	public static class BookServletModel {

		public String id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	// private static String template = "scrolling-nav";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher error_view = request
				.getRequestDispatcher("bookNotFind.jsp");

		String book_id = request.getParameter("book");
		if (book_id == null)
			error_view.forward(request, response);

		String section_id = request.getParameter("id");
		if (section_id == null)
			error_view.forward(request, response);

		BookConnection book = App.srv.bls().getBook(book_id);
		if (book == null)
			error_view.forward(request, response);

		BookServletModel model = book.getModel(section_id);

		// model.id = id;
		request.setAttribute("model", model);

		RequestDispatcher view = request.getRequestDispatcher("book/index.jsp");

		view.forward(request, response);

	}
}
