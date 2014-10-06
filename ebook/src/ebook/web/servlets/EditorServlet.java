package ebook.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.utils.PreferenceSupplier;
import ebook.web.controllers.EditorController;

public class EditorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	// private static String template = "scrolling-nav";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (PreferenceSupplier
				.getBoolean(PreferenceSupplier.LOAD_EDITOR_TEMPLATES_ON_GET)) {

			EditorController contrl = new EditorController();
			request.setAttribute("templates", contrl.getModel());

		} else
			request.setAttribute("templates",
					getServletContext().getAttribute("editor_templates"));

		RequestDispatcher view = request
				.getRequestDispatcher(getServletContext().getInitParameter(
						"ck_editor").concat("index.jsp"));

		view.forward(request, response);

	}
}
