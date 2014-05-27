package codeanalyzer._web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeanalyzer.core.App;

public class AboutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// RequestDispatcher error_view = request
		// .getRequestDispatcher("error.jsp");

		// String c = request.getParameter("book");
		// if (c == null)
		// error_view.forward(request, response);

		request.setAttribute("host", App.jettyHost());
		RequestDispatcher view = request.getRequestDispatcher("about.jsp");
		view.forward(request, response);
	}
}
