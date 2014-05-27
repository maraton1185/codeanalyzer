package codeanalyzer._web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		RequestDispatcher error_view = req.getRequestDispatcher("error.jsp");

		if (!req.getPathInfo().equals("/")) {
			error_view.forward(req, resp);
			return;
		}

		resp.sendRedirect(resp.encodeRedirectURL("/list"));
	}

}
