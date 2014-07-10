package ebook.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InfoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	// private static String template = "scrolling-nav";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// RequestDispatcher error_view = request
		// .getRequestDispatcher("error.jsp");

		// String c = request.getParameter("book");
		// if (c == null)
		// error_view.forward(request, response);

		// request.setAttribute("host", App.getJetty().host());
		// RequestDispatcher view = request.getRequestDispatcher("about.jsp");
		// RequestDispatcher view = request.getServletContext()
		// .getRequestDispatcher("/about/scrolling-nav/index.html");
		RequestDispatcher view = request
				.getRequestDispatcher("/tmpl/info/index.html");

		view.forward(request, response);

	}
}
