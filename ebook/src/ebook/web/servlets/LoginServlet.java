package ebook.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ebook.core.App;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	// private static String template = "scrolling-nav";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher view = request
				.getRequestDispatcher(getServletContext().getInitParameter(
						"root_sign").concat("login.jsp"));

		view.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		if (username == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String pass = request.getParameter("password");
		if (pass == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("user", username);

		String remember = request.getParameter("remember-me");
		if (remember != null)
			session.setMaxInactiveInterval(12 * 60 * 60);

		response.sendRedirect(App.getJetty().list());

	}

}
