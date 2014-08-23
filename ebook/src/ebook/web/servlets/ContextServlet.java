package ebook.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;

public class ContextServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!checkArguments(request.getParameter("book"),
				request.getParameter("id"))) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		RequestDispatcher view = request
				.getRequestDispatcher(getServletContext().getInitParameter(
						"root_context").concat("index.jsp"));

		view.forward(request, response);

	}

	private boolean checkArguments(String book_id, String section_id) {

		if (book_id == null) {
			return false;
		}

		if (section_id == null) {
			return false;
		}

		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		BookConnection book = App.srv.bl().getBook(book_id);
		if (book == null) {
			return false;
		}
		SectionInfo section = (SectionInfo) book.srv().get(id);
		if (section == null)
			return false;

		return true;

	}
}
