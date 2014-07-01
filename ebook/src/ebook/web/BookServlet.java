package ebook.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookServlet extends HttpServlet {

	public class Model {

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
				.getRequestDispatcher("error.jsp");

		String id = request.getParameter("id");
		if (id == null)
			error_view.forward(request, response);

		Model model = new Model();
		model.id = id;
		request.setAttribute("model", model);

		RequestDispatcher view = request.getRequestDispatcher("book/index.jsp");

		view.forward(request, response);

	}
}
