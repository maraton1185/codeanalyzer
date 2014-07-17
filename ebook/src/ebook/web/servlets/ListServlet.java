package ebook.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.module.tree.ITreeItemInfo;
import ebook.web.controllers.ListController;
import ebook.web.model.ListModel;

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
			List<ITreeItemInfo> input = App.srv.bls().getRoot();
			if (input.isEmpty()) {
				error_view.forward(request, response);
				return;
			}
			book_id = input.get(0).getId().toString();
		}

		ListModel model = new ListController(App.srv.bls()).getModel(book_id);
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
