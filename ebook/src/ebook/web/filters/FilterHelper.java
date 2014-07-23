package ebook.web.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ebook.core.App;
import ebook.module.acl.AclViewModel;
import ebook.module.book.BookConnection;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.userList.tree.UserInfo;
import ebook.web.controllers.UserController;

public class FilterHelper {

	HttpServletRequest request;
	HttpServletResponse response;
	FilterChain chain;

	public FilterHelper(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) {
		super();
		this.request = request;
		this.response = response;
		this.chain = chain;
	}

	// swt not restricted
	public boolean swt() throws IOException, ServletException {

		String swt = request.getParameter("swt");
		if (swt != null && swt.equalsIgnoreCase(App.getJetty().swt())) {

			chain.doFilter(request, response);
			return true;
		}
		return false;

	}

	// get book id
	public Integer book() throws IOException, ServletException {

		String book_id = request.getParameter("book");
		{
			// if null, get root book
			if (book_id == null) {
				List<ITreeItemInfo> input = App.srv.bl().getRoot();
				if (input.isEmpty()) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
				book_id = input.get(0).getId().toString();
			}
		}

		// parse book id
		Integer id;
		try {
			id = Integer.parseInt(book_id);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		return id;

	}

	// get section id
	public Integer section(Integer book_id) throws IOException,
			ServletException {

		String section_id = request.getParameter("id");
		if (section_id == null) {

			BookConnection book = App.srv.bl().getBook(book_id.toString());
			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}

			List<ITreeItemInfo> input = book.srv().getRoot();
			if (input.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			section_id = input.get(0).getId().toString();
		}

		// parse section id
		Integer s_id;
		try {
			s_id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		return s_id;
	}

	// acl
	public void acl(List<AclViewModel> acl) throws IOException,
			ServletException {
		// not restricted
		if (acl.isEmpty()) {
			chain.doFilter(request, response);
			return;
		}

		// restricted, must login
		HttpSession session = request.getSession();
		UserInfo user = (UserInfo) session
				.getAttribute(UserController.SessionAttributeName);
		if (user != null && !user.isGroup()) {

			// is logined
			// get user role
			// check if role is in acl
			UserInfo role = App.srv.us().getRole(user);
			if (role != null && acl.contains(new AclViewModel(role.getId())))
				chain.doFilter(request, response);
			else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

		} else {
			// redirect to login page

			session.setAttribute("returnUrl", request.getRequestURI() + "?"
					+ request.getQueryString());
			RequestDispatcher rd = request.getRequestDispatcher(request
					.getServletContext().getInitParameter("login_url"));
			rd.forward(request, response);
		}
	}
}
