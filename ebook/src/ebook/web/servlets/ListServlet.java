package ebook.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.web.model.ListServletModel;

public class ListServlet extends HttpServlet {

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

		String book_id = request.getParameter("id");
		if (book_id == null) {
			error_view.forward(request, response);
			return;
		}

		ListServletModel model = App.srv.bls().getModel(book_id);
		if (model == null) {
			error_view.forward(request, response);
			return;
		}

		request.setAttribute("model", model);
		request.setAttribute("root", "tmpl/list/");

		RequestDispatcher view = request
				.getRequestDispatcher("/tmpl/list/index.jsp");

		view.forward(request, response);

	}
}
