package ebook.module.bookList.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;

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
				.getRequestDispatcher("/error.jsp");

		ListServletModel model = App.srv.bls().getModel();

		String isSwt = request.getParameter("swt");
		if (isSwt != null) {
			model.swtMode = true;
		}

		request.setAttribute("model", model);

		RequestDispatcher view = request
				.getRequestDispatcher("/tmpl/list/index.jsp");

		view.forward(request, response);

	}
}
