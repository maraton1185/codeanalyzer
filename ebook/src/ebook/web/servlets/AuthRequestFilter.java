package ebook.web.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ebook.core.App;
import ebook.web.controllers.UserController;

public class AuthRequestFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		String swt = request.getParameter("swt");
		if (swt != null && swt.equalsIgnoreCase(App.getJetty().swt())) {

			chain.doFilter(request, resp);
			return;
		}

		HttpSession session = request.getSession();
		Object user = session.getAttribute(UserController.SessionAttributeName);
		if (user != null) {

			chain.doFilter(request, resp);

		} else {
			RequestDispatcher rd = request.getRequestDispatcher(request
					.getServletContext().getInitParameter("login_url"));
			rd.forward(request, resp);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
