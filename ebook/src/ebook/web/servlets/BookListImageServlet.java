package ebook.web.servlets;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;

public class BookListImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String book_id = request.getParameter("book");
		if (book_id == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		BufferedInputStream in = App.srv.bls().getImage(book_id);
		if (in == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		final byte[][] buffer = new byte[1][];
		response.setContentType("");
		ServletOutputStream out = response.getOutputStream();

		buffer[0] = new byte[1024];
		int n;
		while ((n = in.read(buffer[0])) > 0) {
			out.write(buffer[0], 0, n);
		}
		in.close();
		out.flush();
	}
}
